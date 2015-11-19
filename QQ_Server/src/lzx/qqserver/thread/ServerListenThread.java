package lzx.qqserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听客户端连接用户的类
 *@author luozhixiao
 *
 */
public class ServerListenThread extends Thread {

	
	/** 用于存储ServerPanelListen传过来的值 */
	private ServerSocket serverSocket = null;

	/**
	 * 构造方法
	 */
	public ServerListenThread(final ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * 线程启动的方法
	 */
	public void run() {

		while (true) {
			Socket socket;
			try {
				socket = serverSocket.accept();
				new ServerReceiveThread(socket).start();

			} catch (IOException e) {
				
				break;
			}
		}

	}

}
