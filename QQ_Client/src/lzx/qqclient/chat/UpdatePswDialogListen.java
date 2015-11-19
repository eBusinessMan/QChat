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
 * �޸����������
 *@author luozhixiao
 *
 */
public class UpdatePswDialogListen implements ActionListener {

	/** �޸�����Ի��� */
	private UpdatePswDialog updatePsw;
	private ChatUtils chat;

	

	/**
	 * ���캯��
	 * @param updatePsw �޸�����Ի���
	 */
	public UpdatePswDialogListen(final UpdatePswDialog updatePsw,ChatUtils chat){
		
		this.updatePsw = updatePsw;
        this.chat=chat;
	}
	
	/**
	 * �¼������ķ���
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==updatePsw.getOkButton()){//���ȷ��
			updatePassword();//�����޸�����ķ���
		}else{
			updatePsw.dispose();
		}

	}
	
	public void updatePassword(){
		
		String oldPsw = new String(updatePsw.getOldField().getPassword()).trim();
		
		if(oldPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "�����벻��Ϊ��,����������룡", "������ʾ", 1);
			return;
		}else if (oldPsw.length() < 3 || oldPsw.length() > 16) {

			JOptionPane.showMessageDialog(updatePsw,
					" ���볤�ȱ����� 3~16 ֮��! ", "������ʾ", 1);
		}else if(!(Pattern.matches("^\\w+$", oldPsw))){
			JOptionPane.showMessageDialog(updatePsw," ����ֻ���������֣���ĸ , _ ! ", "������ʾ", 1);
			return;
		}
		
		String newPsw = new String(updatePsw.getNewField().getPassword()).trim();
		if(newPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "�����벻��Ϊ�գ������������룡", "������ʾ", 1);
			return;
		}else if (newPsw.length() < 3 || newPsw.length() > 16) {

			JOptionPane.showMessageDialog(updatePsw,"���������볤�ȱ����� 3~16 ֮��! ", "������ʾ", 1);
		}
		else if(!(Pattern.matches("^\\w+$", newPsw))){
			JOptionPane.showMessageDialog(updatePsw,"������ֻ���������֣���ĸ , _ ! ", "������ʾ", 1);
			return;
		}
		
		
		String reNewPsw = new String(updatePsw.getReNewField().getPassword()).trim();
		if(reNewPsw.length()==0){
			JOptionPane.showMessageDialog(updatePsw, "ȷ�������벻��Ϊ�գ�������ȷ�������룡", "������ʾ", 1);
			return;
		}

		
		if(!newPsw.equals(reNewPsw)){
			JOptionPane.showMessageDialog(updatePsw, "��������ظ������벻һ�£����������룡", "������ʾ", 1);
			return;
		}
		
		TCPPack updatePack = new TCPPack();
		updatePack.setType(QQPackType.UPDATE_PASSWORD);// ����QQ��Ϊ�޸���������
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
