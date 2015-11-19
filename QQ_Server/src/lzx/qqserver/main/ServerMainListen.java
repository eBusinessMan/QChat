package lzx.qqserver.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.tools.ConfigFile;
import lzx.qqserver.tools.QQUtils;



/**
 * �������ü�����
 * 
 * @author luozhixiao
 * 
 */
public class ServerMainListen implements ActionListener {

	/** �������� */
	private ServerMain frame;

	/** �˿� */
	private String port;

	/**
	 * ���췽��
	 */
	public ServerMainListen(final ServerMain frame) {
		this.frame = frame;

	}

	/**
	 * �����¼�����
	 */

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == frame.getConnButtonn()) {// ��������

			int i = testConnnection();
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "�������ݿ�ɹ�!", "������ʾ", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame,
						"�������ݿ�ʧ��, ����SQL���ݿ�����!", "������ʾ", 1);
			}

		} else if (e.getSource() == frame.getPortButton()) {// ���Զ˿�

			int i = testPort(port);
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "�˿� " + this.getPort()
						+ " ����!", "�˿���ʾ", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame, "�˿� " + this.getPort()
						+ " �ѱ�ռ��!", "�˿���ʾ", 1);
			}

		} else if (e.getSource() == frame.getSaveConfButton()) {// ��������

			int i = saveConfig();
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "�������óɹ�!", "������ʾ", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame, "��������ʧ��!", "������ʾ", 1);
			}

		} else if (e.getSource() == frame.getIntoButton()) {// ���������

			int i = intoServer();
			if (i==1) {
				// ��¼�ɹ�
				this.frame.dispose();
				ServerFrame serverFrame = new ServerFrame();
				
				String str = "[" + QQUtils.formatTime() + "]: �����˷�����!\n";
				serverFrame.getServerPanel().getServerLogArea().append(str);

				LogDaoImpl logDao = new LogDaoImpl();
				// �����ݿ�����д��־	
				logDao.addLog(QQUtils.formatDate(), str);

			}
		}

	}

	/**
	 * �������ݿ����ӵķ���
	 * 
	 * @return int 1�������ݿ�ɹ���-1�������ݿ�ʧ��
	 */
	public int testConnnection() {

		int i = 0;
		String drive = frame.getDriveField().getText().trim(); // ����
		String url = frame.getUrlField().getText().trim(); // url
		String username = frame.getUserField().getText().trim(); // �û���
		String password = new String(frame.getPasswordField().getPassword())
				.trim(); // ����

		if (judgeStr(drive, url, username, password)) {// �ж�
														// ������URL���û����������Ƿ���Ϲ淶
			Connection conn = null;
			try {
				// 1. ע��ͼ������ݿ���������
				Class.forName(drive);

				// 2. ���������ݿ������ͨ��
				conn = DriverManager.getConnection(url, username, password);

			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("SQL������û�򿪻����ݿ����ô���");
				//e.printStackTrace();
			}

			if (conn == null) {

				i=-1;

			}else{
				i=1;
			}
		}
		return i;

	}

	/**
	 * ���Զ˿��Ƿ���õķ���
	 * 
	 * @return int 1�˿ڿ��ã�-1�˿ڱ�ռ��
	 */
	public int testPort(String port) {

		int i = 0;
		port = frame.getPortField().getText().trim();// �˿�
		this.setPort(port);

		if (judgePort(port)) {// ��֤�˿�

			ServerSocket socket = null; // ����sockct
			try {
				socket = new ServerSocket(Integer.parseInt(port));
				socket.close();
			} catch (Exception e) {
				System.out.println("�˿ڱ�ռ��");
				//e.printStackTrace();
			}
			if (socket == null) {
				i=-1;
			}else{
				i=1;
			}

		}
		
		return i;
	}

	/**
	 * �������õķ���
	 * 
	 * @return int 1�������óɹ���-1��������ʧ��
	 */
	public int saveConfig() {

		int i = 0;
		String drive = frame.getDriveField().getText().trim(); // ����
		String url = frame.getUrlField().getText().trim(); // url
		String username = frame.getUserField().getText().trim(); // �û���
		String password = new String(frame.getPasswordField().getPassword())
				.trim(); // ����
		String port = frame.getPortField().getText().trim();// �˿�

		if (judgeStr(drive, url, username, password)) {// �ж� ������URL���û����������Ƿ�Ϊ��
			if (judgePort(port)) {// ��֤�˿�

				ConfigFile confile = new ConfigFile("./config/config.ini");
				confile.setKeyValue("drive", drive);
				confile.setKeyValue("url", url);
				confile.setKeyValue("user", username);
				confile.setKeyValue("password", password);
				confile.setKeyValue("port", port);

				if (confile.save()) {
					return 1;
				} else {
					return -1;
				}
			}
		}
		return i;

	}

	/**
	 * ����������ķ���
	 * 
	 * @return int 1�ɹ������������-1ʧ��
	 */
	public int intoServer() {

		int i = 0;
		if (testConnnection()==1) {
			if (testPort(port)==1) {
				i=1;
			}
		} 
		return i;
	}

	/**
	 * �ж� ������URL���û����������Ƿ�Ϊ��
	 * 
	 * @param drive
	 *            ����
	 * @param url
	 *            URL
	 * @param username
	 *            �û���
	 * @param password
	 *            ����
	 * @return boolean true ������URL���û���������淶��false������URL���û��������벻�淶
	 */
	public boolean judgeStr(String drive, String url, String username,
			String password) {

		if (drive.length() == 0 || drive == null) {

			JOptionPane
					.showMessageDialog(frame, "���ݿ���������Ϊ��, ����������!", "������ʾ", 1);
			return false;

		} else if (url.length() == 0 || url == null) {

			JOptionPane.showMessageDialog(frame, "����URL����Ϊ��, ������URL!", "������ʾ",
					1);
			return false;

		} else if (username.length() == 0 || username == null) {

			JOptionPane.showMessageDialog(frame, "�û�������Ϊ��, �������û���!", "������ʾ", 1);
			return false;

		}
		return true;
	}

	/**
	 * ��֤�˿��Ƿ���Ϲ淶�ķ���
	 * 
	 * @param port
	 *            �˿�
	 * @return boolean true�˿ڷ��Ϲ淶��false�˿ڲ����Ϲ淶
	 */
	public boolean judgePort(String port) {// ��֤�˿�

		if (port.length() == 0 || port == null) {// ��֤�˿��Ƿ�Ϊ��

			JOptionPane.showMessageDialog(frame, "�˿ڲ���Ϊ��!", "�˿���ʾ", 1);
			return false;

		} else if (!Pattern.matches("[0-9]+$", port)) {// �ж϶˿��Ƿ�������

			JOptionPane.showMessageDialog(frame, "�˿ڱ���������!", "�˿���ʾ", 1);
			return false;

		} else if (Integer.parseInt(port) < 1024
				|| Integer.parseInt(port) > 65535) {// �ж϶˿��Ƿ���1024-65535 ֮��

			JOptionPane.showMessageDialog(frame, "�˿ڱ�����1024-65535֮��!", "�˿���ʾ",
					1);
			return false;
		}
		return true;
	}

	/**
	 * ��ȡport��ֵ��
	 * 
	 * @return JTextField port��ֵ��
	 */
	public String getPort() {
		return port;
	}

	/**
	 * ����port��ֵ
	 */
	public void setPort(String port) {
		this.port = port;
	}
}
