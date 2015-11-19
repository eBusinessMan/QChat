package lzx.qqserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;
import lzx.qqserver.tools.QQUtils;

import pub.QQPack;
import pub.QQPackType;
import pub.User;


public class ServerReceiveThread extends Thread {

	/** 输入流 */
	private ObjectInputStream ois = null;

	/** 输出流 */
	private ObjectOutputStream oos = null;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	/** 存储ServerListenThread类传过来的值 */
	private Socket socket;

	/**
	 * 构造方法
	 */
	public ServerReceiveThread(final Socket socket) {
		this.socket = socket;
		userDao = new UserDaoImpl();
		
	
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * 线程启动的方法
	 */
	public void run() {
		
		while (true) {
			try {
				QQPack qqPack = new QQPack();
				Object obj = ois.readObject();

				if (obj instanceof QQPack)// 判断接收到的包是否属于QQPack
				{
					qqPack = (QQPack) obj;

					QQPackType type = qqPack.getType(); // 获取到包的类型。
					String from = qqPack.getFrom(); // 获取到从哪里发的。相当于用户ID。
					String to = qqPack.getTo(); // 获取到要发到哪里。
					Object content = qqPack.getContent(); // 获到到发的内容。

					if (type == QQPackType.LOGIN) // 如果是登录。
					{
						checkLogin(from, content.toString());// 验证用户密码方法。

					} else if (type == QQPackType.UPDATE_PASSWORD) {

						updatePassword(to,from,content.toString());// 更新密码方法。

					} else if (type == QQPackType.USER_WODNLINE) // 用户下线。
					{
						userDownLine(from);// 用户下线包。from用户ID

					} else if (type == QQPackType.PRIVATE_TALK) { // 私聊包

						privateTalk(qqPack);//私聊方法
					} else if (type == QQPackType.GROUP_CHAT) {

						groupChat(qqPack); // 群聊方法。
					}

				}
			} catch (IOException e) {

				//e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {

				//e.printStackTrace();
				break;
			}
		}

	}

	/**
	 * 验证用户登录的用户名和密码的方法
	 * 
	 * @param id
	 *            验证用户ID
	 * @param password
	 *            验证用户密码
	 */
	public void checkLogin(String id, String password) {

		
		Object result = userDao.checkUser(id, password);

		if (result instanceof User) {

			User user = (User) result;
			int i = Integer.parseInt(user.getNisonline());
			if (i == 0) {
				QQPack successPack = new QQPack();
				successPack.setType(QQPackType.LOGIN_SUCCEESS);
				successPack.setContent(result);
				userDao.updateOnline(id, true); // 登录成功后，把状态改为在线。
				try {
					oos.writeObject(successPack);// 用户登录成功信息发回客服端
					oos.flush();
				} catch (IOException e) {

					e.printStackTrace();
				}

				QQUtils.threadMap.put(id, ServerReceiveThread.this); // 将用户的ID和线程放到Map里面


				// 发公告通知所有在线人员：****上线了。
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_LINE);
				qqPack.setContent(user);
				QQUtils.sendToAll(qqPack);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
				ServerFrame.getServerFrame().getServerPanel()
						.updateOnlineUserData();
				
				// -----------------------------------发在线用户列表给所有用户
				QQPack onlineUserPack = new QQPack();
				// 存放数据的Vector,用来从list类弄中转换到Vector类型
				Vector<User> ve = new Vector<User>();
				List<User> onlineList = new ArrayList<User>();// 获取在线用户。
				onlineList = userDao.getOnlineUser(); // 获取当前在线用户方法
				for (User user1 : onlineList) { //
					// 对list进行遍历。把list里面的user存放到vector中。
					ve.add(user1);
				}
				onlineUserPack.setType(QQPackType.ONLINE_LIST);
				onlineUserPack.setContent(ve); // 设置发送内容
				QQUtils.sendToAll(onlineUserPack);// 发送在线用户包给所有用户
				// --------------------------------------------

				// 写日志
				 LogDaoImpl logDao = new LogDaoImpl();
				 String str = "[" + QQUtils.formatTime() + "]: " + id +
				 "用户上线了!\n";
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
						 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument()
								.getLength());//让光标定位到获取的行数
				// 往数据库里面写日志
				logDao.addLog(QQUtils.formatDate(), str);
			} else {
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.LOGIN_FAIL); // 设置登录失败。
				qqPack.setContent("Online");
				try {
					oos.writeObject(qqPack);
					oos.flush();
				} catch (IOException e) {

					e.printStackTrace();
				} // 如果登录不成功，回个用户名已经登录!

			}

		} else {
			QQPack failPack = new QQPack();
			failPack.setType(QQPackType.LOGIN_FAIL);
			failPack.setContent(result);

			try {
				oos.writeObject(failPack);
				oos.flush();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 群聊的方法
	 * @param qqPack QQ包
	 */
	private void groupChat(QQPack qqPack) {

		 QQUtils.sendToAll(qqPack);//向所有用户发送
	}

	/**
	 * 私聊方法
	 * @param qqPack QQ包
	 */
	public void privateTalk(QQPack qqPack){//私聊方法
		
		String id = qqPack.getTo();
		System.out.println("id=="+id);
		QQUtils.sendTo(qqPack, id);
		
	}
	
	/**
	 * 用户下线的方法
	 * @param ID 传用户的ID
	 */
	
	public void userDownLine(String ID){
		
		QQPack downLinePack = new QQPack();
		downLinePack.setType(QQPackType.USER_WODNLINE);//设置包类型为用户下线包
		downLinePack.setFrom(ID);
		downLinePack.setContent("用户["+ID+"]下线了");
		QQUtils.sendToAll(downLinePack);//对所有用户发下线包
		
		QQUtils.threadMap.remove(ID);//把下线的用户线程删除

		userDao.updateOnline(ID,false);//更新用户数据库的状态	
		
		ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
		ServerFrame.getServerFrame().getServerPanel()
				.updateOnlineUserData();//更新服务管理面板的用户表
		

		// -----------------------------------发在线用户列表给所有用户
		QQPack onlineUserPack = new QQPack();
		// 存放数据的Vector,用来从list类弄中转换到Vector类型
		Vector<User> ve = new Vector<User>();
		List<User> onlineList = new ArrayList<User>();// 获取在线用户。
		onlineList = userDao.getOnlineUser(); // 获取当前在线用户方法
		for (User user1 : onlineList) { //
			// 对list进行遍历。把list里面的user存放到vector中。
			ve.add(user1);
		}
		onlineUserPack.setType(QQPackType.ONLINE_LIST);
		onlineUserPack.setContent(ve); // 设置发送内容
		QQUtils.sendToAll(onlineUserPack);// 发送在线用户包给所有用户
		
		//写日志
		 LogDaoImpl logDao = new LogDaoImpl();
		 String str = "[" + QQUtils.formatTime() + "]: " + ID +
		 "用户下线了!\n";
		 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
		 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument()
						.getLength());//让光标定位到获取的行数
		// 往数据库里面写日志
		logDao.addLog(QQUtils.formatDate(), str);
	}
	
	/**
	 * 更改用户密码的方法
	 * @param id 用户ID
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 */
	
	public void updatePassword(String id, String oldPassword, String newPassword){// 更新密码方法。
		
		Object obj = userDao.checkUser(id, oldPassword); // 选验证输入的旧密码是否正确。
		
	
		if(obj instanceof User){//旧密码正确
			
			userDao.updatePassword(id, oldPassword, newPassword);
			
			QQPack pswSucessPack = new QQPack();
			pswSucessPack.setType(QQPackType.UPDATE_PASSWORD);
			pswSucessPack.setContent("Password_Sucess"); // 设置更改密码成功。
			QQUtils.sendTo(pswSucessPack, id); 
			
			ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
			ServerFrame.getServerFrame().getServerPanel()
					.updateOnlineUserData();//更新服务管理面板的用户表
			
		}else {
			QQPack pswErrorPack = new QQPack();
			pswErrorPack.setType(QQPackType.UPDATE_PASSWORD);
			pswErrorPack.setContent("Password_Error"); // 设置更改密码不成功。
			QQUtils.sendTo(pswErrorPack, id); 

		}
	}
	

	/**
	 * 发送信息。
	 * 
	 * @param message
	 *            要发送的信息。
	 */
	public void sendMessage(Object message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取输入流
	 * @return ObjectInputStream 输入流
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * 获取输出流
	 * @return ObjectOutputStream 输出流
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * 获取Scoket
	 * @return Socket 
	 */
	public Socket getSocket() {
		return socket;
	}
}
