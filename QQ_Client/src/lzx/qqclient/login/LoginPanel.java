package lzx.qqclient.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lzx.qqclient.tools.QQUtils;



public class LoginPanel extends JPanel {

	
	private static final long serialVersionUID = -4866516784006055784L;

	/** �ϲ���� */
	private JLabel topLabel;
    
	/** �в���� */
	private JPanel centerPanel;

	/** �²���� */
	private JPanel bottomPanel;

	/** �û��ʺ� */
	private JComboBox userComboBox;

	/** �û����� */
	private JPasswordField passwordField;

	/** ���� */
	private JButton confButton;

	/** ��¼ */
	private JButton loginButton;
    
	/**ע��**/
	private JButton registerButton;
	/** ip */
	private JComboBox ipComboBox;

	/** �˿� */
	private JTextField portField;

	/** ��ס���� */
	private JCheckBox rememberPassword;

	/** ��������Ƿ����� */
	private boolean hide = true;

	/** ��¼�� */
	private LoginMain frame;

	/**
	 * ���췽��
	 */
	public LoginPanel(final LoginMain frame) {
		this.frame = frame;
		initUserPasswordPanel();
		initBottomPanel();
	}

	/**
	 * �û����������
	 */
	public void initUserPasswordPanel() {

		this.setLayout(new BorderLayout());
		this.setBackground(new Color(200, 240, 255));// ���ñ���ɫ

		topLabel = new JLabel(QQUtils.qqbanner);// ����ͼƬ
		this.add(topLabel, BorderLayout.NORTH);

		centerPanel = new JPanel(new BorderLayout());// �в�
		centerPanel.setBackground(new Color(200, 240, 255));// ���ñ���ɫ
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel centerboxPanel = new JPanel(new BorderLayout());
		centerboxPanel.setBackground(new Color(235, 250, 255));// ���ñ���ɫ
        
		Box centerBox = Box.createVerticalBox();// ���ŷŵĺ���

		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(235, 250, 255));// ���ñ���ɫ

		Vector<String> ve = new Vector<String>();
		File file=new File("./log");//��ȡ�����ʺ� 
		for(File fileName: file.listFiles())
		ve.add(fileName.getName());
		userComboBox = new JComboBox(ve);//���ʺ���ʾ
		userComboBox.setPreferredSize(new Dimension(168, 20));// ����������(wieth,height)
		userComboBox.setEditable(true);
		userPanel.add(new JLabel("�� �� "));
		userPanel.add(userComboBox);
 
		JPanel passwordPanel = new JPanel();
		passwordPanel.setBackground(new Color(235, 250, 255));// ���ñ���ɫ
		passwordField = new JPasswordField("123456");
		passwordField.setPreferredSize(new Dimension(168, 20));
		passwordPanel.add(new JLabel("�� �� "));
		passwordPanel.add(passwordField);
        
		
		rememberPassword = new JCheckBox("��¼����");
		rememberPassword.setBackground(new Color(235, 250, 255));// ���ñ���ɫ
        
		// ���ð�ť
		confButton = new JButton(QQUtils.normal);
		confButton.setText("���á�");
		//confButton.setSize(50,10);
		confButton.setHorizontalTextPosition(JButton.CENTER);// �����ı���ˮƽ������ͼƬ���м�
		confButton.setBorder(BorderFactory.createEmptyBorder());// ����button�߿�Ϊ�ձ߿�
		confButton.setRolloverIcon(QQUtils.over);// ��껬��ʱ��ͼƬ

		confButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (hide == true) {
					bottomPanel.setVisible(true);
					frame.setSize(330, 355);
					hide = false;
					confButton.setText("���á�");
				} else if (hide == false) {
					bottomPanel.setVisible(false);
					confButton.setText("���á�");
					frame.setSize(330, 250);
					hide = true;
				}

			}

		});

		
		centerBox.add(Box.createVerticalStrut(10));
		centerBox.add(userPanel);
		centerBox.add(Box.createVerticalStrut(5));
		centerBox.add(passwordPanel);
		centerBox.add(Box.createVerticalStrut(5));
		
		JPanel confPanel=new JPanel();//������úͼ�¼�����Panel
		confPanel.setBackground(new Color(235, 250, 255));
		confPanel.add(confButton);
		confPanel.add(rememberPassword);
		centerBox.add(confPanel);//��ӵ�������
		
		centerBox.setBorder(BorderFactory.createLineBorder(new Color(100, 180,
				255)));
		centerboxPanel.add(centerBox,BorderLayout.CENTER);
		centerPanel.add(centerboxPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(200, 240, 255));// ���ñ���ɫ

		
		LoginListen loginListen = new LoginListen(this);
		// ��¼��ť
		loginButton = new JButton("��  ¼");
		loginButton.addActionListener(loginListen);
		loginButton.setIcon(QQUtils.normal);
		loginButton.setHorizontalTextPosition(JButton.CENTER);
		loginButton.setBorder(null);
		loginButton.setRolloverIcon(QQUtils.over);
        
		registerButton=new JButton("ע  ��");
		registerButton.setIcon(QQUtils.normal);
		registerButton.setHorizontalTextPosition(JButton.CENTER);
		registerButton.setBorder(null);
		registerButton.setRolloverIcon(QQUtils.over);

		buttonPanel.add(registerButton);
		buttonPanel.add(loginButton);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.add(centerPanel, BorderLayout.CENTER);

	}

	/**
	 * ip port ����
	 * 
	 */
	public void initBottomPanel() {

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(200, 240, 255));// ���ñ�����ɫ
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		bottomPanel.setVisible(false);

		JPanel ipportPanel = new JPanel();
		ipportPanel.setBackground(new Color(200, 240, 255));// ���ñ�����ɫ
		ipportPanel.setBorder(BorderFactory.createTitledBorder("��������"));

		Box bottomBox = Box.createVerticalBox();// ���ŷŵĺ���

		JPanel ipPanel = new JPanel();
		ipPanel.setBackground(new Color(200, 240, 255));// ���ñ�����ɫ
		Vector<String> vec = new Vector<String>();
		vec.add("127.0.0.1");

		ipComboBox = new JComboBox(vec);
		ipComboBox.setEditable(true);// ����Ϊ�ɱ༭
		ipComboBox.setPreferredSize(new Dimension(168, 20));// ����������(wieth,height)
		ipPanel.add(new JLabel("IP �� ַ:"));
		ipPanel.add(ipComboBox);

		JPanel portPanel = new JPanel();
		portPanel.setBackground(new Color(200, 240, 255));// ���ñ�����ɫ
		portField = new JTextField("8888");
		portField.setPreferredSize(new Dimension(168, 20));// ����������(wieth,height)
		portPanel.add(new JLabel("�� �� ��:"));
		portPanel.add(portField);

		bottomBox.add(ipPanel);
		bottomBox.add(Box.createVerticalStrut(10));
		bottomBox.add(portPanel);

		ipportPanel.add(bottomBox, BorderLayout.CENTER);

		bottomPanel.add(ipportPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 * ��ȡ��¼��ť��ֵ��
	 * @return JButton ��¼��ť��ֵ��
	 */
	public JButton getLoginButton() {
		return loginButton;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JPasswordField �����ı����ֵ��
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}

	/**
	 * ��ȡ�û����������ֵ��
	 * @return JComboBox �û����������ֵ��
	 */
	public JComboBox getUserComboBox() {
		return userComboBox;
	}

	/**
	 * ��ȡIP�������ֵ��
	 * @return JComboBox IP�������ֵ��
	 */
	public JComboBox getIpComboBox() {
		return ipComboBox;
	}

	/**
	 * ��ȡ�˿��ı����ֵ��
	 * @return JTextField �˿��ı����ֵ��
	 */
	public JTextField getPortField() {
		return portField;
	}

	/**
	 * ��ȡ��¼��LoginMain��ֵ��
	 * @return LoginMain ��¼��LoginMain��ֵ��
	 */
	public LoginMain getFrame() {
		return frame;
	}


}
