package lzx.qqserver.userManger;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JTable;

import lzx.qqserver.main.ServerFrame;



/**
 * �޸��û��Ի�����
 *@author luozhixiao
 *
 */
public class UpdateUserDialog extends JDialog{
	
	private static final long serialVersionUID = 1855917317810197826L;
	
	/** �û���Ϣ��� */
	private UserInfoPanel userInfoPanel;
	
	/** ��� */
	private JTable userTable;
	
	/** �û��� */
	private String name = null;

	/**
	 * ���췽��
	 */
	public UpdateUserDialog() {

		super(ServerFrame.getServerFrame());
		
		this.setLayout(new BorderLayout());
		
		userInfoPanel = new UserInfoPanel();
		userTable =ServerFrame.getServerFrame().getUserPanel().getUserTable();// �����û����ı��,ͨ��������Եõ���������
		
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
		//��Ӽ���
		UpdateUserDialogListen updateListen = new UpdateUserDialogListen(this);
		userInfoPanel.getSaveButton().addActionListener(updateListen);
		userInfoPanel.getCancelButton().addActionListener(updateListen);

		this.setModal(true);
		this.setTitle("����û�");
		this.setResizable(false);
		this.setSize(300, 360);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	/**
	 * ��ȡ�û���Ϣ���ķ���
	 * @return UserInfoPanel �û���Ϣ���
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}

	/**
	 * ��ȡ�û����ķ���
	 */
	public String getName() {
		return name;
	}

}
