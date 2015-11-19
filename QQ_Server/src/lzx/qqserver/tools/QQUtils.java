package lzx.qqserver.tools;

import java.awt.Font;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import lzx.qqserver.thread.ServerReceiveThread;



/**
 * �����ࣨ����ʵ����ͼƬ��������Ϣ��Ⱥ����Ϣ����ʽ��ʱ��ȣ�
 * @author luozhixiao
 *
 */
public class QQUtils {
	
	public static Icon serverstart = new ImageIcon("./images/serverstart.gif"); //��ʼ����
	
	public static Icon serverstop = new ImageIcon("./images/serverstop.gif"); //�رշ���
	
	/** ���ڴ�QQ�ţ����߳�socket */
	public static Map<String, ServerReceiveThread> threadMap = new HashMap<String, ServerReceiveThread>();
	
	/**
	 * ��������
	 */
	public static void sendTo(Object message, String id){
		
		ServerReceiveThread rt = threadMap.get(id);
		if (rt!=null)
			rt.sendMessage(message);
	}
	
	/**
	 * ����������,��map�������
	 * @param message ��Ϣ
	 */
	public static void sendToAll(Object message)
	{
 	
		Collection sts = threadMap.values();
		
		for (Iterator it = sts.iterator(); it.hasNext();) {
			ServerReceiveThread rt = (ServerReceiveThread) it.next();
			rt.sendMessage(message);
		}
	}
	
	/**
	 * �ر������̡߳�
	 */
	public static void closeThread()
	{
		Collection sts = threadMap.values();
		for (Iterator it = sts.iterator(); it.hasNext();) {
			ServerReceiveThread rt = (ServerReceiveThread) it.next();

				try {
					rt.getOis().close();
					rt.getOos().close();
					rt.getSocket().close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			
		}	
	}

	
	
	/** �����������ƣ���ʽ����С */
	public static void setGlobalFont(String font) {

		Font fontRes = new Font(font, Font.PLAIN, 12);
		for (Enumeration keys = UIManager.getDefaults().keys(); keys
				.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, new FontUIResource(fontRes));
			}
		}
	}
	
	/**
	 * ��ʽ�����ڵķ���
	 * @return String����
	 */
	public static String formatDate(){
		
		String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return filename;
		
	}
	
	/**
	 * ��ʽ��ʱ��ķ���
	 * @return Stringʱ��
	 */
	public static String formatTime(){
			
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
	}

}
