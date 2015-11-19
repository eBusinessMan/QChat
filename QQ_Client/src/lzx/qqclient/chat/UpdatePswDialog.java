package lzx.qqclient.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import lzx.qqclient.business.ChatUtils;
import lzx.qqclient.tools.QQUtils;




/**
 * �޸��������
 * @author luozhixiao
 *
 */
public class UpdatePswDialog extends JDialog{
	
	
	private static final long serialVersionUID = -478036278031060193L;

	private JPanel panel;
	
	private ChatUtils chat;
	/** ԭʼ���� */
	private JPasswordField oldField;
	
	/** ������ */
	private JPasswordField newField;
	
	/** ȷ�������� */
	private JPasswordField reNewField;
	
	/** ȷ�� */
	private JButton okButton;
	
	/** ȡ�� */
	private JButton cancelButton;
	
	/** �޸���������� */
	private UpdatePswDialogListen updatePswListen; 
	
	/** ���������� */
	private ChatFrame frame;
	
	/** �޸�����Ի��� */
	private static UpdatePswDialog updatePsw;

	/**
	 * ���췽�� 
	 * @param frame ����������
	 */
	public UpdatePswDialog(final ChatFrame frame,ChatUtils chat)
	{
		
		super(frame);
		updatePsw = this;
		this.chat=chat;
		this.frame = frame;
		initPanel();
		this.setTitle("�޸�����");
		this.setSize(300, 190);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(null);//���öԻ������
		this.setVisible(true);	
		
		
	}

	/**
	 * �������ķ���
	 *
	 */
	public void initPanel()
	{
		panel = new JPanel();
		panel.setBackground(new Color(235, 250, 255));
		
		Box box = Box.createVerticalBox();
		
		JPanel oldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));	
		oldPanel.setBackground(new Color(235, 250, 255));
		oldField = new JPasswordField();
		oldField.setPreferredSize(new Dimension(168, 20));
		oldPanel.add(new JLabel("ԭ  ��  �룺"));
		oldPanel.add(oldField);
		
		
		JPanel newPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		newPanel.setBackground(new Color(235, 250, 255));
		newField = new JPasswordField();
		newField.setPreferredSize(new Dimension(168, 20));
		newPanel.add(new JLabel("��  ��  �룺"));
		newPanel.add(newField);
		
		JPanel renewPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		renewPanel.setBackground(new Color(235, 250, 255));
		reNewField = new JPasswordField();
		reNewField.setPreferredSize(new Dimension(168, 20));
		renewPanel.add(new JLabel("ȷ�������룺"));
		renewPanel.add(reNewField);
		
		Box buttonBox = Box.createHorizontalBox();
		updatePswListen = new UpdatePswDialogListen(this,chat);
		okButton = new JButton("�� ��");//ȷ��
		okButton.addActionListener(updatePswListen);
		okButton.setIcon(QQUtils.normal);
		okButton.setHorizontalTextPosition(JButton.CENTER);
		okButton.setBorder(null);		
		okButton.setRolloverIcon(QQUtils.over);
			
		cancelButton = new JButton("ȡ ��");//ȡ��
		cancelButton.addActionListener(updatePswListen);
		cancelButton.setIcon(QQUtils.normal);
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setBorder(null);		
		cancelButton.setRolloverIcon(QQUtils.over);
		
		buttonBox.add(okButton);
		buttonBox.add(Box.createHorizontalStrut(40));
		buttonBox.add(cancelButton);
		
		box.add(Box.createVerticalStrut(5));
		box.add(oldPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(newPanel);
		box.add(Box.createVerticalStrut(5));
		box.add(renewPanel);
		box.add(Box.createVerticalStrut(10));
		box.add(buttonBox);
		
		
		panel.add(box);
		this.add(panel);
		
		
	}

	/**
	 *  ��ȡȡ����ť��ֵ
	 * @return JButton ȡ����ť��ֵ
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 *  ��ȡ���水ť��ֵ
	 * @return JButton ���水ť��ֵ
	 */
	public JButton getOkButton() {
		return okButton;
	}


	/**
	 * ��ȡ�������ı����ֵ
	 * @return JPasswordField �������ı����ֵ
	 */
	public JPasswordField getNewField() {
		return newField;
	}

	/**
	 * ��ȡ�������ı����ֵ
	 * @return JPasswordField �������ı����ֵ
	 */
	public JPasswordField getOldField() {
		return oldField;
	}

	/**
	 * ��ȡ�ظ��������ı����ֵ
	 * @return JPasswordField �ظ��������ı����ֵ
	 */
	public JPasswordField getReNewField() {
		return reNewField;
	}

	/**
	 * ��ȡ�����������ֵ
	 * @return ChatFrame �����������ֵ
	 */
	public ChatFrame getFrame() {
		return frame;
	}

	public static UpdatePswDialog getUpdatePsw() {
		return updatePsw;
	}
	
	
}
