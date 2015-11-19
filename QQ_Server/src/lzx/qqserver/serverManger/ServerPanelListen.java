package lzx.qqserver.serverManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;
import lzx.qqserver.thread.ServerListenThread;
import lzx.qqserver.tools.ConfigFile;
import lzx.qqserver.tools.QQUtils;
import lzx.qqserver.userManger.UserPanel;

import pub.QQPack;
import pub.QQPackType;


/**
 * ���������������
 *@author luozhixiao
 *
 */
public class ServerPanelListen implements ActionListener {

	/** ������� */
	private ServerPanel serverPanel;

	/** ��д��־�� */
	private LogDaoImpl logDao;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	/** �û����� */
	private UserPanel userPanel;

	/** ����ͨѶSocket */
	private ServerSocket serverSocket = null;

	/**
	 * ���캯��
	 * @param serverPanel ����������
	 */
	public ServerPanelListen(final ServerPanel serverPanel) {

		this.serverPanel = serverPanel;
		logDao = new LogDaoImpl();
		userDao = new UserDaoImpl();

	}
	/**
	 * �¼������ķ���
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == serverPanel.getSendBulletinBtn()) {// ��������

			SendBulletin();

		} else if (e.getSource() == serverPanel.getStartSeverBtn()) {// ��ʼ����

			startServer();

		} else if (e.getSource() == serverPanel.getStopServerBtn()) {// ֹͣ����

			stopServer();

		} else if (e.getSource() == serverPanel.getCoerceDownBtn()) {// ǿ������

			CoerceDown();
		}

	}
	/**
	 * ��ʼ����ķ���
	 */
	private void startServer() {

		// Socket

		ConfigFile confile = new ConfigFile("./config/config.ini");

		int port = Integer.parseInt(confile.getKeyValue("port"));// �������ļ������ȡ�˿�
		String ip = confile.getKeyValue("ip");
		try {
			serverSocket = new ServerSocket(port, 5, InetAddress.getByName(ip));

			new ServerListenThread(serverSocket).start();// �����̼߳����ͻ�������

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(serverPanel,
					"��������ʧ��, �����Ƕ˿ڱ�ռ��,����������!", "������ʾ", 1);

		}

		if (serverSocket != null) {// ˵���������Ѿ�����
			serverPanel.getImageLabel().setIcon(QQUtils.serverstart);// ���������ת��
			serverPanel.getStartSeverBtn().setEnabled(false);
			serverPanel.getStopServerBtn().setEnabled(true);
			serverPanel.getCoerceDownBtn().setEnabled(true);
			serverPanel.getSendBulletinBtn().setEnabled(true);
			serverPanel.getSendBulletinArea().setEditable(true);// �����ı�������Ϊ�ɱ༭

			String str = "[" + QQUtils.formatTime() + "]: ���������ɹ�!\n";
			serverPanel.getServerLogArea().append(str);
			serverPanel.getServerLogArea().setCaretPosition(
					serverPanel.getServerLogArea().getDocument().getLength());
			// �����ݿ�����д��־
			logDao.addLog(QQUtils.formatDate(), str);

		} else {

			String str = "[" + QQUtils.formatTime() + "]: ��������ʧ��!\n";
			serverPanel.getServerLogArea().append(str);
			serverPanel.getServerLogArea().setCaretPosition(
					serverPanel.getServerLogArea().getDocument().getLength());
			// �����ݿ�����д��־
			logDao.addLog(QQUtils.formatDate(), str);
		}
	}

	/**
	 * ���͹��淽��
	 */
	public void SendBulletin() {
		// 3) ���� ������Ϣ���� ���� ��ť,�������û���������Ϣ;
		// ���ͳɹ�����ʾ�ɹ�;
		// 4) ������Ϣһ�����ɷ���200���ַ�(ע:1��Ӣ����ĸ��1�����ֶ���1���ַ�);
		String bulletin = serverPanel.getSendBulletinArea().getText().trim();// ��ȡ���͹��������

		if (bulletin.length() == 0) {
			JOptionPane.showMessageDialog(serverPanel, "������Ϣ����Ϊ��!", "������ʾ", 1);
			return;
		} else if (bulletin.length() > 200) {
			JOptionPane.showMessageDialog(serverPanel, "������Ϣ�������ֻ��200���ַ�!",
					"������ʾ", 1);
			return;
		}

		QQPack qqpack = new QQPack();
		qqpack.setType(QQPackType.BULLETIN);// ���ð�����Ϊ�����
		qqpack.setContent(bulletin);
		QQUtils.sendToAll(qqpack);// �ѹ��淢�������û�

		JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "���͹���ɹ���",
				"������ʾ", 1);
		serverPanel.getSendBulletinArea().setText("");// ���깫�棬�����ı����ÿ�

