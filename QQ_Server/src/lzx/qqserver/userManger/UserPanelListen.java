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
 * 用户面板监听类
 * @author luozhixiao
 *
 */
public class UserPanelListen implements ActionListener{
	
	/** 用户面板 */
	private UserPanel userPanel;
	
	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;
	
	/** 用户表 */
	private JTable userTable;

	
	/**
	 * 构造方法
	 */
	public UserPanelListen(final UserPanel userPanel) {

		this.userPanel = userPanel;
		userDao = new UserDaoImpl();

	}

	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {

		
		// 点击查询按钮
		if (e.getSource().equals(userPanel.getSelectButton())) {

			selectCieck();// 运行查询的方法

		} else if (e.getSource().equals(userPanel.getInsertButton())) {// 添加

			new AddUserDialog();

		} else if (e.getSource().equals(userPanel.getUpdateButton())) {// 修改

			new UpdateUserDialog();

		} else if (e.getSource().equals(userPanel.getDeleteButton())) {// 删除
			deleteUser();

		} else if (e.getSource().equals(userPanel.getRePswButton())) {// 重置密码

			rePassword();

		} else if (e.getSource().equals(userPanel.getRePswAllButton())) {// 重置所有密码

			reAllPassword();

		}
	}
	
	/**
	 * 查询-根据用户的ID，姓名，状态查询，查询用户想要的数据
	 */

	public void selectCieck() {
		
		int isonline = -1;// 初始化值，没选择(‘0’表示不在线，‘1’表示在线)
		String id = userPanel.getIdField().getText().trim();// 获取ID文本框的值并去左右空格
		String name = userPanel.getNameField().getText().trim();// 获取姓名文本框的值并去左右空格

		boolean b = Pattern.matches("[0-9]*$", id);// 用正则表达式来判断ID是否规范
		if (b == false) {
			JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "请输入正确的 ID ",
					"提示", 1);
			userPanel.getIdField().setText("");// 把ID文本清空
			return;

		}

		if (userPanel.getOnLineComboBox().getSelectedItem().equals("在线")) {

			isonline = 1;

		} else if (userPanel.getOnLineComboBox().getSelectedItem()
				.equals("不在线")) {

			isonline = 0;

		} else if (userPanel.getOnLineComboBox().getSelectedItem().equals("选择")) {

			isonline = -1;
		}

		Vector<Vector<String>> rows = userDao.selectUser(id, name, isonline);

		userPanel.getDataModel().setDataVector(rows, userPanel.getColomn());

		userPanel.getDataModel().fireTableDataChanged();

	}

	/**
	 *删除用户的方法
	 */
	public void deleteUser() {
		userTable = userPanel.getUserTable();// 引用用户面板的表格,通过这个可以得到表格的数据
		String id = userTable.getValueAt(userTable.getSelectedRow(), 0)
				.toString();

		String isonline = userTable.getValueAt(userTable.getSelectedRow(), 6).toString();

		if (isonline.equals("在线")) {// 提示该用户在先，是否强制其下线？
			int line = JOptionPane
					.showConfirmDialog(ServerFrame.getServerFrame(),
							"该用户在线，是否强制其下线并删除该用户？", "用户在线提示",
							JOptionPane.YES_NO_OPTION);

			if (line == 0) {//确认要强制删除在线用户
				userDao.updateOnline(id, false);

				// 发强制下线包
				QQPack downOlinePack = new QQPack();
				downOlinePack.setType(QQPackType.FORCE_DOWNLINE);// 设置包类型为强制下线
				// downOlinePack.set
				QQUtils.sendTo(downOlinePack, id);// 将下线包发给要强制下线的那个用户
				QQUtils.threadMap.remove(id); // 把下线的用户从线程map里面移除。

				// 告诉所有在线用户某人下线了，发下线包
				QQPack qqPack = new QQPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(id);
				qqPack.setContent("用户[" + id + "] 下线了!");
				QQUtils.sendToAll(qqPack);
				
				String str = "[" + QQUtils.formatTime() + "]:用户[id]被强制下线了!\n";
				ServerFrame.getServerFrame().getServerPanel().getServerLogArea().append(str);
				ServerFrame.getServerFrame().getServerPanel().getServerLogArea().setCaretPosition(
						ServerFrame.getServerFrame().getServerPanel().getServerLogArea().getDocument().getLength());
				// 往数据库里面写日志
				LogDaoImpl logDao = new LogDaoImpl();
				logDao.addLog(QQUtils.formatDate(), str);
				
				if (userDao.deleteUser(id) == null) {

					ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
					ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// 更新服务管理面的在线用户表

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"删除用户成功!");

				} else {

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"删除用户失败!");
				}
			}

		} else { // 直接删除用户

			int del = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
					"是否删除ID为: " + id + " 用户?", "删除用户",
					JOptionPane.YES_NO_OPTION);
			if (del == 0) {
				if (userDao.deleteUser(id) == null) {

					ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
					ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// 更新服务管理面的在线用户表

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"删除用户成功!");

				} else {

					JOptionPane.showMessageDialog(ServerFrame.getServerFrame(),
							"删除用户失败!");
				}

			}

		}

	}

	/**
	 * 重置密码的方法
	 * 
	 */
	public void rePassword() {
		userTable = userPanel.getUserTable();// 引用用户面板的表格,通过这个可以得到表格的数据
		String id = userTable.getValueAt(userTable.getSelectedRow(), 0)
				.toString();
		String oldPwd = userTable.getValueAt(userTable.getSelectedRow(), 3)
				.toString();
		String newPwd = "123456";

		int reset = JOptionPane
				.showConfirmDialog(ServerFrame.getServerFrame(), "真的要重置ID: " + id
						+ " 用户密码吗?", "重置密码", JOptionPane.YES_NO_OPTION);

		if (reset == 0) {

			if (userDao.updatePassword(id, oldPwd, newPwd) == null) {

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// 更新服务管理面的在线用户表

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "密码重置成功!");

			} else {

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "密码重置失败!");
			}
		}


	}

	/**
	 * 重置所有密码的方法
	 * 
	 */
	public void reAllPassword() {

		int reset = JOptionPane.showConfirmDialog(ServerFrame.getServerFrame(),
				"真的要重置所有密码吗？", "重置所有密码", JOptionPane.YES_NO_OPTION);

		if (reset == 0) {

			if (userDao.updatePassword(null, null, "123456") == null) {

				ServerFrame.getServerFrame().getUserPanel().updateUserData();// 更新用户管理面板用户表
				ServerFrame.getServerFrame().getServerPanel().updateOnlineUserData();// 更新服务管理面的在线用户表

				JOptionPane.showMessageDialog(ServerFrame.getServerFrame(), "重置所有密码成功!");

			} else {

				JOptionPane
						.showMessageDialog(ServerFrame.getServerFrame(), "重置所有密码失败!");
			}
		}
	}
}
