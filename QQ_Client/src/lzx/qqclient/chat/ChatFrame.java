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
 * ������������
 *@author luozhixiao
 *
 */
public class ChatFrame extends JFrame {

	private static final long serialVersionUID = -332337780087298192L;

	/** ������ */
	private JPanel leftPanel;

	/** �ұ���� */
	private JPanel rightPanel;

	/** �����¼��� */
	private JPanel chatLogPanel;

	/** ������ */
	private JTextArea bulletinArea;
	
	/** ��������ʾ�ı��� */
	private JTextField upDownLineField;

	/** ��ʾ�����ı��� */
	private JTextArea contentArea;

	/** �����ı��� */
	private JTextArea sendArea;

	/** �����¼�ı��� */
	private JTextArea chatLogArea;

	/** ��˭˵�ı�ǩ */
	private JLabel toLabel;

	/** ���Ͱ�ť */
	private JButton sendButton;

	/** �رհ�ť */
	private JButton closeButton;

	/** ���� */
	private JButton fontButton;

	/** ���� */
	private JButton faceButton;

	/** �����¼��ť */
	private JButton chatLogButton;

	/** �޸����밴ť */
	private JButton amendPswButton;

	/** �ָ��� */
	private JSplitPane spp;

	/** �û��б� */
	private JList userList;

	/** �û����ģ�� */
	private DefaultListModel userModel;

	/** �ָ��� */
	private JScrollPane jsList;

	/** �û��Լ������ϡ� */
	private User user;

	/** ��¼���� */
	private LoginListen logListen;
	
	/** ���������� */
	private ChatFrameListen chatListen;
	
	private ChatUtils chat;
	
	private ChatFrame chFrame;

