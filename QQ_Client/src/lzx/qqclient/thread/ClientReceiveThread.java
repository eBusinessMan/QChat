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
 * 客户端监听线程类
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
				qqPack = chat.readObject();// 读取服务端发送的数据
				QQPackType packType = qqPack.getType();
				String from = qqPack.getFrom();
				String to = qqPack.getTo();
				Object content = qqPack.getContent();
				// 对收到的包进行判断。
				if (packType == QQPackType.ONLINE_LIST) { //收到的是用户在线包

					listen.getOnlineList(qqPack); // 用户在线列表方法

				} else if (packType == QQPackType.BULLETIN) { //收到的是公告包
					
					listen.getBulletin(content.toString()); // 公告包方法

				} else if (packType == QQPackType.UPDATE_PASSWORD) // 收到的是修改密码包
				{
					listen.updatePassword(content.toString()); // 更改密码方法

				} else if (packType == QQPackType.FORCE_DOWNLINE) // 收到的是强制下线包
				{
					listen.coerceDownOnline(); // 强制下线方法

				} else if (packType == QQPackType.USER_LINE) {//收到的是用户上线包

					listen.userUpOnline(content); // 用户上线方法

				} else if (packType == QQPackType.USER_WODNLINE) {//收到的是用户下线包

					listen.userDownOnline(from, content); // 用户下线方法。

				} else if (packType == QQPackType.PRIVATE_TALK) { // 收到的是私聊包

					listen.privateTalk(qqPack);// 私聊方法

				} else if (packType == QQPackType.GROUP_CHAT) {//收到的是群聊包

					listen.groupChat(qqPack); // 群聊方法
					
				} else if (packType == QQPackType.STOP_SERVER) {//收到的是停止服务包
					
					listen.stopServer(); // 停止服务方法.
				}else if(packType == QQPackType.UPDATE_INFO){//收到的是用户资料被修改的包
					listen.updateUser(content);//修改用户资料方法
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;}
		}
	}
}
