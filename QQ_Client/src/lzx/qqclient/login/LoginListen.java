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
				//��������������
				chatFrame = new ChatFrame(user,chat);
				chatFrame.setTitle("�û���"+user.getSname()+ " [" + user.getSid()+ "]");
				loginPanel.getFrame().dispose();// ������¼����
			} else if (pswErrorCount == 3) {
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"��������������Σ����˳���¼����!", "��¼ʧ��", 1);
				loginPanel.getFrame().dispose();
			}
		}

	}

	/**
	 * �û���¼����֤�û��ķ���
	 * 
	 * @return boolean true��¼�ɣ�false��¼ʧ��
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
		
		boolean bl = checkUserPsW(id, password, ip, port);//���ID�����롢IP���˿��Ƿ�Ϸ�
		
		if (bl == true) {
			try {
				TCPPack loginPack = new TCPPack();
				loginPack.setType(QQPackType.LOGIN);// ����QQ��Ϊ��¼����
				loginPack.setFrom(id);
				loginPack.setContent(password);
				chat=new ChatUtils(ip,Integer.parseInt(port));
				chat.setTcpPack(loginPack);
				chat.sendObject();
				loginPack =chat.readObject();
				if (loginPack.getType() == QQPackType.LOGIN_SUCCEESS) {// ��¼�ɹ�	
					user = (User)loginPack.getContent();
					bo = true;
				} else {
					// ��¼ʧ��
					if ((loginPack.getContent().toString()).equals("ID")) {
						JOptionPane.showMessageDialog(loginPanel.getFrame(),
								"�û�[" + id + "]������!", "��¼ʧ��", 1);

					} else if ((loginPack.getContent().toString())
							.equals("PASSWORD")) {

						pswErrorCount++; // ��¼ʧ�ܴ�����
						if (pswErrorCount < 3) {
							JOptionPane.showMessageDialog(
									loginPanel.getFrame(), "�������!", "��¼ʧ��", 1);
						}

					} else if((loginPack.getContent().toString()).equals("Online")) {
						JOptionPane.showMessageDialog(loginPanel.getFrame(),
								"�û��Ѿ���¼!", "��¼ʧ��", 1);

					}
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"δ֪����,�����µ�¼!", "��¼ʧ��", 1);

			} catch (UnknownHostException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"���� IP ��ַ�޷�ȷ��!", "��¼ʧ��", 1);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(loginPanel.getFrame(),
						"������δ�������ó���!", "��¼ʧ��", 1);

			} catch (Exception e) {
				e.printStackTrace();}
		}

		return bo;
	}

	public boolean checkUserPsW(String id, String password, String ip,
			String port) {

		// �ж��û����Ƿ���Ϲ淶
		if (id.length() == 0) {// ����û���Ϊ��
			JOptionPane.showMessageDialog(loginPanel.getFrame(), "�û�������Ϊ��!",
					"�û�����ʾ", 1);
			return false;
		}

		// �ж������Ƿ���Ϲ淶(���� ������ 3~16 ֮�� (ֻ���������֣���ĸ , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" ���벻��Ϊ��,����������! ", "������ʾ", 1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" ���볤�ȱ����� 3~16 ֮��! ", "������ʾ", 1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" ����ֻ���������֣���ĸ , _ ! ", "������ʾ", 1);
			return false;
		}

		// �ж�IP�Ƿ���Ϲ淶
		if (ip.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					" IP ��ַ����Ϊ��! ", "IP��ʾ", 1);
			return false;
		} else if (!isIp(ip)) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " IP ��ַ���Ϸ�! ",
					"IP��ʾ", 1);
			return false;
		}

		// �ж϶˿��Ƿ���Ϲ淶
		if (port.length() == 0) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " �˿ڲ���Ϊ��! ",
					"�˿���ʾ", 1);
			return false;
		} else if (!Pattern.matches("[0-9]+$", port)) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(), " �˿ڱ���������! ",
					"��˿���ʾ", 1);
			return false;
		} else if (Integer.parseInt(port) < 1024
				|| Integer.parseInt(port) > 65535) {
			JOptionPane.showMessageDialog(loginPanel.getFrame(),
					"�˿�ֻ������1024-65535֮��!", "�˿���ʾ", 1);
			return false;
		}
		return true;
	}

	/**
	 * �ж�IP�Ƿ���Ϲ淶�ķ���
	 * 
	 * @param ip
	 *            Ҫ�����IP��
	 * @return true ��ʾ�Ϸ�IP,false ��ʾIP���Ϸ���
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
