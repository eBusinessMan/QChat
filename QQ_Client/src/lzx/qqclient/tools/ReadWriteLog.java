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
 * ��д�����¼��
 * @author luozhixiao
 *
 */

public class ReadWriteLog {
	

	/**
	 * �������¼д���ļ��ķ���
	 * @param id �û�ID
	 * @param content Ҫ����������¼
	 */
	public static void writeLog(String id,String content){
		
		try {
			File file = new File("./log/"+id+"/"+id+".log");
			
			FileWriter fw = new FileWriter(file,true);
			
			BufferedWriter br= new BufferedWriter(fw);
			
			if(!file.exists()){//����ļ�������
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
	 * �������¼�����������¼д�������¼���ķ���
	 * @param id �û�ID
	 * @param frame ����������
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
