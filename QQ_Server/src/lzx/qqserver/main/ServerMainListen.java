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
 * 启动配置监听类
 * 
 * @author luozhixiao
 * 
 */
public class ServerMainListen implements ActionListener {

	/** 启动配置 */
	private ServerMain frame;

	/** 端口 */
	private String port;

	/**
	 * 构造方法
	 */
	public ServerMainListen(final ServerMain frame) {
		this.frame = frame;

	}

	/**
	 * 监听事件方法
	 */

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == frame.getConnButtonn()) {// 测试连接

			int i = testConnnection();
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "连接数据库成功!", "连接提示", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame,
						"连接数据库失败, 请检查SQL数据库配置!", "连接提示", 1);
			}

		} else if (e.getSource() == frame.getPortButton()) {// 测试端口

			int i = testPort(port);
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "端口 " + this.getPort()
						+ " 可用!", "端口提示", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame, "端口 " + this.getPort()
						+ " 已被占用!", "端口提示", 1);
			}

		} else if (e.getSource() == frame.getSaveConfButton()) {// 保存配置

			int i = saveConfig();
			if (i==1) {
				JOptionPane.showMessageDialog(frame, "保存配置成功!", "配置提示", 1);
			} else if(i==-1){
				JOptionPane.showMessageDialog(frame, "保存配置失败!", "配置提示", 1);
			}

		} else if (e.getSource() == frame.getIntoButton()) {// 进入服务器

			int i = intoServer();
			if (i==1) {
				// 登录成功
				this.frame.dispose();
				ServerFrame serverFrame = new ServerFrame();
				
				String str = "[" + QQUtils.formatTime() + "]: 进入了服务器!\n";
				serverFrame.getServerPanel().getServerLogArea().append(str);

				LogDaoImpl logDao = new LogDaoImpl();
				// 往数据库里面写日志	
				logDao.addLog(QQUtils.formatDate(), str);

			}
		}

	}

	/**
	 * 测试数据库连接的方法
	 * 
	 * @return int 1连接数据库成功，-1连接数据库失败
	 */
	public int testConnnection() {

		int i = 0;
		String drive = frame.getDriveField().getText().trim(); // 驱动
		String url = frame.getUrlField().getText().trim(); // url
		String username = frame.getUserField().getText().trim(); // 用户名
		String password = new String(frame.getPasswordField().getPassword())
				.trim(); // 密码

		if (judgeStr(drive, url, username, password)) {// 判断
														// 驱动、URL、用户名、密码是否符合规范
			Connection conn = null;
			try {
				// 1. 注册和加载数据库驱动程序
				Class.forName(drive);

				// 2. 建立与数据库的连接通道
				conn = DriverManager.getConnection(url, username, password);

			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("SQL服务器没打开或数据库配置错误");
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
	 * 测试端口是否可用的方法
	 * 
	 * @return int 1端口可用，-1端口被占用
	 */
	public int testPort(String port) {

		int i = 0;
		port = frame.getPortField().getText().trim();// 端口
		this.setPort(port);

		if (judgePort(port)) {// 验证端口

			ServerSocket socket = null; // 服务sockct
			try {
				socket = new ServerSocket(Integer.parseInt(port));
				socket.close();
			} catch (Exception e) {
				System.out.println("端口被占用");
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
	 * 保存配置的方法
	 * 
	 * @return int 1保存配置成功，-1保存配置失败
	 */
	public int saveConfig() {

		int i = 0;
		String drive = frame.getDriveField().getText().trim(); // 驱动
		String url = frame.getUrlField().getText().trim(); // url
		String username = frame.getUserField().getText().trim(); // 用户名
		String password = new String(frame.getPasswordField().getPassword())
				.trim(); // 密码
		String port = frame.getPortField().getText().trim();// 端口

		if (judgeStr(drive, url, username, password)) {// 判断 驱动、URL、用户名、密码是否为空
			if (judgePort(port)) {// 验证端口

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
	 * 进入服务器的方法
	 * 
	 * @return int 1成功进入服务器，-1失败
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
	 * 判断 驱动、URL、用户名、密码是否为空
	 * 
	 * @param drive
	 *            驱动
	 * @param url
	 *            URL
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return boolean true 驱动、URL、用户名、密码规范，false驱动、URL、用户名、密码不规范
	 */
	public boolean judgeStr(String drive, String url, String username,
			String password) {

		if (drive.length() == 0 || drive == null) {

			JOptionPane
					.showMessageDialog(frame, "数据库驱动不能为空, 请输入驱动!", "连接提示", 1);
			return false;

		} else if (url.length() == 0 || url == null) {

			JOptionPane.showMessageDialog(frame, "连接URL不能为空, 请输入URL!", "连接提示",
					1);
			return false;

		} else if (username.length() == 0 || username == null) {

			JOptionPane.showMessageDialog(frame, "用户名不能为空, 请输入用户名!", "连接提示", 1);
			return false;

		}
		return true;
	}

	/**
	 * 验证端口是否符合规范的方法
	 * 
	 * @param port
	 *            端口
	 * @return boolean true端口符合规范，false端口不符合规范
	 */
	public boolean judgePort(String port) {// 验证端口

		if (port.length() == 0 || port == null) {// 验证端口是否为空

			JOptionPane.showMessageDialog(frame, "端口不能为空!", "端口提示", 1);
			return false;

		} else if (!Pattern.matches("[0-9]+$", port)) {// 判断端口是否是数字

			JOptionPane.showMessageDialog(frame, "端口必须是数字!", "端口提示", 1);
			return false;

		} else if (Integer.parseInt(port) < 1024
				|| Integer.parseInt(port) > 65535) {// 判断端口是否在1024-65535 之间

			JOptionPane.showMessageDialog(frame, "端口必须在1024-65535之间!", "端口提示",
					1);
			return false;
		}
		return true;
	}

	/**
	 * 获取port的值。
	 * 
	 * @return JTextField port的值。
	 */
	public String getPort() {
		return port;
	}

	/**
	 * 设置port的值
	 */
	public void setPort(String port) {
		this.port = port;
	}
}
