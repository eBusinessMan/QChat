package pub;

import java.io.Serializable;

/**
 * TCP������
 * @author luozhixiao
 *
 */
public class TCPPack implements Serializable {
	
	private static final long serialVersionUID = 2518498132886233883L;

	/** QQ������(ö��) */
	
    
	private QQPackType type; 
	/**����*/
	private String from;
	

	/** Ŀ���ַ */
	private String to;

	/** ���͵����� */
	private Object content;
    
	/**�û�����Ϣ*/
	private  User  user;
	
	/**��ȡ�����ߵ���Ϣ*/
	public User getUser() {
		return user;
	}

	/**���÷����ߵ���Ϣ*/
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * ��ȡ���͵�����
	 * @return Object ���Ͷ���
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * ���÷�������
	 * @param content ��������
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
	 * ��ȡQQ������
	 * @return String QQ������
	 */
	public QQPackType getType() {
		return type;
	}

	/**
	 * ����QQ������
	 * @param type QQ������
	 */
	public void setType(QQPackType type) {
		this.type = type;
	}
}
