package lzx.qqserver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * �����������ݵ���
 * @author luozhixiao
 *
 */
public class DBConnection {
	
	/**
	 * �û��������ݵķ���
	 * @return Connection �������ݿ������ͨ��
	 */

	public static Connection getConn() {

		Connection conn = null;
		try {
			// 1. ע��ͼ������ݿ���������
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

			// 2. ���������ݿ������ͨ��
			String url = "jdbc:microsoft:sqlserver://localhost:1433;databasename=EnPQQ";
			String user = "sa";
			String password = "";
			conn = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {
		//	JOptionPane.showMessageDialog(parentComponent, "���ݿ�Ϊ����", "���ݿ�������ʾ", 1);
			e.printStackTrace();
		}
		
		// 5. �ر���Դ

		return conn;

	}

}
