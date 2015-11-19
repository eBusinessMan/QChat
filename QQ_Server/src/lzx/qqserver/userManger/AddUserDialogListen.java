package lzx.qqserver.userManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.main.ServerFrame;


import pub.User;

/**
 * 添加用户对话框监听类
 * @author luozhixiao
 *
 */
public class AddUserDialogListen implements ActionListener {

	/** 添加用户对话框 */
	private AddUserDialog adduser;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	public AddUserDialogListen(final AddUserDialog adduser) {

		this.adduser = adduser;

	}

	/**
	 * 监听事件方法
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(adduser.getUserInfoPanel().getSaveButton())) {// 保存

			addUserMethod();// 添加用户的方法

		} else if (e.getSource().equals(
				adduser.getUserInfoPanel().getCancelButton())) {// 取消

			adduser.dispose();
			return;

		}

	}

	/**
	 * 添加用户的方法
	 */
	public void addUserMethod() {

		// 5) 保存成功后自动刷新数据; 且将 修改用户 , 删除用户 , 重置密码 三个按钮重新设为不可用(即不可点击);

		String id = adduser.getUserInfoPanel().getIdField().getText().trim();// 编号

		String name = adduser.getUserInfoPanel().getNameField().getText()
				.trim();// 姓名

		String password = new String(adduser.getUserInfoPanel().getPasswordField().getPassword());// 密码

		String sex = adduser.getUserInfoPanel().getSexComboBox()
				.getSelectedItem().toString();// 性别

		String age = adduser.getUserInfoPanel().getAgeField().getText().trim();// 年龄

		String address = adduser.getUserInfoPanel().getAddressField().getText()
				.trim();// 地址

		boolean bo = checkUserInfo(name, password, age, address);
		if (bo == true) {

			User user = new User();
			user.setSid(id);
			user.setSname(name);
			user.setSpassword(password);
			if (sex.equals("男")) {
				user.setSsex("0");
			} else if (sex.equals("女")) {
				user.setSsex("1");
			}
			user.setNage(age);
			user.setSaddress(address);
			// 执行添加SQl
			userDao = new UserDaoImpl();
			// userDao.addUser(user);
			if (userDao.addUser(user) == null) {
				adduser.dispose();
				JOptionPane.showMessageDialog(adduser, "添加用户成功! ", "提示", 1);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表

			} else {
				JOptionPane.showMessageDialog(adduser, "添加用户失败! ", "提示", 1);
				return;
			}
			// 修改用户,删除用户,重置密码 按钮不可编辑
			ServerFrame.getServerFrame().getUserPanel().getUpdateButton()
					.setEnabled(false);
			ServerFrame.getServerFrame().getUserPanel().getDeleteButton()
					.setEnabled(false);
			ServerFrame.getServerFrame().getUserPanel().getRePswButton()
					.setEnabled(false);
		}

	}


	/**
	 * 验证姓名、密码、年龄、地址的方法
	 * @param name 验证姓名
	 * @param password 验证密码
	 * @param age 验证年龄
	 * @param address 验证地址
	 * @return  姓名、密码、年龄、地址是否符合规范
	 */
	public boolean checkUserInfo(String name, String password, String age,
			String address) {


		// 判断姓名是否符合规范(真实姓名长度需在 2~10 之间 (必需是中文或英文))
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " 姓名不能为空,请输入姓名! ", "提示", 1);
			return false;
		} else if (name.length() < 2 || name.length() > 10) {

			JOptionPane.showMessageDialog(adduser, " 真实姓名长度需在3~10 之间! ", "提示",
					1);
			return false;
		} else if (!(Pattern.matches("[\u4e00-\u9fa5]{2,10}", name))) {

			JOptionPane.showMessageDialog(adduser, " 真实姓名必需是中文! ", "提示", 1);
			adduser.getUserInfoPanel().getNameField().setText("");

			return false;
		}

		// 判断密码是否符合规范(密码 长度在 3~16 之间 (只允许是数字，字母 , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " 密码不能为空,请输入密码! ", "提示", 1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(adduser, " 密码长度必需在 3~16 之间! ", "提示",
					1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(adduser, " 密码只允许是数字，字母 , _ ! ", "提示",
					1);
			adduser.getUserInfoPanel().getPasswordField().setText("");
			return false;
		}

		// 判断年龄是否符合规范(年龄 是数字 20~150之间)
		if (age.length() == 0) {
			JOptionPane.showMessageDialog(adduser, " 年龄不能为空,请输入年龄! ", "提示", 1);
			return false;
		}
		if (!(Pattern.matches("[0-9]*$", age))) {
			JOptionPane.showMessageDialog(adduser, " 年龄长度必需是数字! ", "提示", 1);
			adduser.getUserInfoPanel().getAgeField().setText("");
			return false;
		}

		if (Integer.parseInt(age) < 20 || Integer.parseInt(age) > 150) {
			JOptionPane.showMessageDialog(adduser, " 年龄值必需在 20~150 之间! ", "提示",
					1);
			return false;
		}

		// 判断地址是否符合规范(地址 可以为空，但如不为空时，长度不大于 100)
		if (address.length() > 100) {

			JOptionPane.showMessageDialog(adduser, " 地址长度必需在100以内! ", "提示", 1);
			return false;
		}

		return true;
	}
}
