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
 * 启动配置类
 * 
 *@author luozhixiao
 * 
 */
public class ServerMain extends JFrame {

	private static final long serialVersionUID = 2882817269100993556L;

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

	/** 进入服务器 */
	private JButton intoButton;

	/** 服务器配置监听 */
	private ServerMainListen serListen;

	/**
	 * 构造。
	 */
	public ServerMain() {

		this.setLayout(new BorderLayout());

		initcenterPanel();
		initButtonPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		readConfig();// 从配置文件里面读配置

		this.setTitle("服务器启动配置");
		this.setResizable(false);
		this.setSize(460, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * 服务端主程序入口
	 * 
	 * @param args
	 *            字符串数组
	 */
	public static void main(String[] args) {
		QQUtils.setGlobalFont("宋体");
		ServerMain servermain = new ServerMain();

		if (servermain.serListen.testConnnection() == 1) {
			
			if(servermain.serListen.testPort(servermain.serListen.getPort())==1){
				// 登录成功
				servermain.dispose();
				ServerFrame serverFrame = new ServerFrame();

				String str = "[" + QQUtils.formatTime() + "]: 进入了服务器!\n";
				serverFrame.getServerPanel().getServerLogArea().append(str);
				// 往数据库里面写日志
				LogDaoImpl logDao = new LogDaoImpl();
				logDao.addLog(QQUtils.formatDate(), str);
			}else{
				servermain.setVisible(true);
				JOptionPane.showMessageDialog(servermain, "端口被占用！","连接提示", 1);
			}
		} else {
			servermain.setVisible(true);
			JOptionPane.showMessageDialog(servermain, "连接数据库失败，请重新配置SQL数据库配置！","连接提示", 1);
		}
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
	 * 中间面板方法
	 */
	private void initcenterPanel() {

		centerPanel = new JPanel(new BorderLayout());

		// 创建一个盒子来存方各内容面板
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(10));
		// 存储方式
		JPanel savePanel = new JPanel();
		savePanel.setBorder(BorderFactory.createTitledBorder("数据存储类型"));
		savePanel.add(new JLabel("存储方式: "));
		JComboBox saveComboBox = new JComboBox();
		saveComboBox.addItem("SQL");
		saveComboBox.addItem("XML");
		saveComboBox.setEnabled(false); // 存储方式下拉框，设置为不可更改.
		savePanel.add(saveComboBox);
		box.add(savePanel);
		box.add(Box.createVerticalStrut(10));

		Box urlBox = Box.createVerticalBox(); //用来存放url/user/password 的盒子
		urlBox.setBorder(BorderFactory.createTitledBorder("SQL数据库配置")); // 为盒子设置标题

		// 驱动
		JPanel drivePanel = new JPanel();
		drivePanel.add(new JLabel("驱        动: "));
		driveField = new JTextField(40);
		drivePanel.add(driveField);
		urlBox.add(drivePanel);

		// 连接url
		JPanel urlPanel = new JPanel();
		urlPanel.add(new JLabel("连   接  URL: "));
		urlField = new JTextField(40);
		urlPanel.add(urlField);
		urlBox.add(urlPanel); // 把URL加到urlBox盒子里

		// 用户名 / 密 码
		JPanel userPswPanel = new JPanel();
		userPswPanel.add(new JLabel("用   户   名: "));
		userField = new JTextField(14);
		userPswPanel.add(userField);

		userPswPanel.add(new JLabel(" 密   码: "));
		passwordField = new JPasswordField(14);
		userPswPanel.add(passwordField);
		urlBox.add(userPswPanel); // 把用户密码加到urlBox盒子里

		box.add(urlBox); // 再把urlBox加到顶的Box里
		box.add(Box.createVerticalStrut(10));

		Box fileBox = Box.createVerticalBox(); // 用来存放文件和端口
		fileBox.setBorder(BorderFactory.createTitledBorder("服务器网络配置")); // 为盒子设置标题
		fileBox.add(Box.createVerticalStrut(3));

		// 文 件端口号
		JPanel filePanel = new JPanel();
		filePanel.add(new JLabel("文        件: "));
		fileField = new JTextField(14);
		fileField.setEditable(false);
		filePanel.add(fileField);

		filePanel.add(new JLabel(" 端 口 号:"));
		portField = new JTextField(14);

		filePanel.add(portField);
		fileBox.add(filePanel);

		box.add(fileBox);
		box.add(Box.createVerticalStrut(10));

		centerPanel.add(box, BorderLayout.CENTER);
	}

	/**
	 * 按钮面板方法
	 */
	private void initButtonPanel() {// 按钮面板
		buttonPanel = new JPanel();

		serListen = new ServerMainListen(this);

		connButtonn = new JButton("测试连接");
		connButtonn.addActionListener(serListen);

		portButton = new JButton("测试端口");
		portButton.addActionListener(serListen);

		saveConfButton = new JButton("保存配置");
		saveConfButton.addActionListener(serListen);
		intoButton = new JButton("进入服务器");
		intoButton.addActionListener(serListen);

		buttonPanel.add(connButtonn);
		buttonPanel.add(portButton);
		buttonPanel.add(saveConfButton);
		buttonPanel.add(intoButton);
	}

	/**
	 * 获取进入服务器按钮的值。
	 * 
	 * @return JButton 进入服务器按钮的值。
	 */
	public JButton getIntoButton() {
		return intoButton;
	}

	/**
	 * 获取测试连接按钮的值。
	 * 
	 * @return JButton 测试连接按钮的值。
	 */
	public JButton getConnButtonn() {
		return connButtonn;
	}

	/**
	 * 获取端口文本框的值。
	 * 
	 * @return JTextField 端口文本框的值。
	 */
	public JTextField getPortField() {
		return portField;
	}

	/**
	 * 获取测试端口按钮的值。
	 * 
	 * @return JButton 测试端口按钮的值。
	 */
	public JButton getPortButton() {
		return portButton;
	}

	/**
	 * 获取保存配置按钮的值。
	 * 
	 * @return JButton 保存配置按钮的值。
	 */
	public JButton getSaveConfButton() {
		return saveConfButton;
	}

	/**
	 * 获取驱动文本框的值。
	 * 
	 * @return JTextField 驱动文本框的值。
	 */
	public JTextField getDriveField() {
		return driveField;
	}

	/**
	 * 获取密码文本框的值。
	 * 
	 * @return JTextField 密码文本框的值。
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}

	/**
	 * 获取URL文本框的值。
	 * 
	 * @return JTextField URL文本框的值。
	 */
	public JTextField getUrlField() {
		return urlField;
	}

	/**
	 * 获取用户文本框的值。
	 * 
	 * @return JTextField 用户文本框的值。
	 */
	public JTextField getUserField() {
		return userField;
	}

}
