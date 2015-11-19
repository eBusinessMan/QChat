package lzx.qqserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * �����ͻ��������û�����
 *@author luozhixiao
 *
 */
public class ServerListenThread extends Thread {

	
	/** ���ڴ洢ServerPanelListen��������ֵ */
	private ServerSocket serverSocket = null;

	/**
	 * ���췽��
	 */
	public ServerListenThread(final ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * �߳������ķ���
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
