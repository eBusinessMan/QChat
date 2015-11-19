package lzx.qqserver.userManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import lzx.qqserver.dao.LogDaoImpl;
import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;
import lzx.qqserver.tools.QQUtils;

import pub.QQPack;
import pub.QQPackType;


/**
 * �û���������
 * @author luozhixiao
 *
 */
public class UserPanelListen implements ActionListener{
	
	/** �û���� */
	private UserPanel userPanel;
	
	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;
	
	/** �û��� */
	private JTable userTable;

	
	/**
	 * ���췽��
	 */
	public UserPanelListen(final UserPanel userPanel) {

		this.userPanel = userPanel;
		userDao = new UserDaoImpl();

	}

	/**
	 * �¼������ķ���
	 */
	public void actionPerformed(ActionEvent e) {

		
		// �����ѯ��ť
		if (e.getSource().equals(userPanel.getSelectButton())) {

			selectCieck();// ���в�ѯ�ķ���

		} else if (e.getSource().equals(userPanel.getInsertButton())) {// ���

			new AddUserDialog();

		} else if (e.getSource().equals(userPanel.getUpdateButton())) {// �޸�

			new UpdateUserDialog();

		} else if (e.getSource().equals(userPanel.getDeleteButton())) {// ɾ��
			deleteUser();

		} else if (e.getSource().equals(userPanel.getRePswButton())) {// ��������

			rePassword();

		} else if (e.getSource().equals(userPanel.getRePswAllButton())) {// ������������

			reAllPassword();

		}
	}
	
	/**
	 * ��ѯ-�����û���ID��������״̬��ѯ����ѯ�û���Ҫ������
	 */

	public void selectCieck() {
		
		int isonline = -1;// ��ʼ��ֵ��ûѡ��(��0����ʾ�����ߣ���1����ʾ����)
		String id = userPanel.getIdField().getText().trim();// ��ȡID�ı����ֵ��ȥ���ҿո�
		String name = userPanel.getNameField().getText().trim();// ��ȡ�����ı����ֵ��ȥ���ҿո�

		boolean b = Pattern.matches("[0-9]*$", id);// ��������ʽ���ж�ID�Ƿ�淶
		if (b == false) {
			JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "��������ȷ�� ID ",
					"��ʾ", 1);
			userPanel.getIdField().setText("");// ��ID�ı����
			return;

		}

		if (userPanel.getOnLineComboBox().getSelectedItem().equals("����")) {

			isonline = 1;

		} else if (userPanel.getOnLineComboBox().getSelectedItem()
				.equals("������")) {

			isonline = 0;

		} else if (userPanel.getOnLineComboBox().getSelectedItem().equals("ѡ��")) {

			isonline = -1;
		}

		Vector<Vector<String>> rows = userDao.selectUser(id, name, isonline);

		userPanel.getDataModel().setDataVector(rows, userPanel.getColomn());

		userPanel.getDataModel().fireTableDataChanged();

	}

	/**
	 *ɾ���û��ķ���
	 */
	public void deleteUser() {
		userTable = userPanel.getUserTable();// �����û����ı��,ͨ��������Եõ���������
		String id = userTable.getValueAt(userTable.getSelectedRow(), 0)
				.toString();

		String isonline = userTable.getValueAt(userTable.getSelectedRow(), 6).toString();

		if (isonline.equals("����")) {// ��ʾ���û����ȣ��Ƿ�ǿ�������ߣ�
			int line = JOptionPane
					.showConfirmDialog(ServerFrame.getServerFrame(),
							"���û����ߣ��Ƿ�ǿ�������߲�ɾ�����û���", "�û�������ʾ",
							JOptionPane.YES_NO_OPTION);

			if (line == 0) {//ȷ��Ҫǿ��ɾ�������û�
				userDao.updateOnline(id, false);

				// ��ǿ�����߰�
				QQPack downOlinePack = new QQPack();
				downOlinePack.setType(QQPackType.FORCE_DOWNLINE);// ���ð�����Ϊǿ������
				// downOlinePack.set
				QQUtils.sendTo(downOlinePack, id);// �����߰�����Ҫǿ�����ߵ��Ǹ��û�
				QQUtils.threadMap.remove(id); // �����ߵ��û����߳�map�����Ƴ���

				// �������������û�ĳ�������ˣ������߰�
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(id);
				qqPack.setContent("�û�[" + id + "] ������!");
				QQUtils.sendToAll(qqPack);
				
				String str = "[" + QQUtils.formatTime() + "]:�û�[id]��ǿ��������!\n";
				ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
				ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
						ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument().getLength());
				// �����ݿ�����д��־
				LogDaoImpl logDao = new LogDaoImpl();
				logDao.addLog(QQUtils.formatDate(), str);
				
				if (userDao.deleteUser(id) == null) {

					ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
					ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// ���·��������������û���

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"ɾ���û��ɹ�!");

				} else {

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"ɾ���û�ʧ��!");
				}
			}

		} else { // ֱ��ɾ���û�

			int del = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
					"�Ƿ�ɾ��IDΪ: " + id + " �û�?", "ɾ���û�",
					JOptionPane.YES_NO_OPTION);
			if (del == 0) {
				if (userDao.deleteUser(id) == null) {

					ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
					ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// ���·��������������û���

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"ɾ���û��ɹ�!");

				} else {

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"ɾ���û�ʧ��!");
				}

			}

		}

	}

	/**
	 * ��������ķ���
	 * 
	 */
	public void rePassword() {
		userTable = userPanel.getUserTable();// �����û����ı��,ͨ��������Եõ���������
		String id = userTable.getValueAt(userTable.getSelectedRow(), 0)
				.toString();
		String oldPwd = userTable.getValueAt(userTable.getSelectedRow(), 3)
				.toString();
		String newPwd = "123456";

		int reset = JOptionPane
				.showConfirmDialog(ServerFrame.getServerFrame(), "���Ҫ����ID: " + id
						+ " �û�������?", "��������", JOptionPane.YES_NO_OPTION);

		if (reset == 0) {

			if (userDao.updatePassword(id, oldPwd, newPwd) == null) {

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// ���·��������������û���

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "�������óɹ�!");

			} else {

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "��������ʧ��!");
			}
		}


	}

	/**
	 * ������������ķ���
	 * 
	 */
	public void reAllPassword() {

		int reset = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
				"���Ҫ��������������", "������������", JOptionPane.YES_NO_OPTION);

		if (reset == 0) {

			if (userDao.updatePassword(null, null, "123456") == null) {

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// ���·��������������û���

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "������������ɹ�!");

			} else {

				JOptionPane
						.showMessageDialog(ServerFrame.getServerFrame(), "������������ʧ��!");
			}
		}
	}
}
