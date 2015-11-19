package lzx.qqserver.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import lzx.qqserver.configManger.ConfigPanel;
import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.dao.UserDao;
import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.logManger.LogPanel;
import lzx.qqserver.serverManger.ServerPanel;
import lzx.qqserver.tools.QQUtils;
import lzx.qqserver.userManger.UserPanel;

import pub.QQPack;
import pub.QQPackType;


/**
 * ����˹������
 * 
 * @author luozhixiao
 * 
 */
public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 4178925623043643001L;

	/** ����ѡ� */
	private JTabbedPane jtab;

	/** ������� */
	private ServerPanel serverPanel;

	/** �û����� */
	private UserPanel userPanel;

	/** ��־���� */
	private LogPanel logPanel;

	/** ���ù��� */
	private ConfigPanel configPanel;

	/** ����˹������ */
	private static ServerFrame serverFrame;

	/** �û������ݴ洢���� */
	private UserDao userDao;

	/**
	 * ���췽��
	 */
	public ServerFrame() {

		jtab = new JTabbedPane();
		serverFrame = this;
		serverPanel = new ServerPanel();// �������
		userPanel = new UserPanel();// �û�����
		logPanel = new LogPanel();// ��־����
		configPanel = new ConfigPanel();// ���ù���

		jtab.add("�������", serverPanel);
		jtab.add("�û�����", userPanel);
		jtab.add("��־����", logPanel);
		jtab.add("���ù���", configPanel);

		this.add(jtab);

		this.setTitle("��ҵQQ��������");
		this.setResizable(false);
		this.setSize(650, 500);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				int i = JOptionPane.showConfirmDialog(serverFrame,
						"�Ƿ�����˳�������������", "������ʾ", JOptionPane.YES_NO_OPTION);

				if (i == 0) {

					userDao = new UserDaoImpl();
					userDao.setAllOffline();// �������е��û�������

					QQPack stopserverPack = new QQPack();
					stopserverPack.setType(QQPackType.STOP_SERVER);// ���ð�����Ϊֹͣ�����
					QQUtils.sendToAll(stopserverPack);// ��ֹͣ�����

					LogDaoImpl logDao = new LogDaoImpl();
					String str = "[" + QQUtils.formatTime() + "]: �ر��˷�����!\n";
					serverPanel.getServerLogArea().append(str);

					// �����ݿ�����д��־
					logDao.addLog(QQUtils.formatDate(), str);
					
					QQUtils.closeThread(); // �ر������̡߳�
					System.exit(0);// �˳�����
				}
			}

		});

		this.setVisible(true);

	}

	/**
	 * ��ȡ����˹������
	 * 
	 * @return ServerFrame ����˹������
	 */
	public static ServerFrame getServerFrame() {
		return serverFrame;
	}

	/**
	 * ��ȡ�û��������
	 * 
	 * @return UserPanel �û��������
	 */
	public UserPanel getUserPanel() {
		return userPanel;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return ServerPanel ����������
	 */
	public ServerPanel getServerPanel() {
		return serverPanel;
	}
		
}
