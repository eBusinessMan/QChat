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
 * ������������
 * @author luozhixiao
 *
 */
public class ServerPanel extends JPanel {

	private static final long serialVersionUID = 5533189404412973811L;

	/** �ϱ���� */
	private JPanel topPanel;

	/** �±���� */
	private JPanel bottomPanel;

	/** ��ť��� */
	private JPanel buttonPanel;

	/** ͨѶ��Ϣ��� */
	private JPanel infoPanel;

	/** ������� */
	private JPanel bulletinPanel;

	/** ���͹��水ť */
	private JButton sendBulletinBtn;

	/** ��ʼ����ť */
	private JButton startSeverBtn;

	/** ֹͣ����ť */
	private JButton stopServerBtn;

	/** ǿ�����߰�ť */
	private JButton coerceDownBtn;

	/** ������־�ı��� */
	private JTextArea serverLogArea;

	/** ���͹����ı��� */
	private JTextArea sendBulletinArea;

	/** ͼƬ��ǩ */
	private JLabel imageLabel;

	/** ��ͷ */
	private Vector<String> colomn;

	/** ��ģ�� */
	private DefaultTableModel dataModel;

	/** ��� */
	private JTable userTable;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	/**
	 * ���췽��
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
	 * ������巽��
	 */
	public void initTopPanel() {

		topPanel = new JPanel(new BorderLayout());

		buttonPanel = new JPanel(new BorderLayout());

		Box bBox = Box.createHorizontalBox();
		Box buttonBox = Box.createVerticalBox();

		ServerPanelListen serListen = new ServerPanelListen(this);
		startSeverBtn = new JButton("��������");// ��ʼ����ť
		stopServerBtn = new JButton("ֹͣ����");// ֹͣ����ť
		coerceDownBtn = new JButton("ǿ������");// ǿ�����߰�ť
		sendBulletinBtn = new JButton("��������");// ���͹��水ť

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
		imageLabel.setIcon(QQUtils.serverstop);// ����Ϊ����ֹͣͼƬ
		JPanel serverPanel = new JPanel(new BorderLayout());
		serverPanel.setBorder(BorderFactory.createTitledBorder("����������"));

		serverPanel.add(imageLabel, BorderLayout.CENTER);
		serverPanel.add(buttonPanel, BorderLayout.EAST);

		infoPanel = new JPanel(new BorderLayout());// ͨѶ��Ϣ
		infoPanel.setBorder(BorderFactory.createTitledBorder("ͨ����Ϣ��ʾ"));
		serverLogArea = new JTextArea(3, 60);
		serverLogArea.setEditable(false);
		serverLogArea.setLineWrap(true);
		serverLogArea.setBackground(Color.white);
		JScrollPane jspInfo = new JScrollPane(serverLogArea);
		infoPanel.add(jspInfo, BorderLayout.CENTER);

		bulletinPanel = new JPanel(new BorderLayout());// ����
		bulletinPanel.setBorder(BorderFactory.createTitledBorder("���淢��"));

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
	 * �ײ���巽��
	 */
	public void initBottomPanel() {

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createTitledBorder("�����û�"));

		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		colomn = new Vector<String>();// �ű�ͷ�ļ���
		colomn.add("ID");
		colomn.add("����");
		colomn.add("�Ա�");
		colomn.add("����");
		colomn.add("��ַ");
		colomn.add("�Ƿ�����");
		colomn.add("ע��ʱ��");

		dataModel = new DefaultTableModel(rows, colomn) {

			private static final long serialVersionUID = 0L;

			// ��дisCellEditable ,ʹ��񲻿ɱ༭
			public boolean isCellEditable(int row, int col) {

				return false;
			}

		};

		userTable = new JTable(dataModel);

		updateOnlineUserData();// ���������û��б�
		userTable = new JTable(dataModel);// ���Ĭ�ϱ�ģ�ͣ�Ĭ�ϱ�ģ�ͷű����еļ���

		ListSelectionModel listModel = userTable.getSelectionModel();
		listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ���ñ��ֻ��ѡ��һ��


		userTable.getTableHeader().setReorderingAllowed(false);// ���ñ�ͷ���ܶ�
		userTable.getTableHeader().setBackground(Color.lightGray);// ���ñ�ͷ��ɫ

		JScrollPane jspane = new JScrollPane(userTable);// �ѱ��ٷŵ�����������
		bottomPanel.add(jspane, BorderLayout.CENTER);

	}

	/**
	 * ���������û�����
	 */
	public void updateOnlineUserData() {
		// ������ݵ�Vector,������list��Ū��ת����Vector����
		Vector<Vector<String>> date = new Vector<Vector<String>>();

		List<User> list = new ArrayList<User>();
		list = userDao.getOnlineUser(); // ��ȡ��ǰ�����û�
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

		// ���·��������û��б�
		dataModel.setDataVector(date, colomn);
		dataModel.fireTableDataChanged();
	}

	/**
	 * ��ȡ���͹��水ť��ֵ��
	 * 
	 * @return JButton ���͹��水ť��ֵ��
	 */
	public JButton getSendBulletinBtn() {
		return sendBulletinBtn;
	}

	/**
	 * ��ȡ��ʼ����ť��ֵ��
	 * 
	 * @return JButton ��ʼ����ť��ֵ��
	 */
	public JButton getStartSeverBtn() {
		return startSeverBtn;
	}

	/**
	 * ��ȡֹͣ����ť��ֵ��
	 * 
	 * @return JButton ֹͣ����ť��ֵ��
	 */
	public JButton getStopServerBtn() {
		return stopServerBtn;
	}

	/**
	 * ��ȡͼƬ��ǩ��ֵ��
	 * 
	 * @return JLabel ͼƬ��ǩ��ֵ��
	 */
	public JLabel getImageLabel() {
		return imageLabel;
	}

	/**
	 * ��ȡǿ�����߰�ť��ֵ��
	 * 
	 * @return JButton ǿ�����߰�ť��ֵ��
	 */
	public JButton getCoerceDownBtn() {
		return coerceDownBtn;
	}

	/**
	 * ��ȡ���͹����ı����ֵ��
	 * 
	 * @return JTextArea ���͹����ı����ֵ��
	 */
	public JTextArea getSendBulletinArea() {
		return sendBulletinArea;
	}

	/**
	 * ��ȡ������־�ı����ֵ��
	 * 
	 * @return JTextArea ������־�ı����ֵ��
	 */
	public JTextArea getServerLogArea() {
		return serverLogArea;
	}

	/**
	 * ��ȡĬ�ϱ�ģ��
	 * @return DefaultTableModel ��ģ��
	 */
	public DefaultTableModel getDataModel() {
		return dataModel;
	}

	/**
	 * ��ȡ�û���
	 * @return JTable �û���
	 */
	public JTable getUserTable() {
		return userTable;
	}
}