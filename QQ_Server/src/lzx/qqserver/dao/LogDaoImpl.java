package lzx.qqserver.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lzx.qqserver.logManger.LogPanel;




/**
 * 日志存取对象的类
 *@author luozhixiao
 *
 */
public class LogDaoImpl {

	/** 日志面板 */
	private LogPanel logPanel;

	/**
	 * 构造方法
	 */
	public LogDaoImpl() {

	}

	/**
	 * 构造方法
	 */
	public LogDaoImpl(final LogPanel logPanel) {

		this.logPanel = logPanel;
	}

	/**
	 * @param date要保存的日志日期
	 * 
	 * @param content要保存日志的内容
	 * 
	 * @return 保存成功返回null,否则返回错误信息
	 */
	public String addLog(String date, String content) {

		String str = "";
		String sql = "insert into t_log(logdate,content) values('"
				+ date + "','" + content + "')";
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
				str = "添加日志失败!";
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
	 * 查询日志信息。每天的日志放在文本框内。
	 * 
	 * @param date日志日期，按日期查询
	 * @param key关键字，模糊查询。为null或者""代表不使用该条件。
	 */
	public String selectLog(String date, String key) {

		Connection conn = DBConnection.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		if (conn != null) {
			try {

				// 3. 构造SQL语句
				stmt = conn.createStatement();

				StringBuffer strb = new StringBuffer(
						"select content from t_log where 1=1");
				
				// logdate=to_date('2009-04-08','yyyy-MM-dd')
				if(date!=null&&date.length()>0){
					strb.append(" and logdate='"+date+"'");
				}

				if (key != null && key.length() > 0) {
					strb.append(" and content like '%").append(key)
							.append("%'");
				}

				//strb.append("order by logdate");
				rs = stmt.executeQuery(strb.toString());

				while (rs.next()) {
					
					logPanel.getHistoryArea().append(rs.getString("content")); // 把日志文件写到文本框内
					logPanel.getHistoryArea().setCaretPosition(logPanel.getHistoryArea().getDocument().getLength());

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
		return null;
	}

}
