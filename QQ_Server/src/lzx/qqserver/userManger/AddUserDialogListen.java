package lzx.qqserver.userManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;


import pub.User;

/**
 * ����û��Ի��������
 * @author luozhixiao
 *
 */
public class AddUserDialogListen implements ActionListener {

	/** ����û��Ի��� */
	private AddUserDialog adduser;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	public AddUserDialogListen(final AddUserDialog adduser) {

		this.adduser = adduser;

	}

	/**
	 * �����¼�����
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(adduser.getUserInfoPanel().getSaveButton())) {// ����

			addUserMethod();// ����û��ķ���

		} else if (e.getSource().equals(
				adduser.getUserInfoPanel().getCancelButton())) {// ȡ��

			adduser.dispose();
			return;

		}

	}

	/**
	 * ����û��ķ���
	 */
	public void addUserMethod() {

		// 5) ����ɹ����Զ�ˢ������; �ҽ� �޸��û� , ɾ���û� , �������� ������ť������Ϊ������(�����ɵ��);

		String id = adduser.getUserInfoPanel().getIdField().getText().trim();// ���

		String name = adduser.getUserInfoPanel().getNameField().getText()
				.trim();// ����

		String password = new String(adduser.getUserInfoPanel().getPasswordField().getPassword());// ����

		String sex = adduser.getUserInfoPanel().getSexComboBox()
				.getSelectedItem().toString();// �Ա�

		String age = adduser.getUserInfoPanel().getAgeField().getText().trim();// ����

		String address = adduser.getUserInfoPanel().getAddressField().getText()
				.trim();// ��ַ

		boolean bo = checkUserInfo(name, password, age, address);
		if (bo == true) {

			User user = new User();
			user.setSid(id);
			user.setSname(name);
			user.setSpassword(password);
			if (sex.equals("��")) {
				user.setSsex("0");
			} else if (sex.equals("Ů")) {
				user.setSsex("1");
			}
			user.setNage(age);
			user.setSaddress(address);
			// ִ�����SQl
			userDao = new UserDaoImpl();
			// userDao.addUser(user);
			if (userDao.addUser(user) == null) {
				adduser.dispose();
				JOptionPane.showMessageDialog(adduser, "����û��ɹ�! ", "��ʾ", 1);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���

			} else {
				JOptionPane.showMessageDialog(adduser, "����û�ʧ��! ", "��ʾ", 1);
				return;
			}
			// �޸��û�,ɾ���û�,�������� ��ť���ɱ༭
			ServerFrame.getServerFrame().getUserPanel().getUpdateButton()
					.setEnabled(false);
			ServerFrame.getServerFrame().getUserPanel().getDeleteButton()
					.setEnabled(false);
			ServerFrame.getServerFrame().getUserPanel().getRePswButton()
					.setEnabled(false);
		}

	}


	/**
	 * ��֤���������롢���䡢��ַ�ķ���
	 * @param name ��֤����
	 * @param password ��֤����
	 * @param age ��֤����
	 * @param address ��֤��ַ
	 * @return  ���������롢���䡢��ַ�Ƿ���Ϲ淶
	 */
	public boolean checkUserInfo(String name, String password, String age,
			String address) {


		// �ж������Ƿ���Ϲ淶(��ʵ������������ 2~10 ֮�� (���������Ļ�Ӣ��))
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " ��������Ϊ��,����������! ", "��ʾ", 1);
			return false;
		} else if (name.length() < 2 || name.length() > 10) {

			JOptionPane.showMessageDialog(adduser, " ��ʵ������������3~10 ֮��! ", "��ʾ",
					1);
			return false;
		} else if (!(Pattern.matches("[\u4e00-\u9fa5]{2,10}", name))) {

			JOptionPane.showMessageDialog(adduser, " ��ʵ��������������! ", "��ʾ", 1);
			adduser.getUserInfoPanel().getNameField().setText("");

			return false;
		}

		// �ж������Ƿ���Ϲ淶(���� ������ 3~16 ֮�� (ֻ���������֣���ĸ , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " ���벻��Ϊ��,����������! ", "��ʾ", 1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(adduser, " ���볤�ȱ����� 3~16 ֮��! ", "��ʾ",
					1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(adduser, " ����ֻ���������֣���ĸ , _ ! ", "��ʾ",
					1);
			adduser.getUserInfoPanel().getPasswordField().setText("");
			return false;
		}

		// �ж������Ƿ���Ϲ淶(���� ������ 20~150֮��)
		if (age.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " ���䲻��Ϊ��,����������! ", "��ʾ", 1);
			return false;
		}
		if (!(Pattern.matches("[0-9]*$", age))) {
			JOptionPane.showMessageDialog(adduser, " ���䳤�ȱ���������! ", "��ʾ", 1);
			adduser.getUserInfoPanel().getAgeField().setText("");
			return false;
		}

		if (Integer.parseInt(age) < 20 || Integer.parseInt(age) > 150) {
			JOptionPane.showMessageDialog(adduser, " ����ֵ������ 20~150 ֮��! ", "��ʾ",
					1);
			return false;
		}

		// �жϵ�ַ�Ƿ���Ϲ淶(��ַ ����Ϊ�գ����粻Ϊ��ʱ�����Ȳ����� 100)
		if (address.length() > 100) {

			JOptionPane.showMessageDialog(adduser, " ��ַ���ȱ�����100����! ", "��ʾ", 1);
			return false;
		}

		return true;
	}
}
