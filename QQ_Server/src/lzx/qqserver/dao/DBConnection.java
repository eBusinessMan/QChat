package lzx.qqserver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 用于连接数据的类
 * @author luozhixiao
 *
 */
public class DBConnection {
	
	/**
	 * 用户连接数据的方法
	 * @return Connection 返回数据库的连接通道
	 */

	public static Connection getConn() {

		Connection conn = null;
		try {
			// 1. 注册和加载数据库驱动程序
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

			// 2. 建立与数据库的连接通道
			String url = "jdbc:microsoft:sqlserver://localhost:1433;databasename=EnPQQ";
			String user = "sa";
			String password = "";
			conn = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {
		//	JOptionPane.showMessageDialog(parentComponent, "数据库为启动", "数据库连接提示", 1);
			e.printStackTrace();
		}
		
		// 5. 关闭资源

		return conn;

	}

}
