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
 * ������ ������ʼ��ͼƬ�����壬ʱ�����ڵ�
 * @author luozhixiao
 *
 */

public class QQUtils {
	
	private static SystemTray tray;
	
	private static TrayIcon trayIcon;
	
	public static ImageIcon qqbanner = new ImageIcon("./image/qqbanner.jpg");//QQ2009ͼƬ

	public static Image qqicon = Toolkit.getDefaultToolkit().createImage("./image/qqicon.gif");// QQ��¼Сͼ��

	public static Icon normal = new ImageIcon("./image/mousenormal.gif"); //��ťͼ��

	public static Icon over = new ImageIcon("./image/login.jpg"); //�������ʱ��ť��ʾ��ͼ��
		
	public static Icon setfont = new ImageIcon("./image/setfont.gif"); //����	
	
	public static Icon setfontover = new ImageIcon("./image/setfontover.gif"); //����
	
	public static Icon setface = new ImageIcon("./image/setface.gif"); //���� 
	
	public static Icon setfaceover = new ImageIcon("./image/setfaceover.gif"); //���� 
	


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
		
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
		
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
	
	/**
	 * ��ʽ�����ڵķ���
	 * @return String����
	 */
	public static String formatT(){/*���ڸ�ʽ��*/
		
		Date myDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String date = format.format(myDate);
		return date;
		
	}
	public static void initSystemTray(){/* ��ʼ������ͼ��*/
		
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
