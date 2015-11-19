package lzx.qqserver.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lzx.qqserver.logManger.LogPanel;




/**
 * ��־��ȡ�������
 *@author luozhixiao
 *
 */
public class LogDaoImpl {

	/** ��־��� */
	private LogPanel logPanel;

	/**
	 * ���췽��
	 */
	public LogDaoImpl() {

	}

	/**
	 * ���췽��
	 */
	public LogDaoImpl(final LogPanel logPanel) {

		this.logPanel = logPanel;
	}

	/**
	 * @param dateҪ�������־����
	 * 
	 * @param contentҪ������־������
	 * 
	 * @return ����ɹ�����null,���򷵻ش�����Ϣ
	 */
	public String addLog(String date, String content) {

		String str = "";
		String sql = "insert into t_log(logdate,content) values('"
				+ date + "','" + content + "')";
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
				str = "�����־ʧ��!";
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
	 * ��ѯ��־��Ϣ��ÿ�����־�����ı����ڡ�
	 * 
	 * @param date��־���ڣ������ڲ�ѯ
	 * @param key�ؼ��֣�ģ����ѯ��Ϊnull����""����ʹ�ø�������
	 */
	public String selectLog(String date, String key) {

		Connection conn = DBConnection.getConn();
		ResultSet rs = null;
		Statement stmt = null;
		if (conn != null) {
			try {

				// 3. ����SQL���
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
					
					logPanel.getHistoryArea().append(rs.getString("content")); // ����־�ļ�д���ı�����
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
