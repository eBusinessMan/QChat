package lzx.qqserver.dao;


import java.util.List;
import java.util.Vector;

import pub.User;

/**
 * 用户的数据存储对象
 * 
 * @author luozhixiao
 * @version 1.0
 */

public interface UserDao {
	
	/**
	 * 查询用户信息。每个用户都放在一个Vector对象里面，这些Vector对象又放在一个Vector对象。
	 * 供表格显示用。
	 * @param id 用户编号，精确查询。为null或者""代表不使用该条件。
	 * @param name 用户名，模糊查询。为null或者""代表不使用该条件。
	 * @praram isonline 状态。为-1表示不使用该条件。
	 * @return 存放满足条件的用户的Vector对象。
	 */
	public Vector<Vector<String>> selectUser(String id, String name, int isonline);
	
	/**
	 * 取在线用户列表。
	 * @return 存放所有用户列表。
	 */
	public List<User> getOnlineUser();
	
	/**
	 * 把user对象的数据保存到存储介质
	 * @param user 要保存的用户对象
	 * @return 保存成功返回null,否则返回错误信息
	 */
	public String addUser(User user);
	
	/**
	 * 修改用户
	 * @param newUser 更改后的新用户信息
	 * @return 更新成功则返回null，否则返回错误信息
	 */
	public String updateUser(User newUser);
	
	/**
	 * 删除指定用户
	 * @param id 用删除的用户的编号
	 * @return 删除成功则返回null,否则返回错误信息
	 */
	public String deleteUser(String id);
	
	
	/**
	 * 验证用户
	 * @param id 用户编号
	 * @param password 密码
	 * @return 验证通过返回User对象，否则返回错误信息。
	 */
	public Object checkUser(String id, String password);
	
	
	/**
	 * 更新用户的状态（在线/不在线）
	 * @param id 要更改状态的用户的编号
	 * @param online 在线/不在线
	 * @return 更新成功则返回null，否则返回错误信息
	 */
	public String updateOnline(String id, boolean online);
	
	/**
	 * 设置所有用户状态为不在线
	 * @return 设置成功返回null,否则返回错误信息
	 */
	public String setAllOffline();
	
	/**
	 * 修改用户密码
	 * @param id 要修改的用户编号
	 * @param oldPwd 原密码
	 * @param newPwd 新密码
	 * @return 设置成功返回null,否则返回错误信息
	 */
	public String updatePassword(String id, String oldPwd, String newPwd);
	
	/**
	 * 关闭资源
	 */
	public void close();

}
