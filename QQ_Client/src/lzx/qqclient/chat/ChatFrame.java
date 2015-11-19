package lzx.qqclient.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lzx.qqclient.business.ChatUtils;
import lzx.qqclient.login.LoginListen;
import lzx.qqclient.thread.ClientReceiveThread;
import lzx.qqclient.tools.QQUtils;


import pub.TCPPack;
import pub.QQPackType;
import pub.User;

/**
 * 聊天主界面类
 *@author luozhixiao
 *
 */
public class ChatFrame extends JFrame {

	private static final long serialVersionUID = -332337780087298192L;

	/** 左边面板 */
	private JPanel leftPanel;

	/** 右边面板 */
	private JPanel rightPanel;

	/** 聊天记录面板 */
	private JPanel chatLogPanel;

	/** 公告栏 */
	private JTextArea bulletinArea;
	
	/** 上下线提示文本框 */
	private JTextField upDownLineField;

	/** 显示内容文本框 */
	private JTextArea contentArea;

	/** 发送文本框 */
	private JTextArea sendArea;

	/** 聊天记录文本框 */
	private JTextArea chatLogArea;

	/** 对谁说的标签 */
	private JLabel toLabel;

	/** 发送按钮 */
	private JButton sendButton;

	/** 关闭按钮 */
	private JButton closeButton;

	/** 字体 */
	private JButton fontButton;

	/** 表情 */
	private JButton faceButton;

	/** 聊天记录按钮 */
	private JButton chatLogButton;

	/** 修改密码按钮 */
	private JButton amendPswButton;

	/** 分隔条 */
	private JSplitPane spp;

	/** 用户列表 */
	private JList userList;

	/** 用户别表模型 */
	private DefaultListModel userModel;

	/** 分隔条 */
	private JScrollPane jsList;

	/** 用户自己的资料。 */
	private User user;

	/** 登录监听 */
	private LoginListen logListen;
	
	/** 聊天界面监听 */
	private ChatFrameListen chatListen;
	
	private ChatUtils chat;
	
	private ChatFrame chFrame;

	/**
	 * 构造函数
	 */
	public ChatFrame(User user ,ChatUtils chat) {
		chFrame = this;
		this.chat=chat;
		chatListen = new ChatFrameListen(this,chat);
		this.user =user;
		this.chat=chat;
		this.setLayout(new BorderLayout());
		
		initLeftPanel();
		initRightPanel();
		initChatLogPanel();

		spp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		spp.setDividerLocation(360);
		spp.setEnabled(false);
		
		ClientReceiveThread clientThread = new ClientReceiveThread(chat);
		clientThread.start();
		
		this.add(spp, BorderLayout.CENTER);
		this.setTitle("  聊天主窗口");
		this.setIconImage(QQUtils.qqicon);// 设置QQ图标
		this.setSize(560, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// 发送用户下线包。
				TCPPack qqPack = new TCPPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(chFrame.user.getSid());
				try {
					chFrame.chat.setTcpPack(qqPack);
					chFrame.chat.sendObject();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				//释放客户端资源。
				try {
					chFrame.chat.getSocket().close();		
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				System.exit(0); // 退出程序

			}
		});
	}
	

	/**
	 *左边面板
	 */
	public void initLeftPanel() {

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(new Color(235, 250, 255));

		Box leftBox = Box.createVerticalBox();// new左边竖着最大盒子
		// -------------------------------
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(235, 250, 255));
		toLabel = new JLabel("To:");
		Box pswBox = Box.createHorizontalBox();
		amendPswButton = new JButton("修改密码");
		amendPswButton.addActionListener(chatListen);
		amendPswButton.setBackground(new Color(235, 250, 255));

		amendPswButton.setIcon(QQUtils.normal);
		amendPswButton.setHorizontalTextPosition(JButton.CENTER);
		amendPswButton.setBorder(null);
		amendPswButton.setRolloverIcon(QQUtils.over);
		pswBox.add(amendPswButton);
		pswBox.add(Box.createHorizontalStrut(7));

