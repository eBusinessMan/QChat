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

	/** ��� */
	private JTextField idField;

	/** ���� */
	private JTextField nameField;

	/** ���� */
	private JPasswordField passwordField;

	/** �Ա� */
	private JComboBox sexComboBox;

	/** ���� */
	private JTextField ageField;

	/** ��ַ */
	private JTextField addressField;

	/** �Ƿ����� */
	private JTextField isonlineField;

	/** ʱ�� */
	private JTextField timeField;

	/** ���� */
	private JButton saveButton;

	/** ȡ�� */
	private JButton cancelButton;
	
	
	/**
	 * ���캯��
	 */
	public UserInfoPanel(){
		initPanel();
	}
	/**
	 *����û���巽��
	 */
	public void initPanel() {

		Box box = Box.createVerticalBox();

		JPanel idPanel = new JPanel();
		idField = new JTextField();
		idField.setEditable(false);// ID���ɱ༭
		idField.setPreferredSize(new Dimension(168, 20));
		idPanel.add(new JLabel("�� ��"));
		idPanel.add(idField);

		JPanel namePanel = new JPanel();
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(168, 20));
		namePanel.add(new JLabel("�� ��"));
		namePanel.add(nameField);

		JPanel passwordPanel = new JPanel();
		passwordField = new JPasswordField("123456");
		passwordField.setPreferredSize(new Dimension(168, 20));
		passwordPanel.add(new JLabel("�� ��"));
		passwordPanel.add(passwordField);

		JPanel sexPanel = new JPanel();
		sexComboBox = new JComboBox();
		sexComboBox.addItem("��");
		sexComboBox.addItem("Ů");
		sexComboBox.setPreferredSize(new Dimension(168, 20));
		sexPanel.add(new JLabel("�� ��"));
		sexPanel.add(sexComboBox);

		JPanel agePanel = new JPanel();
		ageField = new JTextField();
		ageField.setPreferredSize(new Dimension(168, 20));
		agePanel.add(new JLabel("�� ��"));
		agePanel.add(ageField);

		JPanel addressPanel = new JPanel();
		addressField = new JTextField();
		addressField.setPreferredSize(new Dimension(168, 20));
		addressPanel.add(new JLabel("�� ַ"));
		addressPanel.add(addressField);

		JPanel isonlinePanel = new JPanel();
		isonlineField = new JTextField("������");
		isonlineField.setEditable(false);
		isonlineField.setPreferredSize(new Dimension(168, 20));
		isonlinePanel.add(new JLabel("״ ̬"));
		isonlinePanel.add(isonlineField);

		JPanel timePanel = new JPanel();
		timeField = new JTextField(QQUtils.formatTime());
		timeField.setEditable(false);
		timeField.setPreferredSize(new Dimension(168, 20));
		timePanel.add(new JLabel("�� ��"));
		timePanel.add(timeField);

		Box buttonBox = Box.createHorizontalBox();

		saveButton = new JButton("�� ��");
		//saveButton.addActionListener(addUserListen);
		cancelButton = new JButton("ȡ��");
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
	 * ��ȡȡ����ť��ֵ��
	 * @return JButton ȡ����ť��ֵ��
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * ��ȡ���水ť��ֵ��
	 * @return JButton ���水ť��ֵ��
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * ��ȡ��ַ�ı����ֵ��
	 * @return JTextField ��ַ�ı����ֵ��
	 */
	public JTextField getAddressField() {
		return addressField;
	}


	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JTextField �����ı����ֵ��
	 */
	public JTextField getAgeField() {
		return ageField;
	}

	/**
	 * ��ȡID�ı����ֵ��
	 * @return JTextField ID�ı����ֵ��
	 */
	public JTextField getIdField() {
		return idField;
	}


	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JTextField �����ı����ֵ��
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * ��ȡ�Ա��������ֵ��
	 * @return JComboBox �Ա��������ֵ��
	 */
	public JComboBox getSexComboBox() {
		return sexComboBox;
	}

	/**
	 * ��ȡ�Ƿ������ı����ֵ��
	 * @return JTextField �Ƿ������ı����ֵ��
	 */
	public JTextField getIsonlineField() {
		return isonlineField;
	}

	/**
	 * ��ȡ�����ı����ֵ��
	 * @return JPasswordField �����ı����ֵ��
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	/**
	 * ��ȡʱ���ı����ֵ��
	 * @return JTextField ʱ���ı����ֵ��
	 */
	public JTextField getTimeField() {
		return timeField;
	}


}
