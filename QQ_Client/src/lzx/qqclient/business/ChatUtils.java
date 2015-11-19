package lzx.qqclient.business;
import javax.swing.*;

import pub.TCPPack;
import pub.UDPPack;

import java.io.*;
import java.net.*;
import java.lang.Exception;
public class ChatUtils 
{  
	
	   /**发送的IP地址*/
	  private  String sip; 
	  /**发送的端口号*/
	  private  int sport;
	  
	  /**UDP字节数组*/
	  private  byte b[];  
	  /**TCP 数据包套接字 */
	  private  Socket socket;
	  /**UDP 数据包套接字*/
	  private  DatagramSocket ds; 
	  /**自定义TCP网络信息包*/
	  private  TCPPack tcpPack;
	  
	  /**自定义UDP网络信息包*/
	  private  UDPPack udpPack;
	  /**写出字节流*/
	  private  BufferedWriter dos;
	  /**读入字节流*/
	  public ChatUtils(String ip,int port)
	  {
		this.setSip(ip);
		this.setSport(port);
	  }
	  private  BufferedReader dis;
	      public   void sendUdp(){//发送UDP
			try{
				String str=udpPack.toString();//将UDP包转化为字符串
				DatagramPacket f=new DatagramPacket(str.getBytes(),str.getBytes().length,
				InetAddress.getByName(sip),sport);
				ds=new DatagramSocket();
				ds.send(f);}catch(Exception e){}
				}
      
		  public  UDPPack recieveUdp()throws Exception{//接收UDP包
		       	b=new byte[1024];
		       	DatagramPacket  f1=new DatagramPacket(b,1024);
				ds.receive(f1);	
			    String str=new String(f1.getData());
			    udpPack=toUDPPack(str);//将接收到的字符串构造成UDP包
				return  udpPack;
				}			
	      public  void sendObject()throws Exception{//发送TCP
		        socket=new Socket(sip,sport);
			    ObjectOutputStream op=new ObjectOutputStream(socket.getOutputStream());
			    op.writeObject(tcpPack);
			    }
          public  void createFile(String fileName)throws Exception{ 
		        dos=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Documents and Settings\\Administrator\\桌面\\"+fileName)));
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
		      }catch(Exception e){try{socket.close();}catch(Exception e1){JOptionPane.showMessageDialog(null,"与服务器断开连接","错误",JOptionPane.ERROR_MESSAGE);};return null;}
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
