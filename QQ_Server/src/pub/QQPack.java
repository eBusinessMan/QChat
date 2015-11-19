package pub;

import java.io.Serializable;

/**
 * QQ������
 * @author luozhixiao
 *
 */
public class QQPack implements Serializable {
	
	private static final long serialVersionUID = 2518498132886233883L;

	/** QQ������(ö��) */
	private QQPackType type;

	/** �������� */
	private String from;

	/** �������� */
	private String to;

	/** ���͵����� */
	private Object content;

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

	/**
	 * ��ȡ�������� 
	 * @return String ��������
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * ���÷������� 
	 * @return String ��������
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * ��ȡ�������� 
	 * @return String ��������
	 */
	public String getTo() {
		return to;
	}

	/**
	 * ���÷������� 
	 * @return String ��������
	 */
	public void setTo(String to) {
		this.to = to;
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
