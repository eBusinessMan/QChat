package lzx.qqclient.business;
import javax.swing.*;

import pub.TCPPack;
import pub.UDPPack;

import java.io.*;
import java.net.*;
import java.lang.Exception;
public class ChatUtils 
{  
	
	   /**���͵�IP��ַ*/
	  private  String sip; 
	  /**���͵Ķ˿ں�*/
	  private  int sport;
	  
	  /**UDP�ֽ�����*/
	  private  byte b[];  
	  /**TCP ���ݰ��׽��� */
	  private  Socket socket;
	  /**UDP ���ݰ��׽���*/
	  private  DatagramSocket ds; 
	  /**�Զ���TCP������Ϣ��*/
	  private  TCPPack tcpPack;
	  
	  /**�Զ���UDP������Ϣ��*/
	  private  UDPPack udpPack;
	  /**д���ֽ���*/
	  private  BufferedWriter dos;
	  /**�����ֽ���*/
	  public ChatUtils(String ip,int port)
	  {
		this.setSip(ip);
		this.setSport(port);
	  }
	  private  BufferedReader dis;
	      public   void sendUdp(){//����UDP
			try{
				String str=udpPack.toString();//��UDP��ת��Ϊ�ַ���
				DatagramPacket f=new DatagramPacket(str.getBytes(),str.getBytes().length,
				InetAddress.getByName(sip),sport);
				ds=new DatagramSocket();
				ds.send(f);}catch(Exception e){}
				}
      
		  public  UDPPack recieveUdp()throws Exception{//����UDP��
		       	b=new byte[1024];
		       	DatagramPacket  f1=new DatagramPacket(b,1024);
				ds.receive(f1);	
			    String str=new String(f1.getData());
			    udpPack=toUDPPack(str);//�����յ����ַ��������UDP��
				return  udpPack;
				}			
	      public  void sendObject()throws Exception{//����TCP
		        socket=new Socket(sip,sport);
			    ObjectOutputStream op=new ObjectOutputStream(socket.getOutputStream());
			    op.writeObject(tcpPack);
			    }
          public  void createFile(String fileName)throws Exception{ 
		        dos=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Documents and Settings\\Administrator\\����\\"+fileName)));
		       }
		  public  void writeFile(String str)throws Exception{
		        dos.write(str);
		        dos.flush();
		       }
		  public  void queryFile(String fileName)throws Exception{
			    dis=new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		       }
		  public  String  readFile()throws Exception{
		       return dis.readLine();
		       }				
		  public   TCPPack readObject(){
		  try{
			  ObjectInputStream oi=new ObjectInputStream(socket.getInputStream());
			  tcpPack=(TCPPack)oi.readObject();
			  return tcpPack;
		      }catch(Exception e){try{socket.close();}catch(Exception e1){JOptionPane.showMessageDialog(null,"��������Ͽ�����","����",JOptionPane.ERROR_MESSAGE);};return null;}
		     }
		  	  
		  public  TCPPack getTcpPack() {
				return tcpPack;
			}
		  public  void setTcpPack(TCPPack tcpPack) {
				this.tcpPack = tcpPack;
			}
		  public  UDPPack getUdpPack() {
				return udpPack;
			}
		  public  void setUdpPack(UDPPack udpPack) {
				this.udpPack = udpPack;
			}
		  public String getSip() {
			return sip;
		    }

		  public void setSip(String sip) {
			this.sip = sip;
		    }

		  public int getSport() {
			return sport;
		    }

		  public void setSport(int sport) {
			this.sport = sport;
		    }
           
		  public Socket getSocket() {
			return socket;
		    }
		  public  UDPPack toUDPPack(String str) {
				int index=str.indexOf(":");
				udpPack=new UDPPack();
				udpPack.setFrom(str.substring(0,index));
				str=str.substring(index+1);
				index=str.indexOf(":");
				udpPack.setTo(str.substring(0,index));
				str=str.substring(index+1);
				index=str.indexOf(":");
				udpPack.setContent(str.substring(0,index));
                udpPack.setType(str.substring(index+1));
				return udpPack;
		  }  
}
