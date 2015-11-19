package lzx.qqserver.serverManger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import lzx.qqserver.dao.UserDaoImpl;
import lzx.qqserver.tools.QQUtils;

import pub.User;


/**
 * 服务管理面板类
 * @author luozhixiao
 *
 */
public class ServerPanel extends JPanel {

	private static final long serialVersionUID = 5533189404412973811L;

	/** 上边面板 */
	private JPanel topPanel;

	/** 下边面板 */
	private JPanel bottomPanel;

	/** 按钮面板 */
	private JPanel buttonPanel;

	/** 通讯信息面板 */
	private JPanel infoPanel;

	/** 公告面板 */
	private JPanel bulletinPanel;

	/** 发送公告按钮 */
	private JButton sendBulletinBtn;

	/** 开始服务按钮 */
	private JButton startSeverBtn;

	/** 停止服务按钮 */
	private JButton stopServerBtn;

	/** 强制下线按钮 */
	private JButton coerceDownBtn;

	/** 服务日志文本框 */
	private JTextArea serverLogArea;

	/** 发送公告文本框 */
	private JTextArea sendBulletinArea;

	/** 图片标签 */
	private JLabel imageLabel;

	/** 表头 */
	private Vector<String> colomn;

	/** 表模型 */
	private DefaultTableModel dataModel;

	/** 表格 */
	private JTable userTable;

	/** 用户的数据存储对象 */
	private UserDaoImpl userDao;

	/**
	 * 构造方法
	 */
	public ServerPanel() {
		this.setLayout(new BorderLayout());
		userDao = new UserDaoImpl();

		initTopPanel();
		initBottomPanel();

		JSplitPane spp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel,
				bottomPanel);
		spp.setDividerLocation(200);
		spp.setEnabled(false);

