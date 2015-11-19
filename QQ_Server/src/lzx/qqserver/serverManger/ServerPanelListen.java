package lzx.qqserver.serverManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;
import lzx.qqserver.thread.ServerListenThread;
import lzx.qqserver.tools.ConfigFile;
import lzx.qqserver.tools.QQUtils;
import lzx.qqserver.userManger.UserPanel;

import pub.QQPack;
import pub.QQPackType;


/**
 * 服务管理面板监听类
 *@author luozhixiao
 *
 */
public class ServerPanelListen implements ActionListener {

	/** 服务管理 */
	private ServerPanel serverPanel;

	/** 读写日志类 */
	private LogDaoImpl logDao;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	/** 用户管理 */
	private UserPanel userPanel;

	/** 网络通讯Socket */
	private ServerSocket serverSocket = null;

	/**
	 * 构造函数
	 * @param serverPanel 服务管理面板
	 */
	public ServerPanelListen(final ServerPanel serverPanel) {

		this.serverPanel = serverPanel;
		logDao = new LogDaoImpl();
		userDao = new UserDaoImpl();

	}
	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == serverPanel.getSendBulletinBtn()) {// 发布公告

			SendBulletin();

		} else if (e.getSource() == serverPanel.getStartSeverBtn()) {// 开始服务

			startServer();

		} else if (e.getSource() == serverPanel.getStopServerBtn()) {// 停止服务

			stopServer();

		} else if (e.getSource() == serverPanel.getCoerceDownBtn()) {// 强制下线

			CoerceDown();
		}

	}
	/**
	 * 开始服务的方法
	 */
	private void startServer() {

		// Socket

		ConfigFile confile = new ConfigFile("./config/config.ini");

		int port = Integer.parseInt(confile.getKeyValue("port"));// 从配置文件里面读取端口
		String ip = confile.getKeyValue("ip");
		try {
			serverSocket = new ServerSocket(port, 5, InetAddress.getByName(ip));

			new ServerListenThread(serverSocket).start();// 启动线程监听客户端连接

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(serverPanel,
					"启动服务失败, 可能是端口被占用,请重新配置!", "服务提示", 1);

		}

		if (serverSocket != null) {// 说明服务器已经启动
			serverPanel.getImageLabel().setIcon(QQUtils.serverstart);// 设置球可以转动
			serverPanel.getStartSeverBtn().setEnabled(false);
			serverPanel.getStopServerBtn().setEnabled(true);
			serverPanel.getCoerceDownBtn().setEnabled(true);
			serverPanel.getSendBulletinBtn().setEnabled(true);
			serverPanel.getSendBulletinArea().setEditable(true);// 公告文本框设置为可编辑

			String str = "[" + QQUtils.formatTime() + "]: 服务启动成功!\n";
			serverPanel.getServerLogArea().append(str);
			serverPanel.getServerLogArea().setCaretPosition(
					serverPanel.getServerLogArea().getDocument().getLength());
			// 往数据库里面写日志
			logDao.addLog(QQUtils.formatDate(), str);

		} else {

			String str = "[" + QQUtils.formatTime() + "]: 服务启动失败!\n";
			serverPanel.getServerLogArea().append(str);
			serverPanel.getServerLogArea().setCaretPosition(
					serverPanel.getServerLogArea().getDocument().getLength());
			// 往数据库里面写日志
			logDao.addLog(QQUtils.formatDate(), str);
		}
	}

	/**
	 * 发送公告方法
	 */
	public void SendBulletin() {
		// 3) 输入 公告信息，点 发送 按钮,向所有用户发公告信息;
		// 发送成功，提示成功;
		// 4) 公告信息一次最多可发送200个字符(注:1个英文字母或1个汉字都是1个字符);
		String bulletin = serverPanel.getSendBulletinArea().getText().trim();// 获取发送公告的内容

		if (bulletin.length() == 0) {
			JOptionPane.showMessageDialog(serverPanel, "公告信息不能为空!", "公告提示", 1);
			return;
		} else if (bulletin.length() > 200) {
			JOptionPane.showMessageDialog(serverPanel, "公告信息字数最多只能200个字符!",
					"公告提示", 1);
			return;
		}

		QQPack qqpack = new QQPack();
		qqpack.setType(QQPackType.BULLETIN);// 设置包类型为公告包
		qqpack.setContent(bulletin);
		QQUtils.sendToAll(qqpack);// 把公告发给所有用户

		JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "发送公告成功！",
				"公告提示", 1);
		serverPanel.getSendBulletinArea().setText("");// 发完公告，公告文本框置空

		// 写公告日志
		LogDaoImpl logDao = new LogDaoImpl();
		String str = "[" + QQUtils.formatTime() + "]: 发布了公告!\n";
		serverPanel.getServerLogArea().append(str);
		// 往数据库里面写日志
		logDao.addLog(QQUtils.formatDate(), str);

	}

	/**
	 * 停止服务方法
	 */
	public void stopServer() {
		//		
		// 3) 停止服务后释放一系列资源:
		// a) 释放所占的端口号;
		// b) 强制所有客户端在线用户下线;
		// c) 释放所占的服务端资源(线程);
		// d) 更新数据存储介质中的相应数据;
		// e) 刷新界面显示 (1.在线用户信息, 2.用户管理界面的用户信息);

		// int rows = serverPanel.getUserTable().getRowCount();

		int i = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
				"确定要停止服务器吗？", "服务提示", JOptionPane.YES_NO_OPTION);

		if (i == 0) {// 确定要关闭服务器

			userDao.setAllOffline();// 设置所有的用户不在线

			QQPack stopserverPack = new QQPack();
			stopserverPack.setType(QQPackType.STOP_SERVER);// 设置包类型为停止服务包
			QQUtils.sendToAll(stopserverPack);// 发停止服务包

			ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
			ServerFrame.getServerFrame().getServerPanel()
					.updateOnlineUserData();// 更新服务面板的在线用户表

			serverPanel.getImageLabel().setIcon(QQUtils.serverstop);
			serverPanel.getStartSeverBtn().setEnabled(true);
			serverPanel.getStopServerBtn().setEnabled(false);
			serverPanel.getCoerceDownBtn().setEnabled(false);
			serverPanel.getSendBulletinBtn().setEnabled(false);

			LogDaoImpl logDao = new LogDaoImpl();
			String str = "[" + QQUtils.formatTime() + "]: 关闭了服务器!\n";
			serverPanel.getServerLogArea().append(str);

			// 往数据库里面写日志
			logDao.addLog(QQUtils.formatDate(), str);

			try {
				QQUtils.closeThread(); // 关闭所有线程。
				serverSocket.close();// 关闭ServerSocket；

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * 强制下线方法
	 */
	public void CoerceDown() {// 强制下线的方法

		int flag = 0;
		// 获取强制下线用户的ID
		String id = null;
		try {
			id = serverPanel.getUserTable().getValueAt(
					serverPanel.getUserTable().getSelectedRow(), 0).toString();
		} catch (Exception e) {
			flag = 1;
			JOptionPane.showMessageDialog(serverPanel, "你没选中用户或没用在线用户！",
					"强制下线提示", 1);
		}

		if (flag == 0) {
			int i = JOptionPane.showConfirmDialog(serverPanel, "是否强制用户[" + id
					+ "]下线?", "强制下线提示", JOptionPane.YES_NO_OPTION);

			if (i == 0) {

				userDao.updateOnline(id, false);

				// userPanel = ServerFrame.getFrames().getUserPanel();//
				// 获取用户管理面板
				userPanel = ServerFrame.getServerFrame().getUserPanel();// 获取用户管理面板
				userPanel.updateUserData();// 更新用户管理面板用户表
				// serverPanel = ServerMain.serverMain.getServerPanel(); //
				// 获取服务管理面板
				serverPanel.updateOnlineUserData();// 更新服务管理面的在线用户表

				// 发强制下线包
				QQPack downOlinePack = new QQPack();
				downOlinePack.setType(QQPackType.FORCE_DOWNLINE);// 设置包类型为强制下线
				// downOlinePack.set
				QQUtils.sendTo(downOlinePack, id);// 将下线包发给要强制下线的那个用户
				QQUtils.threadMap.remove(id); // 把下线的用户从线程map里面移除。

				// 告诉所有在线用户某人下线了，发下线包
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(id);
				qqPack.setContent("用户[" + id + "] 下线了!");
				QQUtils.sendToAll(qqPack);

				String str = "[" + QQUtils.formatTime() + "]:用户[id]被强制下线了!\n";
				serverPanel.getServerLogArea().append(str);
				serverPanel.getServerLogArea().setCaretPosition(
						serverPanel.getServerLogArea().getDocument()
								.getLength());
				// 往数据库里面写日志
				logDao.addLog(QQUtils.formatDate(), str);

			}
		}

	}
}
