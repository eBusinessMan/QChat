package pub;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * @author luozhixiao
	 */
	private static final long serialVersionUID = -625767084241463439L;

	/** ID */
	private String sid;

	/** 姓名 */
	private String sname;

	/** 密码 */
	private String spassword;

	/** 性别 */
	private String ssex;

	/** 年龄 */
	private String nage;

	/** 地址 */
	private String saddress;

	/** 是否在线  */
	private String nisonline;

	/** 注册时间 */
	private String dregTime;
	
	/** IP*/
	private String sip;
	
	/** port*/
	private int sport;
	
	/** 离线消息*/
	private String soffMessage;
	
	/** 安全码 用于密码修改*/
	private String ssafeNum;
	
	/**头像路径*/
	private String sheadURL;
	
	/**QQ秀路径*/
	private String sqShowURL;
	
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

	public String getSoffMessage() {
		return soffMessage;
	}

	public void setSoffMessage(String soffMessage) {
		this.soffMessage = soffMessage;
	}

	public String getSsafeNum() {
		return ssafeNum;
	}

	public void setSsafeNum(String ssafeNum) {
		this.ssafeNum = ssafeNum;
	}

	public String getSheadURL() {
		return sheadURL;
	}

	public void setSheadURL(String sheadURL) {
		this.sheadURL = sheadURL;
	}

	public String getSqShowURL() {
		return sqShowURL;
	}

	public void setSqShowURL(String sqShowURL) {
		this.sqShowURL = sqShowURL;
	}

	public String getDregTime() {
		return dregTime;
	}

	public void setDregTime(String dregTime) {
		this.dregTime = dregTime;
	}

	public String getNage() {
		return nage;
	}

	public void setNage(String nage) {
		this.nage = nage;
	}

	public String getNisonline() {
		return nisonline;
	}

	public void setNisonline(String nisonline) {
		this.nisonline = nisonline;
	}

	public String getSaddress() {
		return saddress;
	}

	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSpassword() {
		return spassword;
	}

	public void setSpassword(String spassword) {
		this.spassword = spassword;
	}

	public String getSsex() {
		return ssex;
	}

	public void setSsex(String ssex) {
		this.ssex = ssex;
	}
}
