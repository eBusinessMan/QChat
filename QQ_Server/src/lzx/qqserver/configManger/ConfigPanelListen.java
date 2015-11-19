package lzx.qqserver.configManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import lzx.qqserver.tools.ConfigFile;



/**
 * ���ù��������
 * @author luozhixiao
 *
 */
public class ConfigPanelListen implements ActionListener {
	
	/** ���ù���Panel */
	private ConfigPanel confPanel;
	
	/**�˿� */
	private String port;

	/**
	 * ���췽��
	 * @param confPanel
	 */
	public ConfigPanelListen(final ConfigPanel confPanel) {
		this.confPanel = confPanel;

	}
	
	/**
	 * �����¼�����
	 */
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == confPanel.getConnButtonn()) {// ��������

			int i = testConnnection();
			if (i == 1) {
				JOptionPane.showMessageDialog(confPanel, "�������ݿ�ɹ�!", "������ʾ", 1);
			} else if (i == -1) {
				JOptionPane.showMessageDialog(confPanel, "�������ݿ�ʧ��, �������ݿ�����!",
						"������ʾ", 1);
			}

		} else if (e.getSource() == confPanel.getPortButton()) {// ���Զ˿�

			int i = testPort(port);
			if (i == 1) {
				JOptionPane.showMessageDialog(confPanel, "�˿� " + this.getPort()
						+ " ����!", "�˿���ʾ", 1);
			} else if (i == -1) {
				JOptionPane.showMessageDialog(confPanel, "�˿� " + this.getPort()
						+ " �ѱ�ռ��!", "�˿���ʾ", 1);
			}

		} else if (e.getSource() == confPanel.getSaveConfButton()) {// ��������

			int i = saveConfig();
			if (i == 1) {
				JOptionPane.showMessageDialog(confPanel, "�������óɹ�!", "������ʾ", 1);
			} else if (i == -1) {
				JOptionPane.showMessageDialog(confPanel, "��������ʧ��!", "������ʾ", 1);
			}

		} else if (e.getSource() == confPanel.getDefaultButton()) {// �ָ�Ĭ������

			int i = JOptionPane.showConfirmDialog(confPanel, "���Ҫ�ָ�Ĭ��������?","��ԭĬ������",JOptionPane.YES_NO_OPTION);
			if(i==0)
			{
				defaultConfig();//�ָ�Ĭ�Ϸ���
			}
			
		}
	}

	/**
	 * �������ݿ����ӵķ���
	 * @return int 1�������ݿ�ɹ���-1�������ݿ�ʧ��
	 */
	public int testConnnection() {
		int i = 0;

		String drive = confPanel.getDriveField().getText().trim(); // ����
		String url = confPanel.getUrlField().getText().trim(); // url
		String username = confPanel.getUserField().getText().trim(); // �û���
		String password = new String(confPanel.getPasswordField().getPassword())
				.trim(); // ����

		if (judgeStr(drive, url, username, password)) {// �ж� ������URL���û����������Ƿ�Ϊ��

			Connection conn = null;
			try {
				// 1. ע��ͼ������ݿ���������
				Class.forName(drive);

				// 2. ���������ݿ������ͨ��
				conn = DriverManager.getConnection(url, username, password);

			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}

			if (conn == null) {
				i = -1;

			} else {

				i = 1;
			}
		}
		return i;

	}

	/**
	 * ���Զ˿��Ƿ���õķ���
	 * @return int 1�˿ڿ��ã�-1�˿ڱ�ռ��
	 */
	public int testPort(String port) {

		int i = 0;
		port = confPanel.getPortField().getText().trim();// �˿�
		this.setPort(port);
		if (judgePort(port)) {// ��֤�˿�

			ServerSocket socket = null; // ����sockct
			try {
				socket = new ServerSocket(Integer.parseInt(port));
				socket.close();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (socket == null) {
				i = -1;

			} else {

				i = 1;
			}

		}
		return i;
	}

	/**
	 * �������õķ���
	 * @return int 1�������óɹ���-1��������ʧ��
	 */
	public int saveConfig() {
		int i = 0;
		String drive = confPanel.getDriveField().getText().trim(); // ����
		String url = confPanel.getUrlField().getText().trim(); // url
		String username = confPanel.getUserField().getText().trim(); // �û���
		String password = new String(confPanel.getPasswordField().getPassword())
				.trim(); // ����
		String port = confPanel.getPortField().getText().trim();// �˿�

		if (judgeStr(drive, url, username, password)) {// �ж� ������URL���û����������Ƿ�Ϊ��
			if (judgePort(port)) {// ��֤�˿�

				ConfigFile confile = new ConfigFile("./config/config.ini");
				confile.setKeyValue("drive", drive);
				confile.setKeyValue("url", url);
				confile.setKeyValue("user", username);
				confile.setKeyValue("password", password);
				confile.setKeyValue("port", port);

				if (confile.save()) {
					i = 1;

				} else {
					i = -1;

				}
			}
		}
		return i;

	}
	
	/**
	 * �ָ�Ĭ�����õķ���
	 */
	public void defaultConfig(){
		confPanel.getDriveField().setText("com.microsoft.jdbc.sqlserver.SQLServerDriver"); // ����
		confPanel.getUrlField().setText("jdbc:microsoft:sqlserver://localhost:1433;databasename=EnPQQ"); // url
		confPanel.getUserField().setText("sa"); // �û���
		confPanel.getPasswordField().setText(" "); // ����
		confPanel.getPortField().setText("8888");// �˿�
		
	}

	/**
	 * �ж� ������URL���û����������Ƿ�Ϊ��
	 * @param drive ����
	 * @param url URL
	 * @param username �û���
	 * @param password ����
	 * @return boolean true ������URL���û���������淶��false������URL���û��������벻�淶
	 */
	public boolean judgeStr(String drive, String url, String username,
			String password) {
		boolean bo = false;

		if (drive.length() == 0 || drive == null) {
			JOptionPane.showMessageDialog(confPanel, "���ݿ���������Ϊ��, ����������!",
					"������ʾ", 1);

		} else if (url.length() == 0 || url == null) {
			JOptionPane.showMessageDialog(confPanel, "����URL����Ϊ��, ������URL!",
					"������ʾ", 1);

		} else if (username.length() == 0 || username == null) {
			JOptionPane.showMessageDialog(confPanel, "�û�������Ϊ��, �������û���!", "������ʾ",
					1);

		} else if (password.length() == 0 || password == null) {
			JOptionPane.showMessageDialog(confPanel, "���벻��Ϊ��, ����������!", "������ʾ",
					1);

		} else {
			bo = true;
		}
		return bo;
	}

	/**
	 * ��֤�˿��Ƿ���Ϲ淶�ķ���
	 * @param port �˿�
	 * @return boolean true�˿ڷ��Ϲ淶��false�˿ڲ����Ϲ淶
	 */
	public boolean judgePort(String port) {// ��֤�˿�
		boolean bo = false;
		if (port.length() == 0 || port == null) {// ��֤�˿��Ƿ�Ϊ��

			JOptionPane.showMessageDialog(confPanel, "�˿ڲ���Ϊ��!", "�˿���ʾ", 1);

		} else if (!Pattern.matches("[0-9]+$", port)) {// �ж϶˿��Ƿ�������

			JOptionPane.showMessageDialog(confPanel, "�˿ڱ���������!", "�˿���ʾ", 1);

		} else if (Integer.parseInt(port) < 1024
				|| Integer.parseInt(port) > 65535) {// �ж϶˿��Ƿ���1024-65535
			// ֮��

			JOptionPane.showMessageDialog(confPanel, "�˿ڱ�����1024-65535֮��!",
					"�˿���ʾ", 1);

		} else {
			bo = true;
		}
		return bo;
	}

	/**
	 * ��ȡport��ֵ��
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
