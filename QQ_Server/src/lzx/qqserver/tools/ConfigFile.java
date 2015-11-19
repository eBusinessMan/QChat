package lzx.qqserver.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ��ȡ�����ļ���
 * @author luozhixiao
 */
public class ConfigFile {

	/** �ļ�����·�� */
	private String filename;

	/** �ļ������� */
	private FileInputStream fis;

	/** ��ȡ�����ļ��� */
	private Properties prop;

	/**
	 * ���캯��
	 * @param filename �ļ�����·��
	 */
	public ConfigFile(final String filename) {

		this.filename = filename;

		try {
			fis = new FileInputStream(filename);
			prop = new Properties();
			prop.load(fis);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**��ȡ�ļ����� */
	public String getKeyValue(String key) {

		return prop.getProperty(key);

	}

	/** �������õ�ֵ */
	public void setKeyValue(String key, String value) {

		prop.setProperty(key, value);

	}

	/** �������� */
	public boolean save() {
		
		boolean bo = false;

		try {
			prop.store(new FileOutputStream(filename), "��ע");
			bo = true;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return bo;
	}

}
