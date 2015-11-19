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
 * 修改用户监听类
 * @author luozhixiao
 *
 */
public class UpdateUserDialogListen implements ActionListener {

	/** 添加用户对话框 */
	private UpdateUserDialog updateUser;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	/**
	 * 构造函数
	 * @param updateUser 修改用户面板
	 */
	public UpdateUserDialogListen(final UpdateUserDialog updateUser) {

		this.updateUser = updateUser;

	}

	/**
	 * 监听事件方法
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(updateUser.getUserInfoPanel().getSaveButton())) {// 保存

			updateUserMethod();// 修改用户的方法

		} else if (e.getSource().equals(
				updateUser.getUserInfoPanel().getCancelButton())) {// 取消

			updateUser.dispose();
			return;

		}

	}

	/**
	 * 修改用户的方法
	 * 
	 */
	public void updateUserMethod() {

		// 5) 保存成功后自动刷新数据; 且将 修改用户 , 删除用户 , 重置密码 三个按钮重新设为不可用(即不可点击);

		String id = updateUser.getUserInfoPanel().getIdField().getText().trim();// 编号
		String names = updateUser.getUserInfoPanel().getNameField().getText()
				.trim();// 姓名
		String password = new String(updateUser.getUserInfoPanel().getPasswordField().getPassword());// 密码
		String sex = (String) updateUser.getUserInfoPanel().getSexComboBox()
				.getSelectedItem();// 性别
		String age = updateUser.getUserInfoPanel().getAgeField().getText()
				.trim();// 年龄
		String address = updateUser.getUserInfoPanel().getAddressField()
				.getText().trim();// 地址
		
		String isonline = updateUser.getUserInfoPanel().getIsonlineField().getText().trim();

		boolean bo = checkUserInfo(names, password, age, address);

		if (bo == true) {

			User user = new User();
			user.setSid(id);
			user.setSname(names);
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
			// userDao.updateUser(user);
			if (userDao.updateUser(user) == null) {

				updateUser.dispose();
				// 修改用户,删除用户,重置密码 按钮不可编辑
				ServerFrame.getServerFrame().getUserPanel().getUpdateButton()
						.setEnabled(false);
				ServerFrame.getServerFrame().getUserPanel().getDeleteButton()
						.setEnabled(false);
				ServerFrame.getServerFrame().getUserPanel().getRePswButton()
						.setEnabled(false);
				JOptionPane.showMessageDialog(updateUser, "修改用户成功! ", "提示", 1);

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// 更新服务管理面的在线用户表
				
				//判断用户是否在线，如果在线发信息告诉用户，资料已经被修改，如果姓名被修改，需更新在线用户别表
				System.out.println(isonline);
				
				if(isonline.equals("在线")){
					
					QQPack qqpack = new QQPack();
					qqpack.setType(QQPackType.UPDATE_INFO);// 设置包类型为修改资料包
					qqpack.setContent(user);
					QQUtils.sendTo(qqpack, id);//把修改完的资料 发给用户
					
					if(!updateUser.getName().equals(names)){
						// -----------------------------------发在线用户列表给所有用户
						QQPack onlineUserPack = new QQPack();
						// 存放数据的Vector,用来从list类弄中转换到Vector类型
						Vector<User> ve = new Vector<User>();
						List<User> onlineList = new ArrayList<User>();// 获取在线用户。
						onlineList = userDao.getOnlineUser(); // 获取当前在线用户方法
						for (User user1 : onlineList) { //
							// 对list进行遍历。把list里面的user存放到vector中。
							ve.add(user1);
						}
						onlineUserPack.setType(QQPackType.ONLINE_LIST);
						onlineUserPack.setContent(ve); // 设置发送内容
						QQUtils.sendToAll(onlineUserPack);// 发送在线用户包给所有用户
						// --------------------------------------------
					}
				}		

			}
		}

	}

	/**
	 * 验证姓名、密码、年龄、地址的方法
	 * 
	 * @param name
	 *            验证姓名
	 * @param password
	 *            验证密码
	 * @param age
	 *            验证年龄
	 * @param address
	 *            验证地址
	 * @return 姓名、密码、年龄、地址是否符合规范
	 */
	public boolean checkUserInfo(String name, String password, String age,
			String address) {

		// 判断姓名是否符合规范(真实姓名长度需在 2~10 之间 (必需是中文或英文))
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " 姓名不能为空,请输入姓名! ", "提示",
					1);
			return false;
		} else if (name.length() < 2 || name.length() > 10) {

			JOptionPane.showMessageDialog(updateUser, " 真实姓名长度需在3~10 之间! ",
					"提示", 1);
			return false;
		} else if (!(Pattern.matches("[\u4e00-\u9fa5]{2,10}", name))) {

			JOptionPane.showMessageDialog(updateUser, " 真实姓名必需是中文! ", "提示", 1);
			updateUser.getUserInfoPanel().getNameField().setText("");

			return false;
		}

		// 判断密码是否符合规范(密码 长度在 3~16 之间 (只允许是数字，字母 , _ ))
		if (password.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " 密码不能为空,请输入密码! ", "提示",
					1);
			return false;
		} else if (password.length() < 3 || password.length() > 16) {

			JOptionPane.showMessageDialog(updateUser, " 密码长度必需在 3~16 之间! ",
					"提示", 1);
			return false;
		} else if (!(Pattern.matches("^\\w+$", password))) {

			JOptionPane.showMessageDialog(updateUser, " 密码只允许是数字，字母 , _ ! ",
					"提示", 1);
			updateUser.getUserInfoPanel().getPasswordField().setText("");
			return false;
		}

		// 判断年龄是否符合规范(年龄 是数字 20~150之间)
		if (age.length() == 0) {
			JOptionPane.showMessageDialog(updateUser, " 年龄不能为空,请输入年龄! ", "提示",
					1);
			return false;
		}
		if (!(Pattern.matches("[0-9]*$", age))) {
			JOptionPane.showMessageDialog(updateUser, " 年龄长度必需是数字! ", "提示", 1);
			updateUser.getUserInfoPanel().getAgeField().setText("");
			return false;
		}

		if (Integer.parseInt(age) < 20 || Integer.parseInt(age) > 150) {
			JOptionPane.showMessageDialog(updateUser, " 年龄值必需在 20~150 之间! ",
					"提示", 1);
			return false;
		}

		// 判断地址是否符合规范(地址 可以为空，但如不为空时，长度不大于 100)
		if (address.length() > 100) {

			JOptionPane.showMessageDialog(updateUser, " 地址长度必需在100以内! ", "提示",
					1);
			return false;
		}

		return true;
	}
}
