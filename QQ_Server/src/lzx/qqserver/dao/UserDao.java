package lzx.qqserver.dao;


import java.util.List;
import java.util.Vector;

import pub.User;

/**
 * �û������ݴ洢����
 * 
 * @author luozhixiao
 * @version 1.0
 */

public interface UserDao {
	
	/**
	 * ��ѯ�û���Ϣ��ÿ���û�������һ��Vector�������棬��ЩVector�����ַ���һ��Vector����
	 * �������ʾ�á�
	 * @param id �û���ţ���ȷ��ѯ��Ϊnull����""����ʹ�ø�������
	 * @param name �û�����ģ����ѯ��Ϊnull����""����ʹ�ø�������
	 * @praram isonline ״̬��Ϊ-1��ʾ��ʹ�ø�������
	 * @return ��������������û���Vector����
	 */
	public Vector<Vector<String>> selectUser(String id, String name, int isonline);
	
	/**
	 * ȡ�����û��б�
	 * @return ��������û��б�
	 */
	public List<User> getOnlineUser();
	
	/**
	 * ��user��������ݱ��浽�洢����
	 * @param user Ҫ������û�����
	 * @return ����ɹ�����null,���򷵻ش�����Ϣ
	 */
	public String addUser(User user);
	
	/**
	 * �޸��û�
	 * @param newUser ���ĺ�����û���Ϣ
	 * @return ���³ɹ��򷵻�null�����򷵻ش�����Ϣ
	 */
	public String updateUser(User newUser);
	
	/**
	 * ɾ��ָ���û�
	 * @param id ��ɾ�����û��ı��
	 * @return ɾ���ɹ��򷵻�null,���򷵻ش�����Ϣ
	 */
	public String deleteUser(String id);
	
	
	/**
	 * ��֤�û�
	 * @param id �û����
	 * @param password ����
	 * @return ��֤ͨ������User���󣬷��򷵻ش�����Ϣ��
	 */
	public Object checkUser(String id, String password);
	
	
	/**
	 * �����û���״̬������/�����ߣ�
	 * @param id Ҫ����״̬���û��ı��
	 * @param online ����/������
	 * @return ���³ɹ��򷵻�null�����򷵻ش�����Ϣ
	 */
	public String updateOnline(String id, boolean online);
	
	/**
	 * ���������û�״̬Ϊ������
	 * @return ���óɹ�����null,���򷵻ش�����Ϣ
	 */
	public String setAllOffline();
	
	/**
	 * �޸��û�����
	 * @param id Ҫ�޸ĵ��û����
	 * @param oldPwd ԭ����
	 * @param newPwd ������
	 * @return ���óɹ�����null,���򷵻ش�����Ϣ
	 */
	public String updatePassword(String id, String oldPwd, String newPwd);
	
	/**
	 * �ر���Դ
	 */
	public void close();

}
