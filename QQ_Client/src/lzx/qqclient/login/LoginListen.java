package lzx.qqclient.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import lzx.qqclient.business.ChatUtils;
import lzx.qqclient.chat.ChatFrame;


import pub.TCPPack;
import pub.QQPackType;
import pub.User;

public class LoginListen implements ActionListener {

	/** @author luozhixiao */
	private LoginPanel loginPanel;
  
	private  ChatUtils chat;

	private int pswErrorCount = 0;
	
	private User user =null;
	
	private ChatFrame chatFrame;

	public LoginListen(final LoginPanel loginPanel) {
		
		this.loginPanel = loginPanel;
		
	}

	public void actionPerformed(ActionEvent e) {
	
		if (e.getSource() == loginPanel.getLoginButton()) {
			if (loginCheckMethod()) {
				//进入聊天主界面
				chatFrame = new ChatFrame(user,chat);
				chatFrame.setTitle("用户："+user.getSname()+ " [" + user.getSid()+ "]");
				loginPanel.getFrame().dispose();// 消掉登录窗口
			} else if (pswErrorCount == 3) {
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"密码输入错误三次，将退出登录界面!", "登录失败", 1);
				loginPanel.getFrame().dispose();
			}
		}

	}

	/**
	 * 用户登录及验证用户的方法
	 * 
	 * @return boolean true登录成，false登录失败
	 */

	public boolean loginCheckMethod() {

		boolean bo = false;

		String id = loginPanel.getUserComboBox().getSelectedItem().toString()
				.trim();
		String password = new String(loginPanel.getPasswordField()
				.getPassword()).trim();
		String ip = loginPanel.getIpComboBox().getSelectedItem().toString()
				.trim();
		String port = loginPanel.getPortField().getText();
		
		boolean bl = checkUserPsW(id, password, ip, port);//检查ID、密码、IP、端口是否合法
		
		if (bl == true) {
			try {
				TCPPack loginPack = new TCPPack();
				loginPack.setType(QQPackType.LOGIN);// 设置QQ包为登录类型
				loginPack.setFrom(id);
				loginPack.setContent(password);
				chat=new ChatUtils(ip,Integer.parseInt(port));
				chat.setTcpPack(loginPack);
				chat.sendObject();
				loginPack =chat.readObject();
				if (loginPack.getType() == QQPackType.LOGIN_SUCCEESS) {// 登录成功	
					user = (User)loginPack.getContent();
					bo = true;
				} else {
					// 登录失败
					if ((loginPack.getContent().toString()).equals("ID")) {
						JOptionPane.showMessageDialog(loginPanel.getFrame(),
								"用户[" + id + "]不存在!", "登录失败", 1);

					} else if ((loginPack.getContent().toString())
							.equals("PASSWORD")) {

						pswErrorCount++; // 登录失败次数。
						if (pswErrorCount < 3) {
							JOptionPane.showMessageDialog(
									loginPanel.getFrame(), "密码错误!", "登录失败", 1);
						}

					} else if((loginPack.getContent().toString()).equals("Online")) {
						JOptionPane.showMessageDialog(loginPanel.getFrame(),
								"用户已经登录!", "登录失败", 1);

					}
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"未知错误,请重新登录!", "登录失败", 1);

			} catch (UnknownHostException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"主机 IP 地址无法确定!", "登录失败", 1);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"服务器未开或配置出错!", "登录失败", 1);

			} catch (Exception e) {
				e.printStackTrace();}
		}

		return bo;
	}

	public boolean checkUserPsW(String id, String password, String ip,
			String port) {

		// 判断用户名是否符合规范
		if (id.length() == 0) {// 如果用户名为空
			JOptionPane.showMessageDialog(loginPanel.getFrame(), "用户名不能为空!",
					"用户名提示", 1);
			return false;
		}

		// 判断密码是否符合规范(密码 长度在 3~16 之间 (只允许是数字，字母 , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" 密码不能为空,请输入密码! ", "密码提示", 1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" 密码长度必需在 3~16 之间! ", "密码提示", 1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" 密码只允许是数字，字母 , _ ! ", "密码提示", 1);
			return false;
		}

		// 判断IP是否符合规范
		if (ip.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" IP 地址不能为空! ", "IP提示", 1);
			return false;
		} else if (!isIp(ip)) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " IP 地址不合法! ",
					"IP提示", 1);
			return false;
		}

		// 判断端口是否符合规范
		if (port.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " 端口不能为空! ",
					"端口提示", 1);
			return false;
		} else if (!Pattern.matches("[0-9]+$", port)) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " 端口必须是数字! ",
					"提端口提示", 1);
			return false;
		} else if (Integer.parseInt(port) < 1024
				|| Integer.parseInt(port) > 65535) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					"端口只能是在1024-65535之间!", "端口提示", 1);
			return false;
		}
		return true;
	}

	/**
	 * 判断IP是否符合规范的方法
	 * 
	 * @param ip
	 *            要传入的IP。
	 * @return true 表示合法IP,false 表示IP不合法。
	 */
	public boolean isIp(String ip) {
		String str[] = ip.split("\\.");
		if (str.length != 4) {
			return false;
		}
		if (Integer.parseInt(str[0]) < 1 || Integer.parseInt(str[0]) > 255) {
			return false;
		}

		for (int i = 1; i < str.length; i++) {
			if (Integer.parseInt(str[i]) < 0 || Integer.parseInt(str[i]) > 255) {
				return false;
			}
		}

		return true;
	}

	public User getUser() {
		return user;
	}

	public ChatFrame getChatFrame() {
		return chatFrame;
	}


}
