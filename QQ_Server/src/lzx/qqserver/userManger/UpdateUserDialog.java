package lzx.qqserver.userManger;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JTable;

import lzx.qqserver.main.ServerFrame;



/**
 * 修改用户对话框类
 *@author luozhixiao
 *
 */
public class UpdateUserDialog extends JDialog{
	
	private static final long serialVersionUID = 1855917317810197826L;
	
	/** 用户信息面板 */
	private UserInfoPanel userInfoPanel;
	
	/** 表格 */
	private JTable userTable;
	
	/** 用户名 */
	private String name = null;

	/**
	 * 构造方法
	 */
	public UpdateUserDialog() {

		super(ServerFrame.getServerFrame());
		
		this.setLayout(new BorderLayout());
		
		userInfoPanel = new UserInfoPanel();
		userTable =ServerFrame.getServerFrame().getUserPanel().getUserTable();// 引用用户面板的表格,通过这个可以得到表格的数据
		
		this.add(userInfoPanel,BorderLayout.CENTER);
		
		userInfoPanel.getIdField().setText(userTable.getValueAt(userTable.getSelectedRow(), 0).toString());
		userInfoPanel.getNameField().setText(userTable.getValueAt(userTable.getSelectedRow(), 1).toString());
		userInfoPanel.getPasswordField().setText(userTable.getValueAt(userTable.getSelectedRow(), 2).toString());
		userInfoPanel.getSexComboBox().setSelectedItem(userTable.getValueAt(userTable.getSelectedRow(), 3));
		userInfoPanel.getAgeField().setText(userTable.getValueAt(userTable.getSelectedRow(), 4).toString());
		if (userTable.getValueAt(userTable.getSelectedRow(), 5)== null) {
			userInfoPanel.getAddressField().setText("");
		}else{
			userInfoPanel.getAddressField().setText(userTable.getValueAt(userTable.getSelectedRow(), 5).toString());
		}
		userInfoPanel.getIsonlineField().setText(userTable.getValueAt(userTable.getSelectedRow(), 6).toString());
		
		name = userTable.getValueAt(userTable.getSelectedRow(), 1).toString();
		//添加监听
		UpdateUserDialogListen updateListen = new UpdateUserDialogListen(this);
		userInfoPanel.getSaveButton().addActionListener(updateListen);
		userInfoPanel.getCancelButton().addActionListener(updateListen);

		this.setModal(true);
		this.setTitle("添加用户");
		this.setResizable(false);
		this.setSize(300, 360);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	/**
	 * 获取用户信息面板的方法
	 * @return UserInfoPanel 用户信息面板
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}

	/**
	 * 获取用户名的方法
	 */
	public String getName() {
		return name;
	}

}
