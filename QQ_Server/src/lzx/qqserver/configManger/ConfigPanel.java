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
 * 配置面板类
 * @author luozhixiao
 *
 */
public class ConfigPanel extends JPanel {

	private static final long serialVersionUID = 2882817269100993556L;

	/** 数据存储类型面板 */
	private JPanel savePanel;

	/** 配置面板 */
	private JPanel centerPanel;

	/** 按钮面板 */
	private JPanel buttonPanel;

	/** 数据库驱动 */
	private JTextField driveField;

	/** 连接url */
	private JTextField urlField;

	/** 用户名 */
	private JTextField userField;

	/** 密码 */
	private JPasswordField passwordField;

	/** 文件 */
	private JTextField fileField;

	/** 端口。 */
	private JTextField portField;

	/** 测试连接 */
	private JButton connButtonn;

	/** 测试端口 */
	private JButton portButton;

	/** 保存配置 */
	private JButton saveConfButton;

	/** 恢复默认配置*/
	private JButton defaultButton;

	/**
	 * 构造方法
	 */
	public ConfigPanel() {

		this.setLayout(new BorderLayout());

		initTopPanel();
		initCenterPanel();
		initButtonPanel();
		this.add(savePanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		readConfig();// 从配置文件里面读配置
		this.setVisible(true);

	}
	/** 
	 * 从配置文件里面读配置方法
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
	 *存储方式
	 */
	private void initTopPanel() {

		
		savePanel = new JPanel();
		savePanel.setBorder(BorderFactory.createTitledBorder("数据存储类型"));
		savePanel.add(new JLabel("存储方式: "));
		JComboBox saveComboBox = new JComboBox();
		saveComboBox.addItem("SQL");
		saveComboBox.addItem("XML");
		saveComboBox.setEnabled(false); // 设置下拉框不可编辑
		savePanel.add(saveComboBox);
	}

	/**
	 *oracle数据库配置和服务器网络配置
	 *
	 */
	private void initCenterPanel() {

		centerPanel = new JPanel(new BorderLayout());

		// oracle数据库配置--------------------------------------
		JPanel oraclePanel = new JPanel();
		oraclePanel.setBorder(BorderFactory.createTitledBorder("SQL数据库配置"));
		Box oracleBox = Box.createVerticalBox();
		// 驱动
		JPanel drivePanel = new JPanel();
		drivePanel.add(new JLabel("驱       动:  "));
		driveField = new JTextField(50);
		drivePanel.add(driveField);

		// 连接url
		JPanel urlPanel = new JPanel();
		urlPanel.add(new JLabel("连  接  URL:  "));
		urlField = new JTextField(50);
		urlPanel.add(urlField);

		// 用户名 / 密 码
		JPanel userPanel = new JPanel();
		userPanel.add(new JLabel("用  户   名:  "));
		userField = new JTextField(50);
		userPanel.add(userField);

		JPanel passwordPanel = new JPanel();
		passwordPanel.add(new JLabel("密       码:  "));
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

		// 服务器网络配置-----------------------------------------------
		Box fileBox = Box.createVerticalBox(); // 用来存放文件和端口
		fileBox.setBorder(BorderFactory.createTitledBorder("服务器网络配置"));

		// 文 件端口号
		JPanel filePanel = new JPanel();

		filePanel.add(new JLabel("文      件:  "));
		fileField = new JTextField(50);
		fileField.setEditable(false);
		filePanel.add(fileField);

		JPanel portPanel = new JPanel();
		portPanel.add(new JLabel("端  口  号:  "));
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
	 * 按钮面板
	 */
	private void initButtonPanel() {// 按钮面板
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder("操作选择"));
		ConfigPanelListen confListen = new ConfigPanelListen(this);

		Box btnBox = Box.createHorizontalBox();
		connButtonn = new JButton("测试连接");
		connButtonn.addActionListener(confListen);

		portButton = new JButton("测试端口");
		portButton.addActionListener(confListen);

		saveConfButton = new JButton("保存配置");
		saveConfButton.addActionListener(confListen);

		defaultButton = new JButton("恢复默认配置");
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
	 * 获取测试连接按钮的值。
	 * @return JButton 测试连接按钮的值。
	 */
	public JButton getConnButtonn() {
		return connButtonn;
	}

	/**
	 * 获取恢复默认配置按钮的值。
	 * @return JButton 恢复默认配置按钮的值。
	 */
	public JButton getDefaultButton() {
		return defaultButton;
	}
	/**
	 * 获取测试端口按钮的值。
	 * @return JButton 测试端口按钮的值。
	 */
	public JButton getPortButton() {
		return portButton;
	}

	/**
	 * 获取保存配置按钮的值。
	 * @return JButton 保存配置按钮的值。
	 */
	public JButton getSaveConfButton() {
		return saveConfButton;
	}

	/**
	 * 获取驱动文本框的值。
	 * @return JTextField 驱动文本框的值。
	 */
	public JTextField getDriveField() {
		return driveField;
	}

	/**
	 * 获取密码文本框的值。
	 * @return JTextField 密码文本框的值。
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	/**
	 * 获取端口文本框的值。
	 * @return JTextField 端口文本框的值。
	 */
	public JTextField getPortField() {
		return portField;
	}
	/**
	 * 获取URL文本框的值。
	 * @return JTextField URL文本框的值。
	 */
	public JTextField getUrlField() {
		return urlField;
	}

	/**
	 * 获取用户文本框的值。
	 * @return JTextField 用户文本框的值。
	 */
	public JTextField getUserField() {
		return userField;
	}

}
