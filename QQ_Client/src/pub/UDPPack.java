package pub;
import java.io.Serializable;
public class UDPPack implements Serializable
{
	/** QQ包类型(枚举) */
	private  String type;
	
	/**内容**/
	private Object content;

	/**发送者*/
	private String from;
	
	/**接收者*/
	private String to;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
    public  String toString()
   {
    return	this.getFrom()+":"+this.getTo()+":"+this.getContent()+":"+this.getType();   
   }

}
