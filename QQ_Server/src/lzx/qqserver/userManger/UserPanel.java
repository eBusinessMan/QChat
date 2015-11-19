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
 * �û������
 *@author luozhixiao
 *
 */
public class UserPanel extends JPanel {

	private static final long serialVersionUID = 8208034153855470093L;

	/** ���� */
	private JPanel topPanel;

	/** ����� */
	private JPanel centerPanel;

	/** ����� */
	private JPanel bottomPanel;

	/** ��ѯ */
	private JButton selectButton;

	/** ��� */
	private JButton insertButton;

	/** �޸� */
	private JButton updateButton;

	/** ɾ�� */
	private JButton deleteButton;

	/** �������� */
	private JButton rePswButton;

	/** ������������ */
	private JButton rePswAllButton;

	/** ID */
	private JTextField idField;

	/** ���� */
	private JTextField nameField;

	/** ״̬ */
	private JComboBox onLineComboBox;

	/** ��ģ�� */
	private DefaultTableModel dataModel;

	/** ��� */
	private JTable userTable;

	/** �û������ݴ洢���� */
	private UserDaoImpl userDao;

	/** �û�������� */
	private UserPanelListen userListen;

	/** ��ͷ */
	private Vector<String> colomn;



	/**
	 * ���췽��
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
	 *����巽��
	 */
	public void initTop() {
		topPanel = new JPanel();// ��
		topPanel.setBorder(BorderFactory.createTitledBorder("�û���ѯ"));

		Box topBox = Box.createHorizontalBox();

		JLabel idLabel = new JLabel("�û�ID: ");
		idField = new JTextField();
		idField.setPreferredSize(new Dimension(130, 18));

		JLabel nameLabel = new JLabel("�� ��: ");
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(130, 18));

		JLabel onlineLabel = new JLabel("״ ̬: ");
		onLineComboBox = new JComboBox();
		onLineComboBox.setPreferredSize(new Dimension(80, 18));
		onLineComboBox.addItem("ѡ��");
		onLineComboBox.addItem("����");
		onLineComboBox.addItem("������");

		selectButton = new JButton("�� ѯ(S)");
		selectButton.setMnemonic('s');// ���»���
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
	 *����巽��
	 */
	public void initCenter() {
		centerPanel = new JPanel(new BorderLayout());// ��

		Vector<Vector<String>> rows =userDao.selectUser("", "", -1);
		colomn = new Vector<String>();

		colomn.add("ID");
		colomn.add("����");
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

		ListSelectionModel listModel = userTable.getSelectionModel();
		listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ���ñ��ֻ��ѡ��һ��

		listModel.addListSelectionListener(new ListSelectionListener() {// �Ա����Ӽ���

					public void valueChanged(ListSelectionEvent arg0) {

						if (userTable.getSelectedRow() >= 0) {
							// ���ð�ť�ɵ�
							updateButton.setEnabled(true);
							deleteButton.setEnabled(true);
							rePswButton.setEnabled(true);
						} else {
							// ���ð�ť���ɵ�
							updateButton.setEnabled(false);
							deleteButton.setEnabled(false);
							rePswButton.setEnabled(false);
						}

					}

				});

		userTable.getTableHeader().setReorderingAllowed(false);// ���ñ�ͷ���ܶ�
		userTable.getTableHeader().setBackground(Color.lightGray);// ���ñ�ͷ��ɫ
		JScrollPane jspane = new JScrollPane(userTable);
		centerPanel.add(jspane, BorderLayout.CENTER);
	}

	/**
	 *����巽��
	 */
	public void initBottom() {
		bottomPanel = new JPanel();// ��

		Box buttobBox = Box.createHorizontalBox();

		insertButton = new JButton("�� ��");	
		updateButton = new JButton("�� ��");
		updateButton.setEnabled(false);
		deleteButton = new JButton("ɾ ��");
		deleteButton.setEnabled(false);
		rePswButton = new JButton("��������");
		rePswButton.setEnabled(false);
		rePswAllButton = new JButton("������������");
		
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
	 * ���û��б�ķ���
	 */
	public void updateUserData()
	{
		userDao = new UserDaoImpl();
		Vector<Vector<String>> data = userDao.selectUser("", "", -1);
		dataModel.setDataVector(data, colomn);
		dataModel.fireTableDataChanged();
	}
	
	/**
	 * ��ȡ��ͷ��ֵ
	 * @return Vector<String> ��ͷ��ֵ
	 */
	public Vector<String> getColomn() {
		return colomn;
	}

	/**
	 * ��ȡ��ģ�͵�ֵ
	 * @return DefaultTableModel ��ģ�͵�ֵ
	 */
	public DefaultTableModel getDataModel() {
		return dataModel;
	}

	/**
	 * ��ȡɾ����ť��ֵ
	 * @return JButton ɾ����ť��ֵ
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * ��ȡID�ı����ֵ
	 * @return JTextField ID�ı����ֵ
	 */
	public JTextField getIdField() {
		return idField;
	}

	/**
	 * ��ȡ��Ӱ�ť��ֵ
	 * @return JButton ��Ӱ�ť��ֵ
	 */
	public JButton getInsertButton() {
		return insertButton;
	}

	/**
	 * ��ȡ�����ı����ֵ
	 * @return JTextField �����ı����ֵ
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * ��ȡ״̬�������ֵ
	 * @return JComboBox ״̬�������ֵ
	 */
	public JComboBox getOnLineComboBox() {
		return onLineComboBox;
	}

	/**
	 * ��ȡ�������밴ť��ֵ
	 * @return JButton �������밴ť��ֵ
	 */
	public JButton getRePswAllButton() {
		return rePswAllButton;
	}

	/**
	 * ��ȡ�����������밴ť��ֵ
	 * @return JButton �����������밴ť��ֵ
	 */
	public JButton getRePswButton() {
		return rePswButton;
	}

	/**
	 * ��ȡ��ѯ��ť��ֵ
	 * @return JButton ��ѯ��ť��ֵ
	 */
	public JButton getSelectButton() {
		return selectButton;
	}

	/**
	 * ��ȡ�޸İ�ť��ֵ
	 * @return JButton �޸İ�ť��ֵ
	 */
	public JButton getUpdateButton() {
		return updateButton;
	}

	/**
	 * ��ȡ�û����ֵ
	 * @return JTable �û����ֵ
	 */
	public JTable getUserTable() {
		return userTable;
	}


}
