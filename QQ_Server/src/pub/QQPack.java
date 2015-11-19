package pub;

import java.io.Serializable;

/**
 * QQ包；类
 * @author luozhixiao
 *
 */
public class QQPack implements Serializable {
	
	private static final long serialVersionUID = 2518498132886233883L;

	/** QQ包类型(枚举) */
	private QQPackType type;

	/** 发自哪里 */
	private String from;

	/** 发到哪里 */
	private String to;

	/** 发送的内容 */
	private Object content;

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

	/**
	 * 获取发自那里 
	 * @return String 发自那里
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * 设置发自那里 
	 * @return String 发自那里
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 获取发到那里 
	 * @return String 发自那里
	 */
	public String getTo() {
		return to;
	}

	/**
	 * 设置发到那里 
	 * @return String 发自那里
	 */
	public void setTo(String to) {
		this.to = to;
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
