package lzx.qqclient.business;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import lzx.qqclient.chat.UpdatePswDialog;
import lzx.qqclient.login.LoginListen;
import lzx.qqclient.tools.QQUtils;
import lzx.qqclient.tools.ReadWriteLog;

import pub.TCPPack;
import pub.User;

public class ChatListen {
 
	/** 登录监听 */
	private LoginListen logListen;

    private ChatUtils chat;
      
	/** QQ包 */
	private TCPPack qqPack = null;

	/** 用户ID */
	private String id;

	/**
	 * 构造函数
	 * 
	 * @param logListen
	 *            登录监听
	 */
	public ChatListen(ChatUtils chat) {

		this.logListen = logListen;
		this.chat=chat;
		id = logListen.getUser().getSid();
	}

	public void getOnlineList(TCPPack qqpack) {
		// 在线用户列表的model
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("所有人");
		listModel.addElement(logListen.getUser().getSname()+"("+logListen.getUser().getSid()+")");

		Vector<User> vec = new Vector<User>();
		Object obj = (Object)qqPack.getContent();
		vec = (Vector<User>)obj;// 获取在线用户列表

		Iterator it = vec.iterator();
		while (it.hasNext()) {
			User user = (User) it.next();
			if (!id.equals(user.getSid())) {
				listModel.addElement(user.getSname() + "(" + user.getSid()
						+ ")");
			}
		}
		
		logListen.getChatFrame().getUserList().setModel(listModel);// 为用户列表刷新。

	}

	/**
	 * 把服务端发过来的公告显示在公告面板上
	 * 
	 * @param content
	 *            公告内容
	 */
	public void getBulletin(String content) {
		logListen.getChatFrame().getBulletinArea().setText(
				"【系统公告】 \n" + "   " + content + "\n");
	}

	/**
	 * 用户上线提示方法
	 * @param content User用户信息
	 */
	public void userUpOnline(Object content) {

		User user = new User();
		user = (User) content;
		if (!id.equals(user.getSid())) // 如果用户的ID不等于用户登录的ID，才做添加
		{
			logListen.getChatFrame().getUpDownLineField().setText(
					" 用户[" + user.getSid() + "]上线了\n");
		}
	}

	/**
	 * 用户下线方法提示方法
	 * 
	 * @param content User用户信息
	 */

	public void userDownOnline(String from, Object content) {

		if (!from.equals(logListen.getUser().getSid())) {

			logListen.getChatFrame().getUpDownLineField().setText(
					" 用户[" + from + "]下线了\n");

		}
	}

	/**
	 * 当用户受到强制下线包了时候执行强制下线方法
	 */
	public void coerceDownOnline() {

		JOptionPane.showMessageDialog(logListen.getChatFrame(), "对不起,你将被强制下线！",
				"强制下线提示", 1);
		System.exit(0);
	}

	/**
	 * 群聊的方法
	 * 
	 * @param qqPack  QQ包
	 */
	public void groupChat(TCPPack qqPack) {

		String myID = logListen.getUser().getSid();// 自己的ID

		String user = qqPack.getTo();

		String content = qqPack.getFrom() + "  对  所有人 说: \n"
				+ qqPack.getContent() + "\n";

		if (!user.equals(myID)) {
			
			logListen.getChatFrame().getContentArea().append(content);
			
			ReadWriteLog.writeLog(myID, content);// 保存聊天记录

		}

	}
	/**
	 * 私聊方法
	 * @param qqPack QQ包
	 */
	public void privateTalk(TCPPack qqPack) {

		String content = qqPack.getFrom() + " " + QQUtils.formatT()
				+ " 对 你 说: \n" + qqPack.getContent() + "\n";

		logListen.getChatFrame().getContentArea().append(content);

		ReadWriteLog.writeLog(logListen.getUser().getSid(), content);// 保存聊天记录

	}


	/**
	 * 停止服务的方法
	 */
	public void stopServer() {

		JOptionPane.showMessageDialog(logListen.getChatFrame(),
				"对不起，服务器已关闭，你将被强制下线。", "下线提示", 1);
		System.exit(0);
	}

	/**
	 *  修改密码的方法
	 * @param content 修改密码成功和失败的信息
	 */
	public void updatePassword(String content) { // 更改密码方法。

		if (content.equals("Password_Sucess")) {
			UpdatePswDialog.getUpdatePsw().dispose();//消掉修改密码对话框
			JOptionPane.showMessageDialog(logListen.getChatFrame(), "修改密码成功！",
					"密码提示", 1);
		} else if (content.equals("Password_Error")) {
			JOptionPane.showMessageDialog(logListen.getChatFrame(),
					"原密码错误，请重新修改密码！", "密码提示", 1);
		} else {
			JOptionPane.showMessageDialog(logListen.getChatFrame(), "修改密码失败！",
					"密码提示", 1);
		}
	}

	
	public void updateUser(Object content){
		
		User user = (User)content;	
		logListen.getChatFrame().setTitle("用户："+user.getSname()+ " [" + user.getSid()+ "]"); // 修改用户标题	
		JOptionPane.showMessageDialog(logListen.getChatFrame(),"你的资料已经被管理员修改！！","用户资料提示", 1);
		
	}
	/**
	 * 线程启动的方法
	 */	
}
