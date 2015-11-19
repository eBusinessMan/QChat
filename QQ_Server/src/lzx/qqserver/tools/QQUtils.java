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
 * 工具类（用来实例化图片，单发信息，群发信息，格式化时间等）
 * @author luozhixiao
 *
 */
public class QQUtils {
	
	public static Icon serverstart = new ImageIcon("./images/serverstart.gif"); //开始服务
	
	public static Icon serverstop = new ImageIcon("./images/serverstop.gif"); //关闭服务
	
	/** 用于存QQ号，与线程socket */
	public static Map<String, ServerReceiveThread> threadMap = new HashMap<String, ServerReceiveThread>();
	
	/**
	 * 发给单人
	 */
	public static void sendTo(Object message, String id){
		
		ServerReceiveThread rt = threadMap.get(id);
		if (rt!=null)
			rt.sendMessage(message);
	}
	
	/**
	 * 发给所有人,对map进入遍历
	 * @param message 信息
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
	 * 关闭所有线程。
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

	
	
	/** 设置字体名称，样式，大小 */
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
	 * 格式化日期的方法
	 * @return String日期
	 */
	public static String formatDate(){
		
		String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return filename;
		
	}
	
	/**
	 * 格式化时间的方法
	 * @return String时间
	 */
	public static String formatTime(){
			
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
	}

}
