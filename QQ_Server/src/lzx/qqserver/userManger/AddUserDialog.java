package lzx.qqserver.userManger;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;

import lzx.qqserver.dao.DBConnection;
import lzx.qqserver.main.ServerFrame;



/**
 * 添加用户对话框
 * @author luozhixiao
 *
 */
public class AddUserDialog extends JDialog{
	
	private static final long serialVersionUID = 1855917317810197826L;
	
	/** 用户信息面板 */
	private UserInfoPanel userInfoPanel;
	/**
	 * 构造方法
	 */
	public AddUserDialog() {

		super(ServerFrame.getServerFrame());
		
		this.setLayout(new BorderLayout());
		userInfoPanel = new UserInfoPanel();	
		this.add(userInfoPanel,BorderLayout.CENTER);
		
		userInfoPanel.getIdField().setText(autoBulidID());
		//添加监听
		AddUserDialogListen addListen = new AddUserDialogListen(this);
		userInfoPanel.getSaveButton().addActionListener(addListen);
		userInfoPanel.getCancelButton().addActionListener(addListen);

		this.setModal(true);
		this.setTitle("添加用户");
		this.setResizable(false);
		this.setSize(300, 360);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	
	
	
	/**
	 * 自动生成用户编号的方法
	 * @return String 用户ID
	 */
	public String autoBulidID() {

		String sql = "select lpad(max(t.sid)+1,'5','0') from t_user t";
		String id = null;

		Connection conn = DBConnection.getConn();

		// 3. 构造SQL语句
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				id = rs.getString(1);
				if (id == null) {
					id = "00001";
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return id;

	}


	/**
	 * 获取用户信息面板方法
	 * @return UserInfoPanel 用户信息面板
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}

}
