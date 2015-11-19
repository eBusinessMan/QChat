package lzx.qqclient.tools;

import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 工具类 用来初始化图片，字体，时间日期等
 * @author luozhixiao
 *
 */

public class QQUtils {
	
	private static SystemTray tray;
	
	private static TrayIcon trayIcon;
	
	public static ImageIcon qqbanner = new ImageIcon("./image/qqbanner.jpg");//QQ2009图片

	public static Image qqicon = Toolkit.getDefaultToolkit().createImage("./image/qqicon.gif");// QQ登录小图标

	public static Icon normal = new ImageIcon("./image/mousenormal.gif"); //按钮图标

	public static Icon over = new ImageIcon("./image/login.jpg"); //鼠标移入时候按钮显示的图标
		
	public static Icon setfont = new ImageIcon("./image/setfont.gif"); //字体	
	
	public static Icon setfontover = new ImageIcon("./image/setfontover.gif"); //字体
	
	public static Icon setface = new ImageIcon("./image/setface.gif"); //表情 
	
	public static Icon setfaceover = new ImageIcon("./image/setfaceover.gif"); //表情 
	


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
		
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
		
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
	
	/**
	 * 格式化日期的方法
	 * @return String日期
	 */
	public static String formatT(){/*日期格式化*/
		
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
		
	}
	public static void initSystemTray(){/* 初始化托盘图标*/
		
	   	    tray=SystemTray.getSystemTray();
	   	    TrayMenu  menu=new TrayMenu();
	        trayIcon=new TrayIcon(QQUtils.qqicon,"QQ",menu);
	    	if(SystemTray.isSupported())
	       	{  
	         try{	
	    	    tray.add(trayIcon);
	            trayIcon.addActionListener(menu);
	            }catch(Exception e1){e1.printStackTrace();}
	        }
	       	else 
	       	System.exit(1);
	 } 
}
