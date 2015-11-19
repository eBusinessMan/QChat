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

	/** ������ */
	private ObjectInputStream ois = null;

	/** ����� */
	private ObjectOutputStream oos = null;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	/** �洢ServerListenThread�ഫ������ֵ */
	private Socket socket;

	/**
	 * ���췽��
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
	 * �߳������ķ���
	 */
	public void run() {
		
		while (true) {
			try {
				QQPack qqPack = new QQPack();
				Object obj = ois.readObject();

				if (obj instanceof QQPack)// �жϽ��յ��İ��Ƿ�����QQPack
				{
					qqPack = (QQPack) obj;

					QQPackType type = qqPack.getType(); // ��ȡ���������͡�
					String from = qqPack.getFrom(); // ��ȡ�������﷢�ġ��൱���û�ID��
					String to = qqPack.getTo(); // ��ȡ��Ҫ�������
					Object content = qqPack.getContent(); // �񵽵��������ݡ�

					if (type == QQPackType.LOGIN) // ����ǵ�¼��
					{
						checkLogin(from, content.toString());// ��֤�û����뷽����

					} else if (type == QQPackType.UPDATE_PASSWORD) {

						updatePassword(to,from,content.toString());// �������뷽����

					} else if (type == QQPackType.USER_WODNLINE) // �û����ߡ�
					{
						userDownLine(from);// �û����߰���from�û�ID

					} else if (type == QQPackType.PRIVATE_TALK) { // ˽�İ�

						privateTalk(qqPack);//˽�ķ���
					} else if (type == QQPackType.GROUP_CHAT) {

						groupChat(qqPack); // Ⱥ�ķ�����
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
	 * ��֤�û���¼���û���������ķ���
	 * 
	 * @param id
	 *            ��֤�û�ID
	 * @param password
	 *            ��֤�û�����
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
				userDao.updateOnline(id, true); // ��¼�ɹ��󣬰�״̬��Ϊ���ߡ�
				try {
					oos.writeObject(successPack);// �û���¼�ɹ���Ϣ���ؿͷ���
					oos.flush();
				} catch (IOException e) {

					e.printStackTrace();
				}

				QQUtils.threadMap.put(id, ServerReceiveThread.this); // ���û���ID���̷߳ŵ�Map����


				// ������֪ͨ����������Ա��****�����ˡ�
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_LINE);
				qqPack.setContent(user);
				QQUtils.sendToAll(qqPack);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
				ServerFrame.getServerFrame().getServerPanel()
						.updateOnlineUserData();
				
				// -----------------------------------�������û��б�������û�
				QQPack onlineUserPack = new QQPack();
				// ������ݵ�Vector,������list��Ū��ת����Vector����
				Vector<User> ve = new Vector<User>();
				List<User> onlineList = new ArrayList<User>();// ��ȡ�����û���
				onlineList = userDao.getOnlineUser(); // ��ȡ��ǰ�����û�����
				for (User user1 : onlineList) { //
					// ��list���б�������list�����user��ŵ�vector�С�
					ve.add(user1);
				}
				onlineUserPack.setType(QQPackType.ONLINE_LIST);
				onlineUserPack.setContent(ve); // ���÷�������
				QQUtils.sendToAll(onlineUserPack);// ���������û����������û�
				// --------------------------------------------

				// д��־
				 LogDaoImpl logDao = new LogDaoImpl();
				 String str = "[" + QQUtils.formatTime() + "]: " + id +
				 "�û�������!\n";
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
						 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument()
								.getLength());//�ù�궨λ����ȡ������
				// �����ݿ�����д��־
				logDao.addLog(QQUtils.formatDate(), str);
			} else {
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.LOGIN_FAIL); // ���õ�¼ʧ�ܡ�
				qqPack.setContent("Online");
				try {
					oos.writeObject(qqPack);
					oos.flush();
				} catch (IOException e) {

					e.printStackTrace();
				} // �����¼���ɹ����ظ��û����Ѿ���¼!

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
	 * Ⱥ�ĵķ���
	 * @param qqPack QQ��
	 */
	private void groupChat(QQPack qqPack) {

		 QQUtils.sendToAll(qqPack);//�������û�����
	}

	/**
	 * ˽�ķ���
	 * @param qqPack QQ��
	 */
	public void privateTalk(QQPack qqPack){//˽�ķ���
		
		String id = qqPack.getTo();
		System.out.println("id=="+id);
		QQUtils.sendTo(qqPack, id);
		
	}
	
	/**
	 * �û����ߵķ���
	 * @param ID ���û���ID
	 */
	
	public void userDownLine(String ID){
		
		QQPack downLinePack = new QQPack();
		downLinePack.setType(QQPackType.USER_WODNLINE);//���ð�����Ϊ�û����߰�
		downLinePack.setFrom(ID);
		downLinePack.setContent("�û�["+ID+"]������");
		QQUtils.sendToAll(downLinePack);//�������û������߰�
		
		QQUtils.threadMap.remove(ID);//�����ߵ��û��߳�ɾ��

		userDao.updateOnline(ID,false);//�����û����ݿ��״̬	
		
		ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
		ServerFrame.getServerFrame().getServerPanel()
				.updateOnlineUserData();//���·�����������û���
		

		// -----------------------------------�������û��б�������û�
		QQPack onlineUserPack = new QQPack();
		// ������ݵ�Vector,������list��Ū��ת����Vector����
		Vector<User> ve = new Vector<User>();
		List<User> onlineList = new ArrayList<User>();// ��ȡ�����û���
		onlineList = userDao.getOnlineUser(); // ��ȡ��ǰ�����û�����
		for (User user1 : onlineList) { //
			// ��list���б�������list�����user��ŵ�vector�С�
			ve.add(user1);
		}
		onlineUserPack.setType(QQPackType.ONLINE_LIST);
		onlineUserPack.setContent(ve); // ���÷�������
		QQUtils.sendToAll(onlineUserPack);// ���������û����������û�
		
		//д��־
		 LogDaoImpl logDao = new LogDaoImpl();
		 String str = "[" + QQUtils.formatTime() + "]: " + ID +
		 "�û�������!\n";
		 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
		 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
				 ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument()
						.getLength());//�ù�궨λ����ȡ������
		// �����ݿ�����д��־
		logDao.addLog(QQUtils.formatDate(), str);
	}
	
	/**
	 * �����û�����ķ���
	 * @param id �û�ID
	 * @param oldPassword ������
	 * @param newPassword ������
	 */
	
	public void updatePassword(String id, String oldPassword, String newPassword){// �������뷽����
		
		Object obj = userDao.checkUser(id, oldPassword); // ѡ��֤����ľ������Ƿ���ȷ��
		
	
		if(obj instanceof User){//��������ȷ
			
			userDao.updatePassword(id, oldPassword, newPassword);
			
			QQPack pswSucessPack = new QQPack();
			pswSucessPack.setType(QQPackType.UPDATE_PASSWORD);
			pswSucessPack.setContent("Password_Sucess"); // ���ø�������ɹ���
			QQUtils.sendTo(pswSucessPack, id); 
			
			ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
			ServerFrame.getServerFrame().getServerPanel()
					.updateOnlineUserData();//���·�����������û���
			
		}else {
			QQPack pswErrorPack = new QQPack();
			pswErrorPack.setType(QQPackType.UPDATE_PASSWORD);
			pswErrorPack.setContent("Password_Error"); // ���ø������벻�ɹ���
			QQUtils.sendTo(pswErrorPack, id); 

		}
	}
	

	/**
	 * ������Ϣ��
	 * 
	 * @param message
	 *            Ҫ���͵���Ϣ��
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
	 * ��ȡ������
	 * @return ObjectInputStream ������
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * ��ȡ�����
	 * @return ObjectOutputStream �����
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * ��ȡScoket
	 * @return Socket 
	 */
	public Socket getSocket() {
		return socket;
	}
}
