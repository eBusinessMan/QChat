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

	/** 上部面板 */
	private JLabel topLabel;
    
	/** 中部面板 */
	private JPanel centerPanel;

	/** 下部面板 */
	private JPanel bottomPanel;

	/** 用户帐号 */
	private JComboBox userComboBox;

	/** 用户密码 */
	private JPasswordField passwordField;

	/** 设置 */
	private JButton confButton;

	/** 登录 */
	private JButton loginButton;
    
	/**注册**/
	private JButton registerButton;
	/** ip */
	private JComboBox ipComboBox;

	/** 端口 */
	private JTextField portField;

	/** 记住密码 */
	private JCheckBox rememberPassword;

	/** 设置面板是否隐藏 */
	private boolean hide = true;

	/** 登录类 */
	private LoginMain frame;

	/**
	 * 构造方法
	 */
	public LoginPanel(final LoginMain frame) {
		this.frame = frame;
		initUserPasswordPanel();
		initBottomPanel();
	}

	/**
	 * 用户名密码面板
	 */
	public void initUserPasswordPanel() {

		this.setLayout(new BorderLayout());
		this.setBackground(new Color(200, 240, 255));// 设置背景色

		topLabel = new JLabel(QQUtils.qqbanner);// 设置图片
		this.add(topLabel, BorderLayout.NORTH);

		centerPanel = new JPanel(new BorderLayout());// 中部
		centerPanel.setBackground(new Color(200, 240, 255));// 设置背景色
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel centerboxPanel = new JPanel(new BorderLayout());
		centerboxPanel.setBackground(new Color(235, 250, 255));// 设置背景色
        
		Box centerBox = Box.createVerticalBox();// 竖着放的盒子

		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(235, 250, 255));// 设置背景色

		Vector<String> ve = new Vector<String>();
		File file=new File("./log");//读取本地帐号 
		for(File fileName: file.listFiles())
		ve.add(fileName.getName());
		userComboBox = new JComboBox(ve);//将帐号显示
		userComboBox.setPreferredSize(new Dimension(168, 20));// 设置下拉框(wieth,height)
		userComboBox.setEditable(true);
		userPanel.add(new JLabel("帐 号 "));
		userPanel.add(userComboBox);
 
		JPanel passwordPanel = new JPanel();
		passwordPanel.setBackground(new Color(235, 250, 255));// 设置背景色
		passwordField = new JPasswordField("123456");
		passwordField.setPreferredSize(new Dimension(168, 20));
		passwordPanel.add(new JLabel("密 码 "));
		passwordPanel.add(passwordField);
        
		
		rememberPassword = new JCheckBox("记录密码");
		rememberPassword.setBackground(new Color(235, 250, 255));// 设置背景色
        
		// 设置按钮
		confButton = new JButton(QQUtils.normal);
		confButton.setText("设置↓");
		//confButton.setSize(50,10);
		confButton.setHorizontalTextPosition(JButton.CENTER);// 设置文本（水平）放在图片的中间
		confButton.setBorder(BorderFactory.createEmptyBorder());// 设置button边框为空边框
		confButton.setRolloverIcon(QQUtils.over);// 鼠标滑入时候图片

		confButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (hide == true) {
					bottomPanel.setVisible(true);
					frame.setSize(330, 355);
					hide = false;
					confButton.setText("设置↑");
				} else if (hide == false) {
					bottomPanel.setVisible(false);
					confButton.setText("设置↓");
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
		
		JPanel confPanel=new JPanel();//存放设置和记录密码的Panel
		confPanel.setBackground(new Color(235, 250, 255));
		confPanel.add(confButton);
		confPanel.add(rememberPassword);
		centerBox.add(confPanel);//添加到盒子里
		
		centerBox.setBorder(BorderFactory.createLineBorder(new Color(100, 180,
				255)));
		centerboxPanel.add(centerBox,BorderLayout.CENTER);
		centerPanel.add(centerboxPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(200, 240, 255));// 设置背景色

		
		LoginListen loginListen = new LoginListen(this);
		// 登录按钮
		loginButton = new JButton("登  录");
		loginButton.addActionListener(loginListen);
		loginButton.setIcon(QQUtils.normal);
		loginButton.setHorizontalTextPosition(JButton.CENTER);
		loginButton.setBorder(null);
		loginButton.setRolloverIcon(QQUtils.over);
        
		registerButton=new JButton("注  册");
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
	 * ip port 方法
	 * 
	 */
	public void initBottomPanel() {

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(200, 240, 255));// 设置背景颜色
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		bottomPanel.setVisible(false);

		JPanel ipportPanel = new JPanel();
		ipportPanel.setBackground(new Color(200, 240, 255));// 设置背景颜色
		ipportPanel.setBorder(BorderFactory.createTitledBorder("网络设置"));

		Box bottomBox = Box.createVerticalBox();// 竖着放的盒子

		JPanel ipPanel = new JPanel();
		ipPanel.setBackground(new Color(200, 240, 255));// 设置背景颜色
		Vector<String> vec = new Vector<String>();
		vec.add("127.0.0.1");

		ipComboBox = new JComboBox(vec);
		ipComboBox.setEditable(true);// 设置为可编辑
		ipComboBox.setPreferredSize(new Dimension(168, 20));// 设置下拉框(wieth,height)
		ipPanel.add(new JLabel("IP 地 址:"));
		ipPanel.add(ipComboBox);

		JPanel portPanel = new JPanel();
		portPanel.setBackground(new Color(200, 240, 255));// 设置背景颜色
		portField = new JTextField("8888");
		portField.setPreferredSize(new Dimension(168, 20));// 设置下拉框(wieth,height)
		portPanel.add(new JLabel("端 口 号:"));
		portPanel.add(portField);

		bottomBox.add(ipPanel);
		bottomBox.add(Box.createVerticalStrut(10));
		bottomBox.add(portPanel);

		ipportPanel.add(bottomBox, BorderLayout.CENTER);

		bottomPanel.add(ipportPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 * 获取登录按钮的值。
	 * @return JButton 登录按钮的值。
	 */
	public JButton getLoginButton() {
		return loginButton;
	}

	/**
	 * 获取密码文本框的值。
	 * @return JPasswordField 密码文本框的值。
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}

	/**
	 * 获取用户名下拉框的值。
	 * @return JComboBox 用户名下拉框的值。
	 */
	public JComboBox getUserComboBox() {
		return userComboBox;
	}

	/**
	 * 获取IP下拉框的值。
	 * @return JComboBox IP下拉框的值。
	 */
	public JComboBox getIpComboBox() {
		return ipComboBox;
	}

	/**
	 * 获取端口文本框的值。
	 * @return JTextField 端口文本框的值。
	 */
	public JTextField getPortField() {
		return portField;
	}

	/**
	 * 获取登录类LoginMain的值。
	 * @return LoginMain 登录类LoginMain的值。
	 */
	public LoginMain getFrame() {
		return frame;
	}


}
