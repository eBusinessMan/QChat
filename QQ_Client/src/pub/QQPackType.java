package pub;

/**
 * 枚举类型 QQ包的类型。
 * 
 * @author luozhixiao
 * 
 */
public enum QQPackType {

	/** 登录包 */
	LOGIN,

	/** 登录成功。 */
	LOGIN_SUCCEESS,

	/** 登录失败。 */
	LOGIN_FAIL,

	/** 修改密码。 */
	UPDATE_PASSWORD,

	/** 公告。 */
	BULLETIN,

	/** 私聊 */
	PRIVATE_TALK,

	/** 群聊 */
	GROUP_CHAT,

	/** 强制下线包 */
	FORCE_DOWNLINE,

	/** 停止服务包 */
	STOP_SERVER,

	/** 在线用户包。 */
	ONLINE_LIST,

	/** 用户上线包。 */
	USER_LINE,

	/** 用户下线包 */
	USER_WODNLINE,
	
	/** 修改资料包 */
	UPDATE_INFO,
	

}
