package lzx.qqclient.login;

import javax.swing.JFrame;
import java.awt.TrayIcon;
import java.awt.SystemTray;

import lzx.qqclient.tools.QQUtils;
import lzx.qqclient.tools.TrayMenu;
public class LoginMain extends JFrame {

	private static final long serialVersionUID = -4100586769394236809L;

	/** Login面板 */
	private LoginPanel loginPanel;

	/**
	 * 构造方法
	 */
	public LoginMain() 
	{
		super("企业QQ用户登录");
		QQUtils.initSystemTray();
		loginPanel = new LoginPanel(this);	
		this.add(loginPanel);
		this.setIconImage(QQUtils.qqicon);// 设置QQ图标
		this.setSize(330, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);// 设置为居中显示
		this.setResizable(false);// 设置不能最大化
		this.setVisible(true);// 设置为可视
	}

	/**
	 * 客服端应用程序入口方法
	 * @param args
	 */
	
	public static void main(String[] args)
	{
		QQUtils.setGlobalFont("宋体");
		new LoginMain();
	}
}
