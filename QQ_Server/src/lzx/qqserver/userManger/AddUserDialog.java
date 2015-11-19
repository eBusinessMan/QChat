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
 * ����û��Ի���
 * @author luozhixiao
 *
 */
public class AddUserDialog extends JDialog{
	
	private static final long serialVersionUID = 1855917317810197826L;
	
	/** �û���Ϣ��� */
	private UserInfoPanel userInfoPanel;
	/**
	 * ���췽��
	 */
	public AddUserDialog() {

		super(ServerFrame.getServerFrame());
		
		this.setLayout(new BorderLayout());
		userInfoPanel = new UserInfoPanel();	
		this.add(userInfoPanel,BorderLayout.CENTER);
		
		userInfoPanel.getIdField().setText(autoBulidID());
		//��Ӽ���
		AddUserDialogListen addListen = new AddUserDialogListen(this);
		userInfoPanel.getSaveButton().addActionListener(addListen);
		userInfoPanel.getCancelButton().addActionListener(addListen);

		this.setModal(true);
		this.setTitle("����û�");
		this.setResizable(false);
		this.setSize(300, 360);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	
	
	
	/**
	 * �Զ������û���ŵķ���
	 * @return String �û�ID
	 */
	public String autoBulidID() {

		String sql = "select lpad(max(t.sid)+1,'5','0') from t_user t";
		String id = null;

		Connection conn = DBConnection.getConn();

		// 3. ����SQL���
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
	 * ��ȡ�û���Ϣ��巽��
	 * @return UserInfoPanel �û���Ϣ���
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}

}
