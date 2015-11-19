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
 * ���������������
 @author luozhixiao
 *
 */
public class ChatFrameListen implements ActionListener {

	/** ���������� */
	private ChatFrame frame;

	/** �����¼������Ƿ����� */
	private boolean hide = false;

	/** ����� */
	private ChatUtils chat;

	/**
	 * ���캯��
	 */
	public ChatFrameListen(final ChatFrame frame,ChatUtils chat) {
		this.frame = frame;
		this.chat=chat;
	}

	/**
	 * �¼������ķ���
	 */
	public void actionPerformed(ActionEvent e) {

		// �޸�����
		if (e.getSource() == frame.getAmendPswButton()) {
			new UpdatePswDialog(frame,chat);

		} else if (e.getSource()==frame.getChatLogButton()) {// �����¼
			chatLogClick();

		} else if (e.getSource() == frame.getSendButton()) {// ���Ͱ�ť
			sendMessage();
		} else if (e.getSource() == frame.getCloseButton()) {// �رհ�ť
			// �����û����߰���
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
	 * �����¼��ť�����¼�
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
			ReadWriteLog.readLog(frame.getUser().getSid(),frame);//��ȡ�����¼
			frame.getRightPanel().setVisible(false);
			frame.remove(frame.getRightPanel());
			frame.getSpp().setBottomComponent(frame.getChatLogPanel());
			frame.setSize(700, 500);
			frame.validate();
			hide = true;
		}
	}

	/**
	 * ������Ϣ�ķ�����Ⱥ�ĺ�˽�ģ�
	 *
	 */
	private void sendMessage() {

		String user = null;// �����б�ѡ����û�

		if (frame.getUserList().getSelectedValue() == null) {// ���û��ѡ���û�

			JOptionPane.showMessageDialog(frame, "��ѡ���������");
			return;
		} else {
			user = frame.getUserList().getSelectedValue().toString();
		}

		String content = frame.getSendArea().getText();// ��ȡ�����ı������������

		if (content == null || content.length() == 0) {
			JOptionPane.showMessageDialog(frame, "���ܷ��Ϳ���Ϣ", "��Ϣ��ʾ", 1);
			return;
		} else if (content.length() > 2000) {
			JOptionPane.showMessageDialog(frame, "���͵���Ϣ���ܴ���2000����", "��Ϣ��ʾ", 1);
			return;
		}

		if (user.equals("������")) {// ���û���������˵��ʱ��

			String con = "�� " + QQUtils.formatT() + " �� ������ ˵��" + "\n"
					+ content + "\n";
			frame.getContentArea().append(con);

			TCPPack groupPack = new TCPPack();

			groupPack.setType(QQPackType.GROUP_CHAT);// ���ð�����ΪȺ��
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
			frame.getSendArea().setText("");// ��շ����ı���
			// ������д��������־
			ReadWriteLog.writeLog(frame.getUser().getSid(), con);

		}else if(user.equals(frame.getUser().getSname()+"("+frame.getUser().getSid()+")")){
			
			JOptionPane.showMessageDialog(frame, "���ܶ����ѷ���Ϣ!","��Ϣ��ʾ",1);
			return;
			
		} else {// ˽��

			int i = user.indexOf("(");

			String id = user.substring(i + 1, i + 6); // �õ�Ҫ���͵���

			String con = "�� " + QQUtils.formatT() + " �� " + user + " ˵��" + "\n"+ content + "\n";
			frame.getContentArea().append(con);

			// ��˽�İ�

			TCPPack priChatPack = new TCPPack();
			priChatPack.setType(QQPackType.PRIVATE_TALK);// ���ð�����Ϊ˽�İ�
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

			frame.getSendArea().setText("");// ��շ����ı���

			//������д��������־
			ReadWriteLog.writeLog(frame.getUser().getSid(), con);

		}

	}

}
