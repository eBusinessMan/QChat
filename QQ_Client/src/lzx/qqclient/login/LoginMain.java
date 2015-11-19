package lzx.qqclient.login;

import javax.swing.JFrame;
import java.awt.TrayIcon;
import java.awt.SystemTray;

import lzx.qqclient.tools.QQUtils;
import lzx.qqclient.tools.TrayMenu;
public class LoginMain extends JFrame {

	private static final long serialVersionUID = -4100586769394236809L;

	/** Login��� */
	private LoginPanel loginPanel;

	/**
	 * ���췽��
	 */
	public LoginMain() 
	{
		super("��ҵQQ�û���¼");
		QQUtils.initSystemTray();
		loginPanel = new LoginPanel(this);	
		this.add(loginPanel);
		this.setIconImage(QQUtils.qqicon);// ����QQͼ��
		this.setSize(330, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);// ����Ϊ������ʾ
		this.setResizable(false);// ���ò������
		this.setVisible(true);// ����Ϊ����
	}

	/**
	 * �ͷ���Ӧ�ó�����ڷ���
	 * @param args
	 */
	
	public static void main(String[] args)
	{
		QQUtils.setGlobalFont("����");
		new LoginMain();
	}
}
