package lzx.qqclient.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import lzx.qqclient.chat.ChatFrame;



/**
 * 读写聊天记录类
 * @author luozhixiao
 *
 */

public class ReadWriteLog {
	

	/**
	 * 把聊天记录写入文件的方法
	 * @param id 用户ID
	 * @param content 要保存的聊天记录
	 */
	public static void writeLog(String id,String content){
		
		try {
			File file = new File("./log/"+id+"/"+id+".log");
			
			FileWriter fw = new FileWriter(file,true);
			
			BufferedWriter br= new BufferedWriter(fw);
			
			if(!file.exists()){//如果文件不存在
				file.createNewFile();
				
				
					br.append(content);
					br.close();
				
				
			}else{
				br.append(content);
				br.close();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * 读聊天记录，并把聊天记录写入聊天记录面板的方法
	 * @param id 用户ID
	 * @param frame 聊天主界面
	 */
	public  static void readLog(String id,ChatFrame frame){
		
		try {
			FileReader fr = new FileReader("./log/"+id+"/"+id+".log");
			BufferedReader br = new BufferedReader(fr);
			
			String record = null;
			while(true){
				record=br.readLine();
				if(record==null){
					break;
				}
				frame.getChatLogArea().append(record+"\n");
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
