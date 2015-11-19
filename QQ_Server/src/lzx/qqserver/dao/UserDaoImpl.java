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
 * �û������ݴ洢������
 * 
 * @author luozhixiao
 * @version 1.0
 */
public class UserDaoImpl implements UserDao {

	/**
	 * ��ѯ�û���Ϣ��ÿ���û�������һ��Vector�������棬��ЩVector�����ַ���һ��Vector���� �������ʾ�á�
	 * 
	 * @param id�û���ţ���ȷ��ѯ��Ϊnull����""����ʹ�ø�������
	 * 
	 * @param name�û�����ģ����ѯ��Ϊnull����""����ʹ�ø�������
	 * 
	 * @praram isonline ״̬��Ϊ-1��ʾ��ʹ�ø�������
	 * @return ��������������û���Vector����
	 */
	public Vector<Vector<String>> selectUser(String id, String name,
			int isonline) {

		Vector<Vector<String>> data = new Vector<Vector<String>>();

		Connection conn = DBConnection.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		if (conn != null) {
			try {

				// 3. ����SQL���
				stmt = conn.createStatement();

				/*StringBuffer strb = new StringBuffer(
						"select sid,sname,spassword,(sdecodesex,'0','��','1','Ů','����') as ssex,"
								+ "nage,saddress,decode(nisonline,'0','������','1','����','δ֪') as nisonline,"
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
				// 4. ִ��SQL���(���ؽ����)
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
	 * ȡ�����û��б�
	 * 
	 * @return ��������û��б�
	 */
	public List<User> getOnlineUser() {

		List<User> li = new ArrayList<User>();
		Connection conn = DBConnection.getConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			/*String strb = "select sid,sname,decode(ssex,'0','��','1','Ů','����') as ssex,nage,"
					+ "saddress,decode(nisonline,'0','������','1','����','δ֪') as nisonline,"
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
				user.setSsex("��");break;
				case '1':
				user.setSsex("Ů");break;
				default:
				user.setSsex("����");
				}
				user.setNage(rs.getString("nage"));
				user.setSaddress(rs.getString("saddress"));
				switch(Integer.parseInt(rs.getString("nisonline")))
				{
				case '0':
				user.setNisonline("������");break;
				case '1':
				user.setNisonline("����");break;
				default:
			    user.setNisonline("δ֪");
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
	 * ��user��������ݱ��浽�洢����
	 * 
	 * @param user
	 *            Ҫ������û�����
	 * 
	 * @return ����ɹ�����null,���򷵻ش�����Ϣ
	 */
	public String addUser(User user) {

		String str = "";
		String sql = "insert into t_user(sid,sname,spassword,ssex,nage,saddress) values(?,?,?,?,?,?)";

		Connection conn = DBConnection.getConn();
		if (conn != null) {
			PreparedStatement ps = null;
			try {
				// 3. ����SQL���
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
				str = "����û�ʧ��!";
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
	 * �޸��û�
	 * 
	 * @param newUser
	 *            ���ĺ�����û���Ϣ
	 * @return ���³ɹ��򷵻�null�����򷵻ش�����Ϣ
	 */
	public String updateUser(User newUser) {

		String str = "";
		String sql = "update t_user set sname=?,spassword=?,ssex=?,nage=?,saddress=? where sid=?";
		Connection conn = DBConnection.getConn();

		if (conn != null) {

			PreparedStatement pres = null;
			try {

				// 3. ����SQL���
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
				str = "�޸��û�ʧ��!";
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
	 * ɾ��ָ���û�
	 * 
	 * @param id
	 *            ��ɾ�����û��ı��
	 * @return ɾ���ɹ��򷵻�null,���򷵻ش�����Ϣ
	 */
	public String deleteUser(String id) {

		String str = "";
		String sql = "delete from t_user where sid=" + id;

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. ����SQL���
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "ɾ���û�ʧ��!";
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
	 * ��֤�û�
	 * 
	 * @param id�û����
	 * 
	 * @param password����
	 * 
	 * @return ��֤ͨ������User���󣬷��򷵻ش�����Ϣ��
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
				if (rs.next()) // ����������Ϊ�ա�
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
						case 0:user.setNisonline("������");break;
						case 1:user.setNisonline("����");break;
						default: user.setNisonline("δ֪");
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
				return "SQL������!"; 
			}
		}

		return user;
	}

	/**
	 * �����û���״̬������/�����ߣ�
	 * 
	 * @param id
	 *            Ҫ����״̬���û��ı��
	 * @param online
	 *            ����/������
	 * @return ���³ɹ��򷵻�null�����򷵻ش�����Ϣ
	 */
	public String updateOnline(String id, boolean online) {

		String str = "";
		String sql = null;

		if (online == true) {// �ı��û�״̬,�������,��Ϊ������,��֮,����
			sql = "update t_user set nisonline = 1 where sid=" + id;

		} else {
			sql = "update t_user set nisonline = 0 where sid=" + id;
		}
		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. ����SQL���
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "�����û�״̬ʧ��!";
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
	 * ���������û�״̬Ϊ������
	 * 
	 * @return ���óɹ�����null,���򷵻ش�����Ϣ
	 */
	public String setAllOffline() {

		String str = "";
		String sql = "update t_user set nisonline = 0";

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. ����SQL���
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "�����û�״̬Ϊ������ʧ��!";
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
	 * �޸��û�����
	 * 
	 * @param id
	 *            Ҫ�޸ĵ��û����
	 * @param oldPwd
	 *            ԭ����
	 * @param newPwd
	 *            ������
	 * @return ���óɹ�����null,���򷵻ش�����Ϣ
	 */
	public String updatePassword(String id, String oldPwd, String newPwd) {

		String str = "";

		String sql;
		if (id == null) {// Ϊ�ո�����������
			sql = "update t_user set spassword=" + newPwd;
		} else {
			sql = "update t_user set spassword=" + newPwd + " where sid=" + id;
		}

		Connection conn = DBConnection.getConn();
		if (conn != null) {

			Statement stmt = null;
			try {
				// 3. ����SQL���
				stmt = conn.createStatement();
				int a = stmt.executeUpdate(sql);
				if (a > 0) {
					str = null;

				}

			} catch (SQLException e) {
				str = "ɾ���û�ʧ��!";
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
	 * �ر���Դ
	 */
	public void close() {
	}
}
