package lzx.qqserver.userManger;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lzx.qqserver.tools.QQUtils;




public class UserInfoPanel extends JPanel{
	
	private static final long serialVersionUID = -7007625432228304145L;

	/** 编号 */
	private JTextField idField;

	/** 姓名 */
	private JTextField nameField;

	/** 密码 */
	private JPasswordField passwordField;

	/** 性别 */
	private JComboBox sexComboBox;

	/** 年龄 */
	private JTextField ageField;

	/** 地址 */
	private JTextField addressField;

	/** 是否在线 */
	private JTextField isonlineField;

	/** 时间 */
	private JTextField timeField;

	/** 保存 */
	private JButton saveButton;

	/** 取消 */
	private JButton cancelButton;
	
	
	/**
	 * 构造函数
	 */
	public UserInfoPanel(){
		initPanel();
	}
	/**
	 *添加用户面板方法
	 */
	public void initPanel() {

		Box box = Box.createVerticalBox();

		JPanel idPanel = new JPanel();
		idField = new JTextField();
		idField.setEditable(false);// ID不可编辑
		idField.setPreferredSize(new Dimension(168, 20));
		idPanel.add(new JLabel("编 号"));
		idPanel.add(idField);

		JPanel namePanel = new JPanel();
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(168, 20));
		namePanel.add(new JLabel("姓 名"));
		namePanel.add(nameField);

		JPanel passwordPanel = new JPanel();
		passwordField = new JPasswordField("123456");
		passwordField.setPreferredSize(new Dimension(168, 20));
		passwordPanel.add(new JLabel("密 码"));
		passwordPanel.add(passwordField);

		JPanel sexPanel = new JPanel();
		sexComboBox = new JComboBox();
		sexComboBox.addItem("男");
		sexComboBox.addItem("女");
		sexComboBox.setPreferredSize(new Dimension(168, 20));
		sexPanel.add(new JLabel("性 别"));
		sexPanel.add(sexComboBox);

		JPanel agePanel = new JPanel();
		ageField = new JTextField();
		ageField.setPreferredSize(new Dimension(168, 20));
		agePanel.add(new JLabel("年 龄"));
		agePanel.add(ageField);

		JPanel addressPanel = new JPanel();
		addressField = new JTextField();
		addressField.setPreferredSize(new Dimension(168, 20));
		addressPanel.add(new JLabel("地 址"));
		addressPanel.add(addressField);

		JPanel isonlinePanel = new JPanel();
		isonlineField = new JTextField("不在线");
		isonlineField.setEditable(false);
		isonlineField.setPreferredSize(new Dimension(168, 20));
		isonlinePanel.add(new JLabel("状 态"));
		isonlinePanel.add(isonlineField);

		JPanel timePanel = new JPanel();
		timeField = new JTextField(QQUtils.formatTime());
		timeField.setEditable(false);
		timeField.setPreferredSize(new Dimension(168, 20));
		timePanel.add(new JLabel("日 期"));
		timePanel.add(timeField);

		Box buttonBox = Box.createHorizontalBox();

		saveButton = new JButton("保 存");
		//saveButton.addActionListener(addUserListen);
		cancelButton = new JButton("取消");
		//cancelButton.addActionListener(addUserListen);
		buttonBox.add(saveButton);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(cancelButton);
		
		box.add(Box.createVerticalStrut(10));
		box.add(idPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(namePanel);
		box.add(Box.createVerticalStrut(5));
		box.add(passwordPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(sexPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(agePanel);
		box.add(Box.createVerticalStrut(5));
		box.add(addressPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(isonlinePanel);
		box.add(Box.createVerticalStrut(5));
		box.add(timePanel);
		box.add(Box.createVerticalStrut(5));
		box.add(buttonBox);

		this.add(box);
	}



	/**
	 * 获取取消按钮的值。
	 * @return JButton 取消按钮的值。
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * 获取保存按钮的值。
	 * @return JButton 保存按钮的值。
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * 获取地址文本框的值。
	 * @return JTextField 地址文本框的值。
	 */
	public JTextField getAddressField() {
		return addressField;
	}


	/**
	 * 获取年龄文本框的值。
	 * @return JTextField 年龄文本框的值。
	 */
	public JTextField getAgeField() {
		return ageField;
	}

	/**
	 * 获取ID文本框的值。
	 * @return JTextField ID文本框的值。
	 */
	public JTextField getIdField() {
		return idField;
	}


	/**
	 * 获取姓名文本框的值。
	 * @return JTextField 姓名文本框的值。
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * 获取性别下拉框的值。
	 * @return JComboBox 性别下拉框的值。
	 */
	public JComboBox getSexComboBox() {
		return sexComboBox;
	}

	/**
	 * 获取是否在线文本框的值。
	 * @return JTextField 是否在线文本框的值。
	 */
	public JTextField getIsonlineField() {
		return isonlineField;
	}

	/**
	 * 获取密码文本框的值。
	 * @return JPasswordField 密码文本框的值。
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	/**
	 * 获取时间文本框的值。
	 * @return JTextField 时间文本框的值。
	 */
	public JTextField getTimeField() {
		return timeField;
	}


}
