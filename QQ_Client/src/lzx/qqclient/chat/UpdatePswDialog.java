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
 * 修改密码的类
 * @author luozhixiao
 *
 */
public class UpdatePswDialog extends JDialog{
	
	
	private static final long serialVersionUID = -478036278031060193L;

	private JPanel panel;
	
	private ChatUtils chat;
	/** 原始密码 */
	private JPasswordField oldField;
	
	/** 新密码 */
	private JPasswordField newField;
	
	/** 确认信密码 */
	private JPasswordField reNewField;
	
	/** 确认 */
	private JButton okButton;
	
	/** 取消 */
	private JButton cancelButton;
	
	/** 修改密码监听类 */
	private UpdatePswDialogListen updatePswListen; 
	
	/** 聊天主界面 */
	private ChatFrame frame;
	
	/** 修改密码对话框 */
	private static UpdatePswDialog updatePsw;

	/**
	 * 构造方法 
	 * @param frame 聊天主界面
	 */
	public UpdatePswDialog(final ChatFrame frame,ChatUtils chat)
	{
		
		super(frame);
		updatePsw = this;
		this.chat=chat;
		this.frame = frame;
		initPanel();
		this.setTitle("修改密码");
		this.setSize(300, 190);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(null);//设置对话框居中
		this.setVisible(true);	
		
		
	}

	/**
	 * 构造面板的方法
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
		oldPanel.add(new JLabel("原  密  码："));
		oldPanel.add(oldField);
		
		
		JPanel newPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		newPanel.setBackground(new Color(235, 250, 255));
		newField = new JPasswordField();
		newField.setPreferredSize(new Dimension(168, 20));
		newPanel.add(new JLabel("新  密  码："));
		newPanel.add(newField);
		
		JPanel renewPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		renewPanel.setBackground(new Color(235, 250, 255));
		reNewField = new JPasswordField();
		reNewField.setPreferredSize(new Dimension(168, 20));
		renewPanel.add(new JLabel("确认新密码："));
		renewPanel.add(reNewField);
		
		Box buttonBox = Box.createHorizontalBox();
		updatePswListen = new UpdatePswDialogListen(this,chat);
		okButton = new JButton("修 改");//确认
		okButton.addActionListener(updatePswListen);
		okButton.setIcon(QQUtils.normal);
		okButton.setHorizontalTextPosition(JButton.CENTER);
		okButton.setBorder(null);		
		okButton.setRolloverIcon(QQUtils.over);
			
		cancelButton = new JButton("取 消");//取消
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
	 *  获取取消按钮的值
	 * @return JButton 取消按钮的值
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 *  获取保存按钮的值
	 * @return JButton 保存按钮的值
	 */
	public JButton getOkButton() {
		return okButton;
	}


	/**
	 * 获取新密码文本框的值
	 * @return JPasswordField 新密码文本框的值
	 */
	public JPasswordField getNewField() {
		return newField;
	}

	/**
	 * 获取旧密码文本框的值
	 * @return JPasswordField 旧密码文本框的值
	 */
	public JPasswordField getOldField() {
		return oldField;
	}

	/**
	 * 获取重复新密码文本框的值
	 * @return JPasswordField 重复新密码文本框的值
	 */
	public JPasswordField getReNewField() {
		return reNewField;
	}

	/**
	 * 获取聊天主界面的值
	 * @return ChatFrame 聊天主界面的值
	 */
	public ChatFrame getFrame() {
		return frame;
	}

	public static UpdatePswDialog getUpdatePsw() {
		return updatePsw;
	}
	
	
}
