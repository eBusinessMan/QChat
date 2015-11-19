package lzx.qqserver.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件类
 * @author luozhixiao
 */
public class ConfigFile {

	/** 文件名和路径 */
	private String filename;

	/** 文件输入流 */
	private FileInputStream fis;

	/** 读取配置文件流 */
	private Properties prop;

	/**
	 * 构造函数
	 * @param filename 文件名和路径
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
	/**读取文件配置 */
	public String getKeyValue(String key) {

		return prop.getProperty(key);

	}

	/** 设置配置的值 */
	public void setKeyValue(String key, String value) {

		prop.setProperty(key, value);

	}

	/** 保存配置 */
	public boolean save() {
		
		boolean bo = false;

		try {
			prop.store(new FileOutputStream(filename), "备注");
			bo = true;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return bo;
	}

}
