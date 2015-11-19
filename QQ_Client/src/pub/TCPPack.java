package pub;

import java.io.Serializable;

/**
 * TCP包；类
 * @author luozhixiao
 *
 */
public class TCPPack implements Serializable {
	
	private static final long serialVersionUID = 2518498132886233883L;

	/** QQ包类型(枚举) */
	
    
	private QQPackType type; 
	/**来自*/
	private String from;
	

	/** 目标地址 */
	private String to;

	/** 发送的内容 */
	private Object content;
    
	/**用户的信息*/
	private  User  user;
	
	/**获取发送者的信息*/
	public User getUser() {
		return user;
	}

	/**设置发送者的信息*/
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取发送的内容
	 * @return Object 发送对象
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * 设置发送内容
	 * @param content 发送内容
	 */
	public void setContent(Object content) {
		this.content = content;
	}


	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}
    
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 获取QQ包类型
	 * @return String QQ包类型
	 */
	public QQPackType getType() {
		return type;
	}

	/**
	 * 设置QQ包类型
	 * @param type QQ包类型
	 */
	public void setType(QQPackType type) {
		this.type = type;
	}
}