		this.add(spp, BorderLayout.CENTER);
	}

	/**
	 * 顶部面板方法
	 */
	public void initTopPanel() {

		topPanel = new JPanel(new BorderLayout());

		buttonPanel = new JPanel(new BorderLayout());

		Box bBox = Box.createHorizontalBox();
		Box buttonBox = Box.createVerticalBox();

		ServerPanelListen serListen = new ServerPanelListen(this);
		startSeverBtn = new JButton("启动服务");// 开始服务按钮
		stopServerBtn = new JButton("停止服务");// 停止服务按钮
		coerceDownBtn = new JButton("强制下线");// 强制下线按钮
		sendBulletinBtn = new JButton("发布公告");// 发送公告按钮

		coerceDownBtn.setEnabled(false);
		stopServerBtn.setEnabled(false);
		sendBulletinBtn.setEnabled(false);
		
		startSeverBtn.addActionListener(serListen);
		stopServerBtn.addActionListener(serListen);
		coerceDownBtn .addActionListener(serListen);
		sendBulletinBtn.addActionListener(serListen);


		buttonBox.add(Box.createVerticalStrut(5));
		buttonBox.add(startSeverBtn);
		buttonBox.add(Box.createVerticalStrut(5));
		buttonBox.add(stopServerBtn);
		buttonBox.add(Box.createVerticalStrut(5));
		buttonBox.add(coerceDownBtn);
		buttonBox.add(Box.createVerticalStrut(5));
		buttonBox.add(sendBulletinBtn);
		buttonBox.add(Box.createVerticalStrut(5));

		bBox.add(buttonBox);
		bBox.add(Box.createHorizontalStrut(10));

		buttonPanel.add(bBox, BorderLayout.CENTER);

		imageLabel = new JLabel();
		imageLabel.setIcon(QQUtils.serverstop);// 设置为服务停止图片
		JPanel serverPanel = new JPanel(new BorderLayout());
		serverPanel.setBorder(BorderFactory.createTitledBorder("服务器管理"));

		serverPanel.add(imageLabel, BorderLayout.CENTER);
		serverPanel.add(buttonPanel, BorderLayout.EAST);

		infoPanel = new JPanel(new BorderLayout());// 通讯信息
		infoPanel.setBorder(BorderFactory.createTitledBorder("通信信息提示"));
		serverLogArea = new JTextArea(3, 60);
		serverLogArea.setEditable(false);
		serverLogArea.setLineWrap(true);
		serverLogArea.setBackground(Color.white);
		JScrollPane jspInfo = new JScrollPane(serverLogArea);
		infoPanel.add(jspInfo, BorderLayout.CENTER);

		bulletinPanel = new JPanel(new BorderLayout());// 公告
		bulletinPanel.setBorder(BorderFactory.createTitledBorder("公告发送"));

		sendBulletinArea = new JTextArea(3, 60);
		sendBulletinArea.setEditable(false);
		sendBulletinArea.setLineWrap(true);
		sendBulletinArea.setBackground(Color.white);
		JScrollPane jspBulletin = new JScrollPane(sendBulletinArea);
		bulletinPanel.add(jspBulletin, BorderLayout.CENTER);

		JSplitPane spp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, infoPanel,
				bulletinPanel);
		spp2.setDividerLocation(100);
		spp2.setEnabled(false);

		JSplitPane spp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spp2,
				serverPanel);
		spp1.setDividerLocation(420);
		spp1.setEnabled(false);
		topPanel.add(spp1, BorderLayout.CENTER);

	}

	/**
	 * 底部面板方法
	 */
	public void initBottomPanel() {

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createTitledBorder("在线用户"));

		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		colomn = new Vector<String>();// 放表头的集合
		colomn.add("ID");
		colomn.add("姓名");
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

		updateOnlineUserData();// 更新在线用户列表
		userTable = new JTable(dataModel);// 表放默认表模型，默认表模型放表行列的集合

		ListSelectionModel listModel = userTable.getSelectionModel();
		listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 设置表格只能选择一行


		userTable.getTableHeader().setReorderingAllowed(false);// 设置表头不能动
		userTable.getTableHeader().setBackground(Color.lightGray);// 设置表头颜色

		JScrollPane jspane = new JScrollPane(userTable);// 把表再放到滚动条里面
		bottomPanel.add(jspane, BorderLayout.CENTER);

	}

	/**
	 * 更新在线用户方法
	 */
	public void updateOnlineUserData() {
		// 存放数据的Vector,用来从list类弄中转换到Vector类型
		Vector<Vector<String>> date = new Vector<Vector<String>>();

		List<User> list = new ArrayList<User>();
		list = userDao.getOnlineUser(); // 获取当前在线用户
		for (User user : list) {
			Vector<String> tempVect = new Vector<String>();

			tempVect.add(user.getSid());
			tempVect.add(user.getSname());
			tempVect.add(user.getSsex());
			tempVect.add(String.valueOf(user.getNage()));
			tempVect.add(user.getSaddress());
			tempVect.add(user.getNisonline());
			tempVect.add(user.getDregTime());
			date.add(tempVect);
		}

		// 更新服务在线用户列表
		dataModel.setDataVector(date, colomn);
		dataModel.fireTableDataChanged();
	}

	/**
	 * 获取发送公告按钮的值。
	 * 
	 * @return JButton 发送公告按钮的值。
	 */
	public JButton getSendBulletinBtn() {
		return sendBulletinBtn;
	}

	/**
	 * 获取开始服务按钮的值。
	 * 
	 * @return JButton 开始服务按钮的值。
	 */
	public JButton getStartSeverBtn() {
		return startSeverBtn;
	}

	/**
	 * 获取停止服务按钮的值。
	 * 
	 * @return JButton 停止服务按钮的值。
	 */
	public JButton getStopServerBtn() {
		return stopServerBtn;
	}

	/**
	 * 获取图片标签的值。
	 * 
	 * @return JLabel 图片标签的值。
	 */
	public JLabel getImageLabel() {
		return imageLabel;
	}

	/**
	 * 获取强制下线按钮的值。
	 * 
	 * @return JButton 强制下线按钮的值。
	 */
	public JButton getCoerceDownBtn() {
		return coerceDownBtn;
	}

	/**
	 * 获取发送公告文本框的值。
	 * 
	 * @return JTextArea 发送公告文本框的值。
	 */
	public JTextArea getSendBulletinArea() {
		return sendBulletinArea;
	}

	/**
	 * 获取服务日志文本框的值。
	 * 
	 * @return JTextArea 服务日志文本框的值。
	 */
	public JTextArea getServerLogArea() {
		return serverLogArea;
	}

	/**
	 * 获取默认表模型
	 * @return DefaultTableModel 表模型
	 */
	public DefaultTableModel getDataModel() {
		return dataModel;
	}

	/**
	 * 获取用户表
	 * @return JTable 用户表
	 */
	public JTable getUserTable() {
		return userTable;
	}
}