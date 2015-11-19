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
 
	/** ��¼���� */
	private LoginListen logListen;

    private ChatUtils chat;
      
	/** QQ�� */
	private TCPPack qqPack = null;

	/** �û�ID */
	private String id;

	/**
	 * ���캯��
	 * 
	 * @param logListen
	 *            ��¼����
	 */
	public ChatListen(ChatUtils chat) {

		this.logListen = logListen;
		this.chat=chat;
		id = logListen.getUser().getSid();
	}

	public void getOnlineList(TCPPack qqpack) {
		// �����û��б��model
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("������");
		listModel.addElement(logListen.getUser().getSname()+"("+logListen.getUser().getSid()+")");

		Vector<User> vec = new Vector<User>();
		Object obj = (Object)qqPack.getContent();
		vec = (Vector<User>)obj;// ��ȡ�����û��б�

		Iterator it = vec.iterator();
		while (it.hasNext()) {
			User user = (User) it.next();
			if (!id.equals(user.getSid())) {
				listModel.addElement(user.getSname() + "(" + user.getSid()
						+ ")");
			}
		}
		
		logListen.getChatFrame().getUserList().setModel(listModel);// Ϊ�û��б�ˢ�¡�

	}

	/**
	 * �ѷ���˷������Ĺ�����ʾ�ڹ��������
	 * 
	 * @param content
	 *            ��������
	 */
	public void getBulletin(String content) {
		logListen.getChatFrame().getBulletinArea().setText(
				"��ϵͳ���桿 \n" + "   " + content + "\n");
	}

	/**
	 * �û�������ʾ����
	 * @param content User�û���Ϣ
	 */
	public void userUpOnline(Object content) {

		User user = new User();
		user = (User) content;
		if (!id.equals(user.getSid())) // ����û���ID�������û���¼��ID���������
		{
			logListen.getChatFrame().getUpDownLineField().setText(
					" �û�[" + user.getSid() + "]������\n");
		}
	}

	/**
	 * �û����߷�����ʾ����
	 * 
	 * @param content User�û���Ϣ
	 */

	public void userDownOnline(String from, Object content) {

		if (!from.equals(logListen.getUser().getSid())) {

			logListen.getChatFrame().getUpDownLineField().setText(
					" �û�[" + from + "]������\n");

		}
	}

	/**
	 * ���û��ܵ�ǿ�����߰���ʱ��ִ��ǿ�����߷���
	 */
	public void coerceDownOnline() {

		JOptionPane.showMessageDialog(logListen.getChatFrame(), "�Բ���,�㽫��ǿ�����ߣ�",
				"ǿ��������ʾ", 1);
		System.exit(0);
	}

	/**
	 * Ⱥ�ĵķ���
	 * 
	 * @param qqPack  QQ��
	 */
	public void groupChat(TCPPack qqPack) {

		String myID = logListen.getUser().getSid();// �Լ���ID

		String user = qqPack.getTo();

		String content = qqPack.getFrom() + "  ��  ������ ˵: \n"
				+ qqPack.getContent() + "\n";

		if (!user.equals(myID)) {
			
			logListen.getChatFrame().getContentArea().append(content);
			
			ReadWriteLog.writeLog(myID, content);// ���������¼

		}

	}
	/**
	 * ˽�ķ���
	 * @param qqPack QQ��
	 */
	public void privateTalk(TCPPack qqPack) {

		String content = qqPack.getFrom() + " " + QQUtils.formatT()
				+ " �� �� ˵: \n" + qqPack.getContent() + "\n";

		logListen.getChatFrame().getContentArea().append(content);

		ReadWriteLog.writeLog(logListen.getUser().getSid(), content);// ���������¼

	}


	/**
	 * ֹͣ����ķ���
	 */
	public void stopServer() {

		JOptionPane.showMessageDialog(logListen.getChatFrame(),
				"�Բ��𣬷������ѹرգ��㽫��ǿ�����ߡ�", "������ʾ", 1);
		System.exit(0);
	}

	/**
	 *  �޸�����ķ���
	 * @param content �޸�����ɹ���ʧ�ܵ���Ϣ
	 */
	public void updatePassword(String content) { // �������뷽����

		if (content.equals("Password_Sucess")) {
			UpdatePswDialog.getUpdatePsw().dispose();//�����޸�����Ի���
			JOptionPane.showMessageDialog(logListen.getChatFrame(), "�޸�����ɹ���",
					"������ʾ", 1);
		} else if (content.equals("Password_Error")) {
			JOptionPane.showMessageDialog(logListen.getChatFrame(),
					"ԭ��������������޸����룡", "������ʾ", 1);
		} else {
			JOptionPane.showMessageDialog(logListen.getChatFrame(), "�޸�����ʧ�ܣ�",
					"������ʾ", 1);
		}
	}

	
	public void updateUser(Object content){
		
		User user = (User)content;	
		logListen.getChatFrame().setTitle("�û���"+user.getSname()+ " [" + user.getSid()+ "]"); // �޸��û�����	
		JOptionPane.showMessageDialog(logListen.getChatFrame(),"��������Ѿ�������Ա�޸ģ���","�û�������ʾ", 1);
		
	}
	/**
	 * �߳������ķ���
	 */	
}
