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
 * 服务端管理界面
 * 
 * @author luozhixiao
 * 
 */
public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 4178925623043643001L;

	/** 管理选项卡 */
	private JTabbedPane jtab;

	/** 服务管理 */
	private ServerPanel serverPanel;

	/** 用户管理 */
	private UserPanel userPanel;

	/** 日志管理 */
	private LogPanel logPanel;

	/** 配置管理 */
	private ConfigPanel configPanel;

	/** 服务端管理面板 */
	private static ServerFrame serverFrame;

	/** 用户的数据存储对象 */
	private UserDao userDao;

	/**
	 * 构造方法
	 */
	public ServerFrame() {

		jtab = new JTabbedPane();
		serverFrame = this;
		serverPanel = new ServerPanel();// 服务管理
		userPanel = new UserPanel();// 用户管理
		logPanel = new LogPanel();// 日志管理
		configPanel = new ConfigPanel();// 配置管理

		jtab.add("服务管理", serverPanel);
		jtab.add("用户管理", userPanel);
		jtab.add("日志管理", logPanel);
		jtab.add("配置管理", configPanel);

		this.add(jtab);

		this.setTitle("企业QQ服务器端");
		this.setResizable(false);
		this.setSize(650, 500);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				int i = JOptionPane.showConfirmDialog(serverFrame,
						"是否真的退出服务器！！！", "服务提示", JOptionPane.YES_NO_OPTION);

				if (i == 0) {

					userDao = new UserDaoImpl();
					userDao.setAllOffline();// 设置所有的用户不在线

					QQPack stopserverPack = new QQPack();
					stopserverPack.setType(QQPackType.STOP_SERVER);// 设置包类型为停止服务包
					QQUtils.sendToAll(stopserverPack);// 发停止服务包

					LogDaoImpl logDao = new LogDaoImpl();
					String str = "[" + QQUtils.formatTime() + "]: 关闭了服务器!\n";
					serverPanel.getServerLogArea().append(str);

					// 往数据库里面写日志
					logDao.addLog(QQUtils.formatDate(), str);
					
					QQUtils.closeThread(); // 关闭所有线程。
					System.exit(0);// 退出程序
				}
			}

		});

		this.setVisible(true);

	}

	/**
	 * 获取服务端管理面板
	 * 
	 * @return ServerFrame 服务端管理面板
	 */
	public static ServerFrame getServerFrame() {
		return serverFrame;
	}

	/**
	 * 获取用户管理面板
	 * 
	 * @return UserPanel 用户管理面板
	 */
	public UserPanel getUserPanel() {
		return userPanel;
	}

	/**
	 * 获取服务管理面板
	 * 
	 * @return ServerPanel 服务管理面板
	 */
	public ServerPanel getServerPanel() {
		return serverPanel;
	}
		
}
