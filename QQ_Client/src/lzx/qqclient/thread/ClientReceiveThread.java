package lzx.qqclient.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import lzx.qqclient.business.*;
import lzx.qqclient.chat.UpdatePswDialog;
import lzx.qqclient.login.LoginListen;
import lzx.qqclient.tools.QQUtils;
import lzx.qqclient.tools.ReadWriteLog;

import pub.TCPPack;
import pub.QQPackType;
import pub.User;

/**
 * �ͻ��˼����߳���
 *@author luozhixiao
 *
 */
public class ClientReceiveThread extends Thread {
    private ChatUtils chat;
    private ChatListen listen;
    private TCPPack qqPack;
    public ClientReceiveThread(ChatUtils chat)
    {
    	listen=new ChatListen(chat);
    }
	public void run() {
		while (true) {
			try {
				qqPack = chat.readObject();// ��ȡ����˷��͵�����
				QQPackType packType = qqPack.getType();
				String from = qqPack.getFrom();
				String to = qqPack.getTo();
				Object content = qqPack.getContent();
				// ���յ��İ������жϡ�
				if (packType == QQPackType.ONLINE_LIST) { //�յ������û����߰�

					listen.getOnlineList(qqPack); // �û������б���

				} else if (packType == QQPackType.BULLETIN) { //�յ����ǹ����
					
					listen.getBulletin(content.toString()); // ���������

				} else if (packType == QQPackType.UPDATE_PASSWORD) // �յ������޸������
				{
					listen.updatePassword(content.toString()); // �������뷽��

				} else if (packType == QQPackType.FORCE_DOWNLINE) // �յ�����ǿ�����߰�
				{
					listen.coerceDownOnline(); // ǿ�����߷���

				} else if (packType == QQPackType.USER_LINE) {//�յ������û����߰�

					listen.userUpOnline(content); // �û����߷���

				} else if (packType == QQPackType.USER_WODNLINE) {//�յ������û����߰�

					listen.userDownOnline(from, content); // �û����߷�����

				} else if (packType == QQPackType.PRIVATE_TALK) { // �յ�����˽�İ�

					listen.privateTalk(qqPack);// ˽�ķ���

				} else if (packType == QQPackType.GROUP_CHAT) {//�յ�����Ⱥ�İ�

					listen.groupChat(qqPack); // Ⱥ�ķ���
					
				} else if (packType == QQPackType.STOP_SERVER) {//�յ�����ֹͣ�����
					
					listen.stopServer(); // ֹͣ���񷽷�.
				}else if(packType == QQPackType.UPDATE_INFO){//�յ������û����ϱ��޸ĵİ�
					listen.updateUser(content);//�޸��û����Ϸ���
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;}
		}
	}
}
