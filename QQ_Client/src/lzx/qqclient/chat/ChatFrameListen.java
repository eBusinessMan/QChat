package lzx.qqclient.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import lzx.qqclient.business.ChatUtils;
import lzx.qqclient.tools.QQUtils;
import lzx.qqclient.tools.ReadWriteLog;
import pub.TCPPack;
import pub.QQPackType;

/**
 * 聊天主界面监听类
 @author luozhixiao
 *
 */
public class ChatFrameListen implements ActionListener {

	/** 聊天主界面 */
	private ChatFrame frame;

	/** 聊天记录，面板是否隐藏 */
	private boolean hide = false;

	/** 输出流 */
	private ChatUtils chat;

	/**
	 * 构造函数
	 */
	public ChatFrameListen(final ChatFrame frame,ChatUtils chat) {
		this.frame = frame;
		this.chat=chat;
	}

	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {

		// 修改密码
		if (e.getSource() == frame.getAmendPswButton()) {
			new UpdatePswDialog(frame,chat);

		} else if (e.getSource()==frame.getChatLogButton()) {// 聊天记录
			chatLogClick();

		} else if (e.getSource() == frame.getSendButton()) {// 发送按钮
			sendMessage();
		} else if (e.getSource() == frame.getCloseButton()) {// 关闭按钮
			// 发送用户下线包。
			TCPPack downLinePack = new TCPPack();
			downLinePack.setType(QQPackType.USER_WODNLINE);
			downLinePack.setFrom(frame.getUser().getSid());
			try {
				chat.setTcpPack(downLinePack);
				chat.sendObject();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);

		}

	}

	/**
	 * 聊天记录按钮监听事件
	 */
	public void chatLogClick() {
		if (hide == true) {

			frame.getChatLogPanel().setVisible(false);
			frame.getRightPanel().setVisible(true);
			frame.remove(frame.getChatLogPanel());
			frame.getSpp().setBottomComponent(frame.getRightPanel());
			frame.setSize(560, 500);
			frame.validate();
			hide = false;

		} else if (hide == false) {

			frame.getChatLogPanel().setVisible(true);
			frame.getChatLogArea().setText("");
			ReadWriteLog.readLog(frame.getUser().getSid(),frame);//读取聊天记录
			frame.getRightPanel().setVisible(false);
			frame.remove(frame.getRightPanel());
			frame.getSpp().setBottomComponent(frame.getChatLogPanel());
			frame.setSize(700, 500);
			frame.validate();
			hide = true;
		}
	}

	/**
	 * 发送信息的方法（群聊和私聊）
	 *
	 */
	private void sendMessage() {

		String user = null;// 在线列表选择的用户

		if (frame.getUserList().getSelectedValue() == null) {// 如果没有选择用户

			JOptionPane.showMessageDialog(frame, "请选择聊天对象");
			return;
		} else {
			user = frame.getUserList().getSelectedValue().toString();
		}

		String content = frame.getSendArea().getText();// 获取发送文本框里面的内容

		if (content == null || content.length() == 0) {
			JOptionPane.showMessageDialog(frame, "不能发送空消息", "消息提示", 1);
			return;
		} else if (content.length() > 2000) {
			JOptionPane.showMessageDialog(frame, "发送的消息不能大于2000个字", "消息提示", 1);
			return;
		}

		if (user.equals("所有人")) {// 当用户对所有人说的时候

			String con = "我 " + QQUtils.formatT() + " 对 所有人 说：" + "\n"
					+ content + "\n";
			frame.getContentArea().append(con);

			TCPPack groupPack = new TCPPack();

			groupPack.setType(QQPackType.GROUP_CHAT);// 设置包类型为群聊
			groupPack.setFrom(frame.getUser().getSname() + "("
					+ frame.getUser().getSid() + ") " + QQUtils.formatT());
			groupPack.setContent(content);
			groupPack.setTo(frame.getUser().getSid());
            chat.setTcpPack(groupPack);
			try {
				chat.sendObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
			frame.getSendArea().setText("");// 清空发送文本框
			// 把内容写入聊天日志
			ReadWriteLog.writeLog(frame.getUser().getSid(), con);

		}else if(user.equals(frame.getUser().getSname()+"("+frame.getUser().getSid()+")")){
			
			JOptionPane.showMessageDialog(frame, "不能对自已发信息!","信息提示",1);
			return;
			
		} else {// 私聊

			int i = user.indexOf("(");

			String id = user.substring(i + 1, i + 6); // 得到要发送的人

			String con = "我 " + QQUtils.formatT() + " 对 " + user + " 说：" + "\n"+ content + "\n";
			frame.getContentArea().append(con);

			// 发私聊包

			TCPPack priChatPack = new TCPPack();
			priChatPack.setType(QQPackType.PRIVATE_TALK);// 设置包类型为私聊包
			priChatPack.setFrom(frame.getUser().getSname() + "("
					+ frame.getUser().getSid() + ") ");
			priChatPack.setTo(id);
			priChatPack.setContent(content);
            chat.setTcpPack(priChatPack);
			try {
				chat.sendObject();
			} catch (Exception e) {
				e.printStackTrace();
			}

			frame.getSendArea().setText("");// 清空发送文本框

			//把内容写入聊天日志
			ReadWriteLog.writeLog(frame.getUser().getSid(), con);

		}

	}

}
