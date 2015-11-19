package lzx.qqserver.userManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;
import lzx.qqserver.tools.QQUtils;

import pub.QQPack;
import pub.QQPackType;
import pub.User;


/**
 * �޸��û�������
 * @author luozhixiao
 *
 */
public class UpdateUserDialogListen implements ActionListener {

	/** ����û��Ի��� */
	private UpdateUserDialog updateUser;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	/**
	 * ���캯��
	 * @param updateUser �޸��û����
	 */
	public UpdateUserDialogListen(final UpdateUserDialog updateUser) {

		this.updateUser = updateUser;

	}

	/**
	 * �����¼�����
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(updateUser.getUserInfoPanel().getSaveButton())) {// ����

			updateUserMethod();// �޸��û��ķ���

		} else if (e.getSource().equals(
				updateUser.getUserInfoPanel().getCancelButton())) {// ȡ��

			updateUser.dispose();
			return;

		}

	}

	/**
	 * �޸��û��ķ���
	 * 
	 */
	public void updateUserMethod() {

		// 5) ����ɹ����Զ�ˢ������; �ҽ� �޸��û� , ɾ���û� , �������� ������ť������Ϊ������(�����ɵ��);

		String id = updateUser.getUserInfoPanel().getIdField().getText().trim();// ���
		String names = updateUser.getUserInfoPanel().getNameField().getText()
				.trim();// ����
		String password = new String(updateUser.getUserInfoPanel().getPasswordField().getPassword());// ����
		String sex = (String) updateUser.getUserInfoPanel().getSexComboBox()
				.getSelectedItem();// �Ա�
		String age = updateUser.getUserInfoPanel().getAgeField().getText()
				.trim();// ����
		String address = updateUser.getUserInfoPanel().getAddressField()
				.getText().trim();// ��ַ
		
		String isonline = updateUser.getUserInfoPanel().getIsonlineField().getText().trim();

		boolean bo = checkUserInfo(names, password, age, address);

		if (bo == true) {

			User user = new User();
			user.setSid(id);
			user.setSname(names);
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
			// userDao.updateUser(user);
			if (userDao.updateUser(user) == null) {

				updateUser.dispose();
				// �޸��û�,ɾ���û�,�������� ��ť���ɱ༭
				ServerFrame.getServerFrame().getUserPanel().getUpdateButton()
						.setEnabled(false);
				ServerFrame.getServerFrame().getUserPanel().getDeleteButton()
						.setEnabled(false);
				ServerFrame.getServerFrame().getUserPanel().getRePswButton()
						.setEnabled(false);
				JOptionPane.showMessageDialog(updateUser, "�޸��û��ɹ�! ", "��ʾ", 1);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// �����û���������û���
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// ���·��������������û���
				
				//�ж��û��Ƿ����ߣ�������߷���Ϣ�����û��������Ѿ����޸ģ�����������޸ģ�����������û����
				System.out.println(isonline);
				
				if(isonline.equals("����")){
					
					QQPack qqpack = new QQPack();
					qqpack.setType(QQPackType.UPDATE_INFO);// ���ð�����Ϊ�޸����ϰ�
					qqpack.setContent(user);
					QQUtils.sendTo(qqpack, id);//���޸�������� �����û�
					
					if(!updateUser.getName().equals(names)){
						// -----------------------------------�������û��б�������û�
						QQPack onlineUserPack = new QQPack();
						// ������ݵ�Vector,������list��Ū��ת����Vector����
						Vector<User> ve = new Vector<User>();
						List<User> onlineList = new ArrayList<User>();// ��ȡ�����û���
						onlineList = userDao.getOnlineUser(); // ��ȡ��ǰ�����û�����
						for (User user1 : onlineList) { //
							// ��list���б�������list�����user��ŵ�vector�С�
							ve.add(user1);
						}
						onlineUserPack.setType(QQPackType.ONLINE_LIST);
						onlineUserPack.setContent(ve); // ���÷�������
						QQUtils.sendToAll(onlineUserPack);// ���������û����������û�
						// --------------------------------------------
					}
				}		

			}
		}

	}

	/**
	 * ��֤���������롢���䡢��ַ�ķ���
	 * 
	 * @param name
	 *            ��֤����
	 * @param password
	 *            ��֤����
	 * @param age
	 *            ��֤����
	 * @param address
	 *            ��֤��ַ
	 * @return ���������롢���䡢��ַ�Ƿ���Ϲ淶
	 */
	public boolean checkUserInfo(String name, String password, String age,
			String address) {

		// �ж������Ƿ���Ϲ淶(��ʵ������������ 2~10 ֮�� (���������Ļ�Ӣ��))
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " ��������Ϊ��,����������! ", "��ʾ",
					1);
			return false;
		} else if (name.length() < 2 || name.length() > 10) {

			JOptionPane.showMessageDialog(updateUser, " ��ʵ������������3~10 ֮��! ",
					"��ʾ", 1);
			return false;
		} else if (!(Pattern.matches("[\u4e00-\u9fa5]{2,10}", name))) {

			JOptionPane.showMessageDialog(updateUser, " ��ʵ��������������! ", "��ʾ", 1);
			updateUser.getUserInfoPanel().getNameField().setText("");

			return false;
		}

		// �ж������Ƿ���Ϲ淶(���� ������ 3~16 ֮�� (ֻ���������֣���ĸ , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " ���벻��Ϊ��,����������! ", "��ʾ",
					1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(updateUser, " ���볤�ȱ����� 3~16 ֮��! ",
					"��ʾ", 1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(updateUser, " ����ֻ���������֣���ĸ , _ ! ",
					"��ʾ", 1);
			updateUser.getUserInfoPanel().getPasswordField().setText("");
			return false;
		}

		// �ж������Ƿ���Ϲ淶(���� ������ 20~150֮��)
		if (age.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " ���䲻��Ϊ��,����������! ", "��ʾ",
					1);
			return false;
		}
		if (!(Pattern.matches("[0-9]*$", age))) {
			JOptionPane.showMessageDialog(updateUser, " ���䳤�ȱ���������! ", "��ʾ", 1);
			updateUser.getUserInfoPanel().getAgeField().setText("");
			return false;
		}

		if (Integer.parseInt(age) < 20 || Integer.parseInt(age) > 150) {
			JOptionPane.showMessageDialog(updateUser, " ����ֵ������ 20~150 ֮��! ",
					"��ʾ", 1);
			return false;
		}

		// �жϵ�ַ�Ƿ���Ϲ淶(��ַ ����Ϊ�գ����粻Ϊ��ʱ�����Ȳ����� 100)
		if (address.length() > 100) {

			JOptionPane.showMessageDialog(updateUser, " ��ַ���ȱ�����100����! ", "��ʾ",
					1);
			return false;
		}

		return true;
	}
}
