package lzx.qqserver.userManger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import lzx.qqserver.dao.UserDaoImpl;



/**
 * 用户面板类
 *@author luozhixiao
 *
 */
public class UserPanel extends JPanel {

	private static final long serialVersionUID = 8208034153855470093L;

	/** 上面 */
	private JPanel topPanel;

	/** 中面板 */
	private JPanel centerPanel;

	/** 下面板 */
	private JPanel bottomPanel;

	/** 查询 */
	private JButton selectButton;

	/** 添加 */
	private JButton insertButton;

	/** 修改 */
	private JButton updateButton;

	/** 删除 */
	private JButton deleteButton;

	/** 重置密码 */
	private JButton rePswButton;

	/** 重置所有密码 */
	private JButton rePswAllButton;

	/** ID */
	private JTextField idField;

	/** 姓名 */
	private JTextField nameField;

	/** 状态 */
	private JComboBox onLineComboBox;

	/** 表模型 */
	private DefaultTableModel dataModel;

	/** 表格 */
	private JTable userTable;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	/** 用户管理监听 */
	private UserPanelListen userListen;

	/** 表头 */
	private Vector<String> colomn;



	/**
	 * 构造方法
	 */
	public UserPanel() {

		this.setLayout(new BorderLayout());
		userDao = new UserDaoImpl();
		userListen = new UserPanelListen(this);
		
		initTop();
		initCenter();
		initBottom();
		this.add(topPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 *上面板方法
	 */
	public void initTop() {
		topPanel = new JPanel();// 上
		topPanel.setBorder(BorderFactory.createTitledBorder("用户查询"));

		Box topBox = Box.createHorizontalBox();

		JLabel idLabel = new JLabel("用户ID: ");
		idField = new JTextField();
		idField.setPreferredSize(new Dimension(130, 18));

		JLabel nameLabel = new JLabel("姓 名: ");
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(130, 18));

		JLabel onlineLabel = new JLabel("状 态: ");
		onLineComboBox = new JComboBox();
		onLineComboBox.setPreferredSize(new Dimension(80, 18));
		onLineComboBox.addItem("选择");
		onLineComboBox.addItem("在线");
		onLineComboBox.addItem("不在线");

		selectButton = new JButton("查 询(S)");
		selectButton.setMnemonic('s');// 加下划线
		selectButton.addActionListener(userListen);

		topBox.add(idLabel);
		topBox.add(idField);
		topBox.add(Box.createHorizontalStrut(15));
		topBox.add(nameLabel);
		topBox.add(nameField);
		topBox.add(Box.createHorizontalStrut(15));
		topBox.add(onlineLabel);
		topBox.add(onLineComboBox);
		topBox.add(Box.createHorizontalStrut(15));
		topBox.add(selectButton);

		topPanel.add(topBox);
	}

	/**
	 *中面板方法
	 */
	public void initCenter() {
		centerPanel = new JPanel(new BorderLayout());// 中

		Vector<Vector<String>> rows =userDao.selectUser("", "", -1);
		colomn = new Vector<String>();

		colomn.add("ID");
		colomn.add("姓名");
		colomn.add("密码");
		colomn.add("性别");
		colomn.add("年龄");
		colomn.add("地址");
		colomn.add("是否在线");
		colomn.add("注册时间");

		dataModel = new DefaultTableModel(rows, colomn) {

			private static final long serialVersionUID = 0L;

			// 重写isCellEditable ,使表格不可编辑
			public boolean isCellEditable(int row, int col) {

				return false;
			}

		};

		userTable = new JTable(dataModel);

		ListSelectionModel listModel = userTable.getSelectionModel();
		listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 设置表格只能选择一行

		listModel.addListSelectionListener(new ListSelectionListener() {// 对表格添加监听

					public void valueChanged(ListSelectionEvent arg0) {

						if (userTable.getSelectedRow() >= 0) {
							// 设置按钮可点
							updateButton.setEnabled(true);
							deleteButton.setEnabled(true);
							rePswButton.setEnabled(true);
						} else {
							// 设置按钮不可点
							updateButton.setEnabled(false);
							deleteButton.setEnabled(false);
							rePswButton.setEnabled(false);
						}

					}

				});

		userTable.getTableHeader().setReorderingAllowed(false);// 设置表头不能动
		userTable.getTableHeader().setBackground(Color.lightGray);// 设置表头颜色
		JScrollPane jspane = new JScrollPane(userTable);
		centerPanel.add(jspane, BorderLayout.CENTER);
	}

	/**
	 *下面板方法
	 */
	public void initBottom() {
		bottomPanel = new JPanel();// 下

		Box buttobBox = Box.createHorizontalBox();

		insertButton = new JButton("添 加");	
		updateButton = new JButton("修 改");
		updateButton.setEnabled(false);
		deleteButton = new JButton("删 除");
		deleteButton.setEnabled(false);
		rePswButton = new JButton("重置密码");
		rePswButton.setEnabled(false);
		rePswAllButton = new JButton("重置所有密码");
		
		insertButton.addActionListener(userListen);
		updateButton.addActionListener(userListen);
		deleteButton.addActionListener(userListen);
		rePswButton.addActionListener(userListen);
		rePswAllButton.addActionListener(userListen);

		buttobBox.add(insertButton);
		buttobBox.add(Box.createHorizontalStrut(10));
		buttobBox.add(updateButton);
		buttobBox.add(Box.createHorizontalStrut(10));
		buttobBox.add(deleteButton);
		buttobBox.add(Box.createHorizontalStrut(10));
		buttobBox.add(rePswButton);
		buttobBox.add(Box.createHorizontalStrut(10));
		buttobBox.add(rePswAllButton);

		bottomPanel.add(buttobBox);
	}

	/**
	 * 更用户列表的方法
	 */
	public void updateUserData()
	{
		userDao = new UserDaoImpl();
		Vector<Vector<String>> data = userDao.selectUser("", "", -1);
		dataModel.setDataVector(data, colomn);
		dataModel.fireTableDataChanged();
	}
	
	/**
	 * 获取表头的值
	 * @return Vector<String> 表头的值
	 */
	public Vector<String> getColomn() {
		return colomn;
	}

	/**
	 * 获取表模型的值
	 * @return DefaultTableModel 表模型的值
	 */
	public DefaultTableModel getDataModel() {
		return dataModel;
	}

	/**
	 * 获取删除按钮的值
	 * @return JButton 删除按钮的值
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * 获取ID文本框的值
	 * @return JTextField ID文本框的值
	 */
	public JTextField getIdField() {
		return idField;
	}

	/**
	 * 获取添加按钮的值
	 * @return JButton 添加按钮的值
	 */
	public JButton getInsertButton() {
		return insertButton;
	}

	/**
	 * 获取姓名文本框的值
	 * @return JTextField 姓名文本框的值
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * 获取状态下拉框的值
	 * @return JComboBox 状态下拉框的值
	 */
	public JComboBox getOnLineComboBox() {
		return onLineComboBox;
	}

	/**
	 * 获取重置密码按钮的值
	 * @return JButton 重置密码按钮的值
	 */
	public JButton getRePswAllButton() {
		return rePswAllButton;
	}

	/**
	 * 获取重置所有密码按钮的值
	 * @return JButton 重置所有密码按钮的值
	 */
	public JButton getRePswButton() {
		return rePswButton;
	}

	/**
	 * 获取查询按钮的值
	 * @return JButton 查询按钮的值
	 */
	public JButton getSelectButton() {
		return selectButton;
	}

	/**
	 * 获取修改按钮的值
	 * @return JButton 修改按钮的值
	 */
	public JButton getUpdateButton() {
		return updateButton;
	}

	/**
	 * 获取用户表的值
	 * @return JTable 用户表的值
	 */
	public JTable getUserTable() {
		return userTable;
	}


}