	/**
	 * ���캯��
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
		this.setTitle("  ����������");
		this.setIconImage(QQUtils.qqicon);// ����QQͼ��
		this.setSize(560, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// �����û����߰���
				TCPPack qqPack = new TCPPack();
				qqPack.setType(QQPackType.USER_WODNLINE);
				qqPack.setFrom(chFrame.user.getSid());
				try {
					chFrame.chat.setTcpPack(qqPack);
					chFrame.chat.sendObject();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				//�ͷſͻ�����Դ��
				try {
					chFrame.chat.getSocket().close();		
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				System.exit(0); // �˳�����

			}
		});
	}
	

	/**
	 *������
	 */
	public void initLeftPanel() {

		leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(new Color(235, 250, 255));

		Box leftBox = Box.createVerticalBox();// new�������������
		// -------------------------------
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(235, 250, 255));
		toLabel = new JLabel("To:");
		Box pswBox = Box.createHorizontalBox();
		amendPswButton = new JButton("�޸�����");
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
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));// �ű���
		groupPanel.setBackground(new Color(235, 250, 255));
		Box toolBox = Box.createHorizontalBox();

		fontButton = new JButton();// ����s
		fontButton.setIcon(QQUtils.setfont);
		fontButton.setBorder(null);
		fontButton.setBackground(new Color(230, 250, 250));
		fontButton.setContentAreaFilled(false);
		fontButton.setRolloverIcon(QQUtils.setfontover);

		faceButton = new JButton();// ����
		faceButton.setIcon(QQUtils.setface);
		faceButton.setBorder(BorderFactory.createEmptyBorder());
		faceButton.setBackground(new Color(230, 250, 250));
		faceButton.setContentAreaFilled(false);
		faceButton.setRolloverIcon(QQUtils.setfaceover);

		faceButton.setBorder(null);

		chatLogButton = new JButton("�����¼");
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

		sendButton = new JButton("����(S)");// ���Ͱ�ť
		sendButton.setIcon(QQUtils.normal);
		sendButton.setHorizontalTextPosition(JButton.CENTER);
		sendButton.setBorder(null);
		sendButton.setRolloverIcon(QQUtils.over);
		
		sendButton.addActionListener(chatListen);

		closeButton = new JButton("�ر�(C)");// �رհ�ť
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
	 * �ұ����
	 */
	public void initRightPanel() {

		rightPanel = new JPanel(new BorderLayout());

		bulletinArea = new JTextArea(7, 5);// ������
		bulletinArea.setLineWrap(true);
		JScrollPane jsBulletin = new JScrollPane(bulletinArea);
		jsBulletin.setBackground(new Color(235, 250, 255));
		JTabbedPane jtab = new JTabbedPane();
		jtab.setBackground(new Color(235, 250, 255));
		jtab.add("������", jsBulletin);

		JPanel  upDownLinePanel = new JPanel(new BorderLayout());
	//	upDownLinePanel.setBorder(BorderFactory.create);
		upDownLineField = new JTextField(10);
		upDownLineField.setEditable(false);
		upDownLineField.setBackground(new Color(235, 250, 255));
		upDownLinePanel.add(upDownLineField,BorderLayout.CENTER);
		
		JPanel userListPanel = new JPanel(new BorderLayout());
		// ---------------------------------
		userListPanel.setBackground(new Color(235, 250, 255));
		userListPanel.setBorder(BorderFactory.createTitledBorder("�û��б�"));
		/**
		 * �����û��б����
		 */
		userList = new JList();

		userModel = new DefaultListModel();

		userList.setModel((ListModel) userModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// �����б�ֻ�ܵ�ѡ

		userList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {

				int i = userList.getSelectedIndex();
				if (i == 0 && e.getValueIsAdjusting()) {

					toLabel.setText("To: ������");

				} else if (i != -1 && e.getValueIsAdjusting()) {

					String user = userList.getSelectedValue().toString();
					toLabel.setText("To: " + user);
				}
			}
		});

		// �������û��б����뵽����������
		jsList = new JScrollPane(userList);
		userListPanel.add(jsList, BorderLayout.CENTER);

		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upDownLinePanel,
				userListPanel);
		
		
		JSplitPane spp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jtab,
				jsp);
		rightPanel.add(spp1, BorderLayout.CENTER);
	}

	/**
	 * �����¼���
	 */
	public void initChatLogPanel() {

		// �ұ�
		chatLogPanel = new JPanel(new BorderLayout());

		chatLogArea = new JTextArea(7, 25);// �����¼
		chatLogArea.setLineWrap(true);
		chatLogArea.setEditable(false);

		JScrollPane jsChat = new JScrollPane(chatLogArea);
		JTabbedPane jtab = new JTabbedPane();

		jtab.setBackground(new Color(235, 250, 255));
		jtab.add("�����¼", jsChat);

		chatLogPanel.add(jtab, BorderLayout.CENTER);

	}

	/** 
	 * ��ȡ�޸����밴ť��ֵ
	 * @return JButton �޸����밴ť��ֵ
	 */
	public JButton getAmendPswButton() {
		return amendPswButton;
	}

	/** 
	 * ��ȡ�����¼��ť��ֵ
	 * @return JButton �����¼��ť��ֵ
	 */
	public JButton getChatLogButton() {
		return chatLogButton;
	}
	/** 
	 * ��ȡ�����¼����ֵ
	 * @return JPanel �����¼����ֵ
	 */
	public JPanel getChatLogPanel() {
		return chatLogPanel;
	}

	/** 
	 * ��ȡ�ұ�����ֵ
	 * @return JPanel �ұ�����ֵ
	 */
	public JPanel getRightPanel() {
		return rightPanel;
	}

	/** 
	 * ��ȡ�ָ�����ֵ
	 * @return JSplitPane �ָ�����ֵ
	 */
	public JSplitPane getSpp() {
		return spp;
	}

	/** 
	 * ��ȡ�رհ�ť��ֵ
	 * @return JButton �رհ�ť��ֵ
	 */
	public JButton getCloseButton() {
		return closeButton;
	}

	/** 
	 * ��ȡ��ʱ��¼�ı����ֵ
	 * @return JTextArea ��ʱ��¼�ı����ֵ
	 */
	public JTextArea getContentArea() {
		return contentArea;
	}

	/** 
	 * ��ȡ�����ı����ֵ
	 * @return JTextArea �����ı����ֵ
	 */
	public JTextArea getSendArea() {
		return sendArea;
	}

	/** 
	 * ��ȡ���Ͱ�ť��ֵ
	 * @return JButton ���Ͱ�ť��ֵ
	 */
	public JButton getSendButton() {
		return sendButton;
	}

	/** 
	 * ��ȡ�û��б��ֵ
	 * @return JButton �û��б��ֵ
	 */
	public JList getUserList() {
		return userList;
	}

	/** 
	 * ��ȡ�����ı����ֵ
	 * @return JTextArea �����ı����ֵ
	 */
	public JTextArea getBulletinArea() {
		return bulletinArea;
	}

	/** 
	 * ��ȡ�û���ֵ
	 * @return User �û���ֵ
	 */
	public User getUser() {
		return user;
	}


	/**
	 * ��ȡ�������ı����ֵ
	 * @return JTextField �������ı����ֵ
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
