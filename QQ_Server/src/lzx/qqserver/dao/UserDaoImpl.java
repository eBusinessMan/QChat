package lzx.qqserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import pub.User;


/**
 * 用户的数据存储对象类
 * 
 * @author luozhixiao
 * @version 1.0
 */
public class UserDaoImpl implements UserDao {

	/**
	 * 查询用户信息。每个用户都放在一个Vector对象里面，这些Vector对象又放在一个Vector对象。 供表格显示用。
	 * 
	 * @param id用户编号，精确查询。为null或者""代表不使用该条件。
	 * 
	 * @param name用户名，模糊查询。为null或者""代表不使用该条件。
	 * 
	 * @praram isonline 状态。为-1表示不使用该条件。
	 * @return 存放满足条件的用户的Vector对象。
	 */
	public Vector<Vector<String>> selectUser(String id, String name,
			int isonline) {

		Vector<Vector<String>> data = new Vector<Vector<String>>();

		Connection conn = DBConnection.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		if (conn != null) {
			try {

				// 3. 构造SQL语句
				stmt = conn.createStatement();

				/*StringBuffer strb = new StringBuffer(
						"select sid,sname,spassword,(sdecodesex,'0','男','1','女','保密') as ssex,"
								+ "nage,saddress,decode(nisonline,'0','不在线','1','在线','未知') as nisonline,"
								+ "dregtime from t_user where 1=1");*/

				StringBuffer strb = new StringBuffer(
						"select sid,sname,spassword, ssex,"
								+ "nage,saddress,nisonline,"
								+ "dregtime from t_user where 1=1");
				if (id != null && id.length() > 0) {
					strb.append("and sid = ").append(id);
				}

				if (name != null && name.length() > 0) {
					strb.append(" and sname like '%").append(name).append("%'");
				}

				if (isonline != -1) {
					strb.append(" and nisonline = ").append(isonline);
				}
				strb.append("order by sid");
				// 4. 执行SQL语句(返回结果集)
				rs = stmt.executeQuery(strb.toString());

				while (rs.next()) {
					Vector<String> row = new Vector<String>();

					row.add(rs.getString("sid"));
					row.add(rs.getString("sname"));
					row.add(rs.getString("spassword"));
					row.add(rs.getString("ssex"));
					row.add(rs.getString("nage"));
					row.add(rs.getString("saddress"));
					row.add(rs.getString("nisonline"));
					row.add(rs.getString("dregtime"));
					data.add(row);
				}

			} catch (SQLException e) {

				e.printStackTrace();
			} finally {

				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}

		return data;

	}

	/**
	 * 取在线用户列表。
	 * 
	 * @return 存放所有用户列表。
	 */
	public List<User> getOnlineUser() {

		List<User> li = new ArrayList<User>();
		Connection conn = DBConnection.getConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			/*String strb = "select sid,sname,decode(ssex,'0','男','1','女','保密') as ssex,nage,"
					+ "saddress,decode(nisonline,'0','不在线','1','在线','未知') as nisonline,"
					+ "dregtime from t_user where nisonline = 1";*/
			String strb = "select sid,sname,ssex,nage,"
			+ "saddress,nisonline,"
			+ "dregtime from t_user where nisonline = 1";
			rs = stmt.executeQuery(strb);

			while (rs.next()) {
				User user = new User();
				user.setSid(rs.getString("sid"));
				user.setSname(rs.getString("sname"));
				switch(Integer.parseInt(rs.getString("ssex")))
				{
				case '0':
				user.setSsex("男");break;
				case '1':
				user.setSsex("女");break;
				default:
				user.setSsex("保密");
				}
				user.setNage(rs.getString("nage"));
				user.setSaddress(rs.getString("saddress"));
				switch(Integer.parseInt(rs.getString("nisonline")))
				{
				case '0':
				user.setNisonline("不在线");break;
				case '1':
				user.setNisonline("在线");break;
				default:
			    user.setNisonline("未知");
				}
				user.setDregTime(rs.getString("dregtime"));
				li.add(user);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return li;

	}

	/**
	 * 把user对象的数据保存到存储介质
	 * 
	 * @param user
	 *            要保存的用户对象
	 * 
	 * @return 保存成功返回null,否则返回错误信息
	 */
	public String addUser(User user) {

		String str = "";
		String sql = "insert into t_user(sid,sname,spassword,ssex,nage,saddress) values(?,?,?,?,?,?)";

		Connection conn = DBConnection.getConn();
		if (conn != null) {
			PreparedStatement ps = null;
			try {
				// 3. 构造SQL语句
				ps = conn.prepareStatement(sql);
				ps.setString(1, user.getSid());
				ps.setString(2, user.getSname());
				ps.setString(3, user.getSpassword());
				ps.setString(4, user.getSsex());
				ps.setString(5, user.getNage());
				ps.setString(6, user.getSaddress());

				int a = ps.executeUpdate();
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "添加用户失败!";
				e.printStackTrace();
			} finally {

				try {
					ps.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;

	}

	/**
	 * 修改用户
	 * 
	 * @param newUser
	 *            更改后的新用户信息
	 * @return 更新成功则返回null，否则返回错误信息
	 */
	public String updateUser(User newUser) {

		String str = "";
		String sql = "update t_user set sname=?,spassword=?,ssex=?,nage=?,saddress=? where sid=?";
		Connection conn = DBConnection.getConn();

		if (conn != null) {

			PreparedStatement pres = null;
			try {

				// 3. 构造SQL语句
				pres = conn.prepareStatement(sql);
				pres.setString(1, newUser.getSname());
				pres.setString(2, newUser.getSpassword());
				pres.setString(3, newUser.getSsex());
				pres.setString(4, newUser.getNage());
				pres.setString(5, newUser.getSaddress());
				pres.setString(6, newUser.getSid());

				int a = pres.executeUpdate();
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "修改用户失败!";
				e.printStackTrace();
			} finally {

				try {
					pres.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;
	}

	/**
	 * 删除指定用户
	 * 
	 * @param id
	 *            用删除的用户的编号
	 * @return 删除成功则返回null,否则返回错误信息
	 */
	public String deleteUser(String id) {

		String str = "";
		String sql = "delete from t_user where sid=" + id;

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. 构造SQL语句
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "删除用户失败!";
				e.printStackTrace();
			} finally {

				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;
	}

	/**
	 * 验证用户
	 * 
	 * @param id用户编号
	 * 
	 * @param password密码
	 * 
	 * @return 验证通过返回User对象，否则返回错误信息。
	 */
	public Object checkUser(String id, String password) {
		User user = null;

		String sql = "select sid,sname,spassword,ssex,nage,saddress,nisonline,dregtime,sip,sport,soffmessage,sheadurl,sqqshowurl from t_user where sid = '"
				+ id + "'";
		String sql1 ="select spassword from t_user where sid='"+id+ "'";
		Connection conn = DBConnection.getConn();
		if (conn != null) {
			Statement stmt = null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql1);
				if (rs.next()) // 如果结果集不为空。
				{
				 if(rs.getString("spassword").equals(password))
				 {
					rs1 = stmt.executeQuery(sql);
					if (rs1.next()) {
						user = new User();
						user.setSid(rs1.getString("sid"));
						user.setSname(rs1.getString("sname"));
						user.setSpassword(rs1.getString("spassword"));
						user.setSsex(rs1.getString("ssex"));
						user.setNage(rs1.getString("nage"));
						user.setSaddress(rs1.getString("saddress"));
						user.setNisonline(rs1.getInt("nisonline")+"");
						user.setSip(rs1.getString("sip"));
						user.setSport(rs1.getInt("sport"));
						user.setSoffMessage(rs1.getString("soffMessage"));
						user.setSheadURL(rs1.getString("sheadURL"));
						user.setSqShowURL(rs1.getString("sqShowURL"));
						/*switch(rs1.getInt("nisonline"))
						{
						case 0:user.setNisonline("不在线");break;
						case 1:user.setNisonline("在线");break;
						default: user.setNisonline("未知");
						}*/
						user.setDregTime(rs1.getString("dregtime"));
					}
				 }	
				else{
						return "PASSWORD";
					}                    
				} else {
					return "ID";
				}
			} catch (SQLException e) {
				return "SQL语句错误!"; 
			}
		}

		return user;
	}

	/**
	 * 更新用户的状态（在线/不在线）
	 * 
	 * @param id
	 *            要更改状态的用户的编号
	 * @param online
	 *            在线/不在线
	 * @return 更新成功则返回null，否则返回错误信息
	 */
	public String updateOnline(String id, boolean online) {

		String str = "";
		String sql = null;

		if (online == true) {// 改变用户状态,如果在线,改为不在线,反之,在线
			sql = "update t_user set nisonline = 1 where sid=" + id;

		} else {
			sql = "update t_user set nisonline = 0 where sid=" + id;
		}
		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. 构造SQL语句
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "设置用户状态失败!";
				e.printStackTrace();
			} finally {

				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;
	}

	/**
	 * 设置所有用户状态为不在线
	 * 
	 * @return 设置成功返回null,否则返回错误信息
	 */
	public String setAllOffline() {

		String str = "";
		String sql = "update t_user set nisonline = 0";

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. 构造SQL语句
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "设置用户状态为不在线失败!";
				e.printStackTrace();
			} finally {

				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 *            要修改的用户编号
	 * @param oldPwd
	 *            原密码
	 * @param newPwd
	 *            新密码
	 * @return 设置成功返回null,否则返回错误信息
	 */
	public String updatePassword(String id, String oldPwd, String newPwd) {

		String str = "";

		String sql;
		if (id == null) {// 为空更新所有密码
			sql = "update t_user set spassword=" + newPwd;
		} else {
			sql = "update t_user set spassword=" + newPwd + " where sid=" + id;
		}

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. 构造SQL语句
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "删除用户失败!";
				e.printStackTrace();
			} finally {

				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}

			}

		}
		return str;

	}

	/**
	 * 关闭资源
	 */
	public void close() {
	}
}
