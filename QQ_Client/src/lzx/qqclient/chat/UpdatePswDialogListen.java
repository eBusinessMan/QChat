package lzx.qqclient.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import lzx.qqclient.business.ChatUtils;

import pub.TCPPack;
import pub.QQPackType;

/**
 * 修改密码监听类
 *@author luozhixiao
 *
 */
public class UpdatePswDialogListen implements ActionListener {

	/** 修改密码对话框 */
	private UpdatePswDialog updatePsw;
	private ChatUtils chat;

	

	/**
	 * 构造函数
	 * @param updatePsw 修改密码对话框
	 */
	public UpdatePswDialogListen(final UpdatePswDialog updatePsw,ChatUtils chat){
		
		this.updatePsw = updatePsw;
        this.chat=chat;
	}
	
	/**
	 * 事件监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==updatePsw.getOkButton()){//点击确定
			updatePassword();//运行修改密码的方法
		}else{
			updatePsw.dispose();
		}

	}
	
	public void updatePassword(){
		
		String oldPsw = new String(updatePsw.getOldField().getPassword()).trim();
		
		if(oldPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "旧密码不能为空,请输入旧密码！", "密码提示", 1);
			return;
		}else if (oldPsw.length() < 3 || oldPsw.length() > 16) {

			JOptionPane.showMessageDialog(updatePsw,
					" 密码长度必需在 3~16 之间! ", "密码提示", 1);
		}else if(!(Pattern.matches("^\\w+$", oldPsw))){
			JOptionPane.showMessageDialog(updatePsw," 密码只允许是数字，字母 , _ ! ", "密码提示", 1);
			return;
		}
		
		String newPsw = new String(updatePsw.getNewField().getPassword()).trim();
		if(newPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "新密码不能为空，请输入新密码！", "密码提示", 1);
			return;
		}else if (newPsw.length() < 3 || newPsw.length() > 16) {

			JOptionPane.showMessageDialog(updatePsw,"新密码密码长度必需在 3~16 之间! ", "密码提示", 1);
		}
		else if(!(Pattern.matches("^\\w+$", newPsw))){
			JOptionPane.showMessageDialog(updatePsw,"新密码只允许是数字，字母 , _ ! ", "密码提示", 1);
			return;
		}
		
		
		String reNewPsw = new String(updatePsw.getReNewField().getPassword()).trim();
		if(reNewPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "确认新密码不能为空，请输入确认新密码！", "密码提示", 1);
			return;
		}

		
		if(!newPsw.equals(reNewPsw)){
			JOptionPane.showMessageDialog(updatePsw, "新密码和重复新密码不一致，请重新输入！", "密码提示", 1);
			return;
		}
		
		TCPPack updatePack = new TCPPack();
		updatePack.setType(QQPackType.UPDATE_PASSWORD);// 设置QQ包为修改密码类型
		updatePack.setTo(updatePsw.getFrame().getUser().getSid());
		updatePack.setFrom(oldPsw);
		updatePack.setContent(newPsw);
		chat.setTcpPack(updatePack);
		try {
			chat.sendObject();	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			updatePsw.getOldField().setText("");
			updatePsw.getNewField().setText("");
			updatePsw.getReNewField().setText("");
		
		}

	}

}
