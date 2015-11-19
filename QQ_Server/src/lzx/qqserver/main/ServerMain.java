package lzx.qqserver.main;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.tools.ConfigFile;
import lzx.qqserver.tools.QQUtils;



/**
 * ����������
 * 
 *@author luozhixiao
 * 
 */
public class ServerMain extends JFrame {

	private static final long serialVersionUID = 2882817269100993556L;

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

	/** ��������� */
	private JButton intoButton;

	/** ���������ü��� */
	private ServerMainListen serListen;

	/**
	 * ���졣
	 */
	public ServerMain() {

		this.setLayout(new BorderLayout());

		initcenterPanel();
		initButtonPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		readConfig();// �������ļ����������

		this.setTitle("��������������");
		this.setResizable(false);
		this.setSize(460, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * ��������������
	 * 
	 * @param args
	 *            �ַ�������
	 */
	public static void main(String[] args) {
		QQUtils.setGlobalFont("����");
		ServerMain servermain = new ServerMain();

		if (servermain.serListen.testConnnection() == 1) {
			
			if(servermain.serListen.testPort(servermain.serListen.getPort())==1){
				// ��¼�ɹ�
				servermain.dispose();
				ServerFrame serverFrame = new ServerFrame();

				String str = "[" + QQUtils.formatTime() + "]: �����˷�����!\n";
				serverFrame.getServerPanel().getServerLogArea().append(str);
				// �����ݿ�����д��־
				LogDaoImpl logDao = new LogDaoImpl();
				logDao.addLog(QQUtils.formatDate(), str);
			}else{
				servermain.setVisible(true);
				JOptionPane.showMessageDialog(servermain, "�˿ڱ�ռ�ã�","������ʾ", 1);
			}
		} else {
			servermain.setVisible(true);
			JOptionPane.showMessageDialog(servermain, "�������ݿ�ʧ�ܣ�����������SQL���ݿ����ã�","������ʾ", 1);
		}
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
	 * �м���巽��
	 */
	private void initcenterPanel() {

		centerPanel = new JPanel(new BorderLayout());

		// ����һ���������淽���������
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(10));
		// �洢��ʽ
		JPanel savePanel = new JPanel();
		savePanel.setBorder(BorderFactory.createTitledBorder("���ݴ洢����"));
		savePanel.add(new JLabel("�洢��ʽ: "));
		JComboBox saveComboBox = new JComboBox();
		saveComboBox.addItem("SQL");
		saveComboBox.addItem("XML");
		saveComboBox.setEnabled(false); // �洢��ʽ����������Ϊ���ɸ���.
		savePanel.add(saveComboBox);
		box.add(savePanel);
		box.add(Box.createVerticalStrut(10));

		Box urlBox = Box.createVerticalBox(); //�������url/user/password �ĺ���
		urlBox.setBorder(BorderFactory.createTitledBorder("SQL���ݿ�����")); // Ϊ�������ñ���

		// ����
		JPanel drivePanel = new JPanel();
		drivePanel.add(new JLabel("��        ��: "));
		driveField = new JTextField(40);
		drivePanel.add(driveField);
		urlBox.add(drivePanel);

		// ����url
		JPanel urlPanel = new JPanel();
		urlPanel.add(new JLabel("��   ��  URL: "));
		urlField = new JTextField(40);
		urlPanel.add(urlField);
		urlBox.add(urlPanel); // ��URL�ӵ�urlBox������

		// �û��� / �� ��
		JPanel userPswPanel = new JPanel();
		userPswPanel.add(new JLabel("��   ��   ��: "));
		userField = new JTextField(14);
		userPswPanel.add(userField);

		userPswPanel.add(new JLabel(" ��   ��: "));
		passwordField = new JPasswordField(14);
		userPswPanel.add(passwordField);
		urlBox.add(userPswPanel); // ���û�����ӵ�urlBox������

		box.add(urlBox); // �ٰ�urlBox�ӵ�����Box��
		box.add(Box.createVerticalStrut(10));

		Box fileBox = Box.createVerticalBox(); // ��������ļ��Ͷ˿�
		fileBox.setBorder(BorderFactory.createTitledBorder("��������������")); // Ϊ�������ñ���
		fileBox.add(Box.createVerticalStrut(3));

		// �� ���˿ں�
		JPanel filePanel = new JPanel();
		filePanel.add(new JLabel("��        ��: "));
		fileField = new JTextField(14);
		fileField.setEditable(false);
		filePanel.add(fileField);

		filePanel.add(new JLabel(" �� �� ��:"));
		portField = new JTextField(14);

		filePanel.add(portField);
		fileBox.add(filePanel);

		box.add(fileBox);
		box.add(Box.createVerticalStrut(10));

		centerPanel.add(box, BorderLayout.CENTER);
	}

	/**
	 * ��ť��巽��
	 */
	private void initButtonPanel() {// ��ť���
		buttonPanel = new JPanel();

		serListen = new ServerMainListen(this);

		connButtonn = new JButton("��������");
		connButtonn.addActionListener(serListen);

		portButton = new JButton("���Զ˿�");
		portButton.addActionListener(serListen);

		saveConfButton = new JButton("��������");
		saveConfButton.addActionListener(serListen);
		intoButton = new JButton("���������");
		intoButton.addActionListener(serListen);

		buttonPanel.add(connButtonn);
		buttonPanel.add(portButton);
		buttonPanel.add(saveConfButton);
		buttonPanel.add(intoButton);
	}

	/**
	 * ��ȡ�����������ť��ֵ��
	 * 
	 * @return JButton �����������ť��ֵ��
	 */
	public JButton getIntoButton() {
		return intoButton;
	}

	/**
	 * ��ȡ�������Ӱ�ť��ֵ��
	 * 
	 * @return JButton �������Ӱ�ť��ֵ��
	 */
	public JButton getConnButtonn() {
		return connButtonn;
	}

	/**
	 * ��ȡ�˿��ı����ֵ��
	 * 
	 * @return JTextField �˿��ı����ֵ��
	 */
	public JTextField getPortField() {
		return portField;
	}

	/**
	 * ��ȡ���Զ˿ڰ�ť��ֵ��
	 * 
	 * @return JButton ���Զ˿ڰ�ť��ֵ��
	 */
	public JButton getPortButton() {
		return portButton;
	}

	/**
	 * ��ȡ�������ð�ť��ֵ��
	 * 
	 * @return JButton �������ð�ť��ֵ��
	 */
	public JButton getSaveConfButton() {
		return saveConfButton;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * 
	 * @return JTextField �����ı����ֵ��
	 */
	public JTextField getDriveField() {
		return driveField;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * 
	 * @return JTextField �����ı����ֵ��
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}

	/**
	 * ��ȡURL�ı����ֵ��
	 * 
	 * @return JTextField URL�ı����ֵ��
	 */
	public JTextField getUrlField() {
		return urlField;
	}

	/**
	 * ��ȡ�û��ı����ֵ��
	 * 
	 * @return JTextField �û��ı����ֵ��
	 */
	public JTextField getUserField() {
		return userField;
	}

}
