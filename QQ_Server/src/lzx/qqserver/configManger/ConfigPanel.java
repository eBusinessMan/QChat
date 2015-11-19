package lzx.qqserver.configManger;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lzx.qqserver.tools.ConfigFile;



/**
 * ���������
 * @author luozhixiao
 *
 */
public class ConfigPanel extends JPanel {

	private static final long serialVersionUID = 2882817269100993556L;

	/** ���ݴ洢������� */
	private JPanel savePanel;

	/** ������� */
	private JPanel centerPanel;

	/** ��ť��� */
	private JPanel buttonPanel;

	/** ���ݿ����� */
	private JTextField driveField;

	/** ����url */
	private JTextField urlField;

	/** �û��� */
	private JTextField userField;

	/** ���� */
	private JPasswordField passwordField;

	/** �ļ� */
	private JTextField fileField;

	/** �˿ڡ� */
	private JTextField portField;

	/** �������� */
	private JButton connButtonn;

	/** ���Զ˿� */
	private JButton portButton;

	/** �������� */
	private JButton saveConfButton;

	/** �ָ�Ĭ������*/
	private JButton defaultButton;

	/**
	 * ���췽��
	 */
	public ConfigPanel() {

		this.setLayout(new BorderLayout());

		initTopPanel();
		initCenterPanel();
		initButtonPanel();
		this.add(savePanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		readConfig();// �������ļ����������
		this.setVisible(true);

	}
	/** 
	 * �������ļ���������÷���
	 *
	 */
	public void readConfig() {
		ConfigFile confile = new ConfigFile("./config/config.ini");
		driveField.setText(confile.getKeyValue("drive"));
		urlField.setText(confile.getKeyValue("url"));
		userField.setText(confile.getKeyValue("user"));
		passwordField.setText(confile.getKeyValue("password"));
		portField.setText(confile.getKeyValue("port"));

	}

	/** 
	 *�洢��ʽ
	 */
	private void initTopPanel() {

		
		savePanel = new JPanel();
		savePanel.setBorder(BorderFactory.createTitledBorder("���ݴ洢����"));
		savePanel.add(new JLabel("�洢��ʽ: "));
		JComboBox saveComboBox = new JComboBox();
		saveComboBox.addItem("SQL");
		saveComboBox.addItem("XML");
		saveComboBox.setEnabled(false); // ���������򲻿ɱ༭
		savePanel.add(saveComboBox);
	}

	/**
	 *oracle���ݿ����úͷ�������������
	 *
	 */
	private void initCenterPanel() {

		centerPanel = new JPanel(new BorderLayout());

		// oracle���ݿ�����--------------------------------------
		JPanel oraclePanel = new JPanel();
		oraclePanel.setBorder(BorderFactory.createTitledBorder("SQL���ݿ�����"));
		Box oracleBox = Box.createVerticalBox();
		// ����
		JPanel drivePanel = new JPanel();
		drivePanel.add(new JLabel("��       ��:  "));
		driveField = new JTextField(50);
		drivePanel.add(driveField);

		// ����url
		JPanel urlPanel = new JPanel();
		urlPanel.add(new JLabel("��  ��  URL:  "));
		urlField = new JTextField(50);
		urlPanel.add(urlField);

		// �û��� / �� ��
		JPanel userPanel = new JPanel();
		userPanel.add(new JLabel("��  ��   ��:  "));
		userField = new JTextField(50);
		userPanel.add(userField);

		JPanel passwordPanel = new JPanel();
		passwordPanel.add(new JLabel("��       ��:  "));
		passwordField = new JPasswordField(50);
		passwordPanel.add(passwordField);

		oracleBox.add(Box.createVerticalStrut(10));
		oracleBox.add(drivePanel);
		oracleBox.add(Box.createVerticalStrut(10));
		oracleBox.add(urlPanel);
		oracleBox.add(Box.createVerticalStrut(10));
		oracleBox.add(userPanel);
		oracleBox.add(Box.createVerticalStrut(10));
		oracleBox.add(passwordPanel);
		oraclePanel.add(oracleBox);

		// ��������������-----------------------------------------------
		Box fileBox = Box.createVerticalBox(); // ��������ļ��Ͷ˿�
		fileBox.setBorder(BorderFactory.createTitledBorder("��������������"));

		// �� ���˿ں�
		JPanel filePanel = new JPanel();

		filePanel.add(new JLabel("��      ��:  "));
		fileField = new JTextField(50);
		fileField.setEditable(false);
		filePanel.add(fileField);

		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("��  ��  ��:  "));
		portField = new JTextField(50);
		portPanel.add(portField);

		fileBox.add(Box.createVerticalStrut(20));
		fileBox.add(filePanel);
		fileBox.add(Box.createVerticalStrut(10));
		fileBox.add(portPanel);
		fileBox.add(Box.createVerticalStrut(20));

		centerPanel.add(oraclePanel, BorderLayout.CENTER);
		centerPanel.add(fileBox, BorderLayout.SOUTH);
	}

	/**
	 * ��ť���
	 */
	private void initButtonPanel() {// ��ť���
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder("����ѡ��"));
		ConfigPanelListen confListen = new ConfigPanelListen(this);

		Box btnBox = Box.createHorizontalBox();
		connButtonn = new JButton("��������");
		connButtonn.addActionListener(confListen);

		portButton = new JButton("���Զ˿�");
		portButton.addActionListener(confListen);

		saveConfButton = new JButton("��������");
		saveConfButton.addActionListener(confListen);

		defaultButton = new JButton("�ָ�Ĭ������");
		defaultButton.addActionListener(confListen);

		btnBox.add(connButtonn);
		btnBox.add(Box.createHorizontalStrut(15));
		btnBox.add(portButton);
		btnBox.add(Box.createHorizontalStrut(15));
		btnBox.add(saveConfButton);
		btnBox.add(Box.createHorizontalStrut(15));
		btnBox.add(defaultButton);

		buttonPanel.add(btnBox);
	}

	/**
	 * ��ȡ�������Ӱ�ť��ֵ��
	 * @return JButton �������Ӱ�ť��ֵ��
	 */
	public JButton getConnButtonn() {
		return connButtonn;
	}

	/**
	 * ��ȡ�ָ�Ĭ�����ð�ť��ֵ��
	 * @return JButton �ָ�Ĭ�����ð�ť��ֵ��
	 */
	public JButton getDefaultButton() {
		return defaultButton;
	}
	/**
	 * ��ȡ���Զ˿ڰ�ť��ֵ��
	 * @return JButton ���Զ˿ڰ�ť��ֵ��
	 */
	public JButton getPortButton() {
		return portButton;
	}

	/**
	 * ��ȡ�������ð�ť��ֵ��
	 * @return JButton �������ð�ť��ֵ��
	 */
	public JButton getSaveConfButton() {
		return saveConfButton;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JTextField �����ı����ֵ��
	 */
	public JTextField getDriveField() {
		return driveField;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JTextField �����ı����ֵ��
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	/**
	 * ��ȡ�˿��ı����ֵ��
	 * @return JTextField �˿��ı����ֵ��
	 */
	public JTextField getPortField() {
		return portField;
	}
	/**
	 * ��ȡURL�ı����ֵ��
	 * @return JTextField URL�ı����ֵ��
	 */
	public JTextField getUrlField() {
		return urlField;
	}

	/**
	 * ��ȡ�û��ı����ֵ��
	 * @return JTextField �û��ı����ֵ��
	 */
	public JTextField getUserField() {
		return userField;
	}

}
