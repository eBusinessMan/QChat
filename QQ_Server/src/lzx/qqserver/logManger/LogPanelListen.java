package lzx.qqserver.logManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import lzx.qqserver.dao.LogDaoImpl;




/**
 * 日志管理监听类
 *@author luozhixiao
 *
 */
public class LogPanelListen implements ActionListener {

	/** 日志管理面板 */
	private LogPanel logPanel;

	/**
	 * 构造方法
	 */
	public LogPanelListen(final LogPanel logPanel) {

		this.logPanel = logPanel;
	}

	/**
	 * 监听事件方法
	 */
	public void actionPerformed(ActionEvent e) {


		if (e.getSource() == logPanel.getSelectButton()) {// 日志查询

			logSecect();
		}

	}
	/**
	 * 查询日志的方法
	 */
	public void logSecect() {

		String year = logPanel.getYearComboBox().getSelectedItem().toString();

		DecimalFormat df = new DecimalFormat("00");
		String month = logPanel.getMonthComboBox().getSelectedItem().toString();
		month = df.format(Double.parseDouble(month));

		String day = logPanel.getDayComboBox().getSelectedItem().toString();
		day = df.format(Double.parseDouble(day));

		String date = year + "-" + month + "-" + day;

		String keyText = logPanel.getKeyWorldField().getText().trim();// 获取关键字的值
		logPanel.getHistoryArea().setText("");// 读取日志之前先清空日志

		LogDaoImpl logDao = new LogDaoImpl(logPanel);
		logDao.selectLog(date, keyText);
		if (logPanel.getHistoryArea().getText().length() == 0) {
			if (keyText.length() != 0) {

				logPanel.getHistoryArea().setText("没有查询到包含此关键字的日志");

			} else {
				logPanel.getHistoryArea().setText("今天无日志");
			}
		}

	}

}