		// д������־
		LogDaoImpl logDao = new LogDaoImpl();
		String str = "[" + QQUtils.formatTime() + "]: �����˹���!\n";
		serverPanel.getServerLogArea().append(str);
		// �����ݿ�����д��־
		logDao.addLog(QQUtils.formatDate(), str);

	}

	/**
	 * ֹͣ���񷽷�
	 */
	public void stopServer() {
		//		
		// 3) ֹͣ������ͷ�һϵ����Դ:
		// a) �ͷ���ռ�Ķ˿ں�;
		// b) ǿ�����пͻ��������û�����;
		// c) �ͷ���ռ�ķ������Դ(�߳�);
		// d) �������ݴ洢�����е���Ӧ����;
		// e) ˢ�½�����ʾ (1.�����û���Ϣ, 2.�û����������û���Ϣ);

		// int rows = serverPanel.getUserTable().getRowCount();

		int i = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
				"ȷ��Ҫֹͣ��������", "������ʾ", JOptionPane.YES_NO_OPTION);

		if (i == 0) {// ȷ��Ҫ�رշ�����

			userDao.setAllOffline();// �������е��û�������

			QQPack stopserverPack = new QQPack();
			stopserverPack.setType(QQPackType.STOP_SERVER);// ���ð�����Ϊֹͣ�����
			QQUtils.sendToAll(stopserverPack);// ��ֹͣ�����

			ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
			ServerFrame.getServerFrame().getServerPanel()
					.updateOnlineUserData();// ���·������������û���

			serverPanel.getImageLabel().setIcon(QQUtils.serverstop);
			serverPanel.getStartSeverBtn().setEnabled(true);
			serverPanel.getStopServerBtn().setEnabled(false);
			serverPanel.getCoerceDownBtn().setEnabled(false);
			serverPanel.getSendBulletinBtn().setEnabled(false);

			LogDaoImpl logDao = new LogDaoImpl();
			String str = "[" + QQUtils.formatTime() + "]: �ر��˷�����!\n";
			serverPanel.getServerLogArea().append(str);

			// �����ݿ�����д��־
			logDao.addLog(QQUtils.formatDate(), str);

			try {
				QQUtils.closeThread(); // �ر������̡߳�
				serverSocket.close();// �ر�ServerSocket��

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * ǿ�����߷���
	 */
	public void CoerceDown() {// ǿ�����ߵķ���

		int flag = 0;
		// ��ȡǿ�������û���ID
		String id = null;
		try {
			id = serverPanel.getUserTable().getValueAt(
					serverPanel.getUserTable().getSelectedRow(), 0).toString();
		} catch (Exception e) {
			flag = 1;
			JOptionPane.showMessageDialog(serverPanel, "��ûѡ���û���û�������û���",
					"ǿ��������ʾ", 1);
		}

		if (flag == 0) {
			int i = JOptionPane.showConfirmDialog(serverPanel, "�Ƿ�ǿ���û�[" + id
					+ "]����?", "ǿ��������ʾ", JOptionPane.YES_NO_OPTION);

			if (i == 0) {

				userDao.updateOnline(id, false);

				// userPanel = ServerFrame.getFrames().getUserPanel();//
				// ��ȡ�û��������
				userPanel = ServerFrame.getServerFrame().getUserPanel();// ��ȡ�û��������
				userPanel.updateUserData();// �����û���������û���
				// serverPanel = ServerMain.serverMain.getServerPanel(); //
				// ��ȡ����������
				serverPanel.updateOnlineUserData();// ���·��������������û���

				// ��ǿ�����߰�
				QQPack downOlinePack = new QQPack();
				downOlinePack.setType(QQPackType.FORCE_DOWNLINE);// ���ð�����Ϊǿ������
				// downOlinePack.set
				QQUtils.sendTo(downOlinePack, id);// �����߰�����Ҫǿ�����ߵ��Ǹ��û�
				QQUtils.threadMap.remove(id); // �����ߵ��û����߳�map�����Ƴ���

				// �������������û�ĳ�������ˣ������߰�
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(id);
				qqPack.setContent("�û�[" + id + "] ������!");
				QQUtils.sendToAll(qqPack);

				String str = "[" + QQUtils.formatTime() + "]:�û�[id]��ǿ��������!\n";
				serverPanel.getServerLogArea().append(str);
				serverPanel.getServerLogArea().setCaretPosition(
						serverPanel.getServerLogArea().getDocument()
								.getLength());
				// �����ݿ�����д��־
				logDao.addLog(QQUtils.formatDate(), str);

			}
		}

	}
}