		topPanel.add(toLabel, BorderLayout.WEST);
		topPanel.add(pswBox, BorderLayout.EAST);
		// ----------------------------------
		contentArea = new JTextArea(19, 0);
		contentArea.setLineWrap(true);
		contentArea.setBackground(Color.WHITE);
		JScrollPane jsContent = new JScrollPane(contentArea);
		// -----------------------------------
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));// 放表情
		groupPanel.setBackground(new Color(235, 250, 255));
		Box toolBox = Box.createHorizontalBox();

		fontButton = new JButton();// 字体s
		fontButton.setIcon(QQUtils.setfont);
		fontButton.setBorder(null);
		fontButton.setBackground(new Color(230, 250, 250));
		fontButton.setContentAreaFilled(false);
		fontButton.setRolloverIcon(QQUtils.setfontover);

		faceButton = new JButton();// 表情
		faceButton.setIcon(QQUtils.setface);
		faceButton.setBorder(BorderFactory.createEmptyBorder());
		faceButton.setBackground(new Color(230, 250, 250));
		faceButton.setContentAreaFilled(false);
		faceButton.setRolloverIcon(QQUtils.setfaceover);

		faceButton.setBorder(null);

		chatLogButton = new JButton("聊天记录");
		chatLogButton.addActionListener(chatListen);
		chatLogButton.setIcon(QQUtils.normal);
		chatLogButton.setHorizontalTextPosition(JButton.CENTER);
		chatLogButton.setBorder(null);
		chatLogButton.setRolloverIcon(QQUtils.over);


		toolBox.add(fontButton);
		toolBox.add(faceButton);
		toolBox.add(Box.createHorizontalStrut(230));
		toolBox.add(chatLogButton);

		groupPanel.add(toolBox);
		// _----------------------------------
		sendArea = new JTextArea(5, 0);
		sendArea.setLineWrap(true);
		JScrollPane jsSend = new JScrollPane(sendArea);
		jsSend.setBackground(new Color(235, 250, 255));

		// ------------------------------------------
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(235, 250, 255));

		Box buttonBox = Box.createHorizontalBox();

		sendButton = new JButton("发送(S)");// 发送按钮
		sendButton.setIcon(QQUtils.normal);
		sendButton.setHorizontalTextPosition(JButton.CENTER);
		sendButton.setBorder(null);
		sendButton.setRolloverIcon(QQUtils.over);
		
		sendButton.addActionListener(chatListen);

		closeButton = new JButton("关闭(C)");// 关闭按钮
		closeButton.setIcon(QQUtils.normal);
		closeButton.setHorizontalTextPosition(JButton.CENTER);
		closeButton.setBorder(null);
		closeButton.setRolloverIcon(QQUtils.over);
		
		closeButton.addActionListener(chatListen);

		buttonBox.add(Box.createHorizontalStrut(185));
		buttonBox.add(closeButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(sendButton);
		buttonPanel.add(buttonBox);
		// -------------------------------------
		leftBox.add(Box.createVerticalStrut(5));
		leftBox.add(topPanel);
		leftBox.add(Box.createVerticalStrut(5));
		leftBox.add(jsContent);
		leftBox.add(groupPanel);
		leftBox.add(jsSend);
		leftBox.add(Box.createVerticalStrut(5));
		leftBox.add(buttonPanel);

		leftPanel.add(leftBox, BorderLayout.CENTER);
	}

	/**
	 * 右边面板
	 */
	public void initRightPanel() {

		rightPanel = new JPanel(new BorderLayout());

		bulletinArea = new JTextArea(7, 5);// 公告栏
		bulletinArea.setLineWrap(true);
		JScrollPane jsBulletin = new JScrollPane(bulletinArea);
		jsBulletin.setBackground(new Color(235, 250, 255));
		JTabbedPane jtab = new JTabbedPane();
		jtab.setBackground(new Color(235, 250, 255));
		jtab.add("公告栏", jsBulletin);

		JPanel  upDownLinePanel = new JPanel(new BorderLayout());
	//	upDownLinePanel.setBorder(BorderFactory.create);
		upDownLineField = new JTextField(10);
		upDownLineField.setEditable(false);
		upDownLineField.setBackground(new Color(235, 250, 255));
		upDownLinePanel.add(upDownLineField,BorderLayout.CENTER);
		
		JPanel userListPanel = new JPanel(new BorderLayout());
		// ---------------------------------
		userListPanel.setBackground(new Color(235, 250, 255));
		userListPanel.setBorder(BorderFactory.createTitledBorder("用户列表"));
		/**
		 * 在线用户列表面板
		 */
		userList = new JList();

		userModel = new DefaultListModel();

		userList.setModel((ListModel) userModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 设置列表只能单选

		userList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {

				int i = userList.getSelectedIndex();
				if (i == 0 && e.getValueIsAdjusting()) {

					toLabel.setText("To: 所有人");

				} else if (i != -1 && e.getValueIsAdjusting()) {

					String user = userList.getSelectedValue().toString();
					toLabel.setText("To: " + user);
				}
			}
		});

		// 把在线用户列表框加入到滚动条里面
		jsList = new JScrollPane(userList);
		userListPanel.add(jsList, BorderLayout.CENTER);

		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upDownLinePanel,
				userListPanel);
		
		
		JSplitPane spp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jtab,
				jsp);
		rightPanel.add(spp1, BorderLayout.CENTER);
	}

	/**
	 * 聊天记录面板
	 */
	public void initChatLogPanel() {

		// 右边
		chatLogPanel = new JPanel(new BorderLayout());

		chatLogArea = new JTextArea(7, 25);// 聊天记录
		chatLogArea.setLineWrap(true);
		chatLogArea.setEditable(false);

		JScrollPane jsChat = new JScrollPane(chatLogArea);
		JTabbedPane jtab = new JTabbedPane();

		jtab.setBackground(new Color(235, 250, 255));
		jtab.add("聊天记录", jsChat);

		chatLogPanel.add(jtab, BorderLayout.CENTER);

	}

	/** 
	 * 获取修改密码按钮的值
	 * @return JButton 修改密码按钮的值
	 */
	public JButton getAmendPswButton() {
		return amendPswButton;
	}

	/** 
	 * 获取聊天记录按钮的值
	 * @return JButton 聊天记录按钮的值
	 */
	public JButton getChatLogButton() {
		return chatLogButton;
	}
	/** 
	 * 获取聊天记录面板的值
	 * @return JPanel 聊天记录面板的值
	 */
	public JPanel getChatLogPanel() {
		return chatLogPanel;
	}

	/** 
	 * 获取右边面板的值
	 * @return JPanel 右边面板的值
	 */
	public JPanel getRightPanel() {
		return rightPanel;
	}

	/** 
	 * 获取分隔条的值
	 * @return JSplitPane 分隔条的值
	 */
	public JSplitPane getSpp() {
		return spp;
	}

	/** 
	 * 获取关闭按钮的值
	 * @return JButton 关闭按钮的值
	 */
	public JButton getCloseButton() {
		return closeButton;
	}

	/** 
	 * 获取临时记录文本框的值
	 * @return JTextArea 临时记录文本框的值
	 */
	public JTextArea getContentArea() {
		return contentArea;
	}

	/** 
	 * 获取发送文本框的值
	 * @return JTextArea 发送文本框的值
	 */
	public JTextArea getSendArea() {
		return sendArea;
	}

	/** 
	 * 获取发送按钮的值
	 * @return JButton 发送按钮的值
	 */
	public JButton getSendButton() {
		return sendButton;
	}

	/** 
	 * 获取用户列表的值
	 * @return JButton 用户列表的值
	 */
	public JList getUserList() {
		return userList;
	}

	/** 
	 * 获取公告文本框的值
	 * @return JTextArea 公告文本框的值
	 */
	public JTextArea getBulletinArea() {
		return bulletinArea;
	}

	/** 
	 * 获取用户的值
	 * @return User 用户的值
	 */
	public User getUser() {
		return user;
	}


	/**
	 * 获取上下线文本框的值
	 * @return JTextField 上下线文本框的值
	 */
	public JTextField getUpDownLineField() {
		return upDownLineField;
	}



	public JTextArea getChatLogArea() {
		return chatLogArea;
	}


	public ChatFrame getChFrame() {
		return chFrame;
	}






}
