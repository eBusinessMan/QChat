package lzx.qqserver.logManger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import lzx.qqserver.dao.LogDaoImpl;




/**
 * ��־���������
 *@author luozhixiao
 *
 */
public class LogPanelListen implements ActionListener {

	/** ��־������� */
	private LogPanel logPanel;

	/**
	 * ���췽��
	 */
	public LogPanelListen(final LogPanel logPanel) {

		this.logPanel = logPanel;
	}

	/**
	 * �����¼�����
	 */
	public void actionPerformed(ActionEvent e) {


		if (e.getSource() == logPanel.getSelectButton()) {// ��־��ѯ

			logSecect();
		}

	}
	/**
	 * ��ѯ��־�ķ���
	 */
	public void logSecect() {

		String year = logPanel.getYearComboBox().getSelectedItem().toString();

		DecimalFormat df = new DecimalFormat("00");
		String month = logPanel.getMonthComboBox().getSelectedItem().toString();
		month = df.format(Double.parseDouble(month));

		String day = logPanel.getDayComboBox().getSelectedItem().toString();
		day = df.format(Double.parseDouble(day));

		String date = year + "-" + month + "-" + day;

		String keyText = logPanel.getKeyWorldField().getText().trim();// ��ȡ�ؼ��ֵ�ֵ
		logPanel.getHistoryArea().setText("");// ��ȡ��־֮ǰ�������־

		LogDaoImpl logDao = new LogDaoImpl(logPanel);
		logDao.selectLog(date, keyText);
		if (logPanel.getHistoryArea().getText().length() == 0) {
			if (keyText.length() != 0) {

				logPanel.getHistoryArea().setText("û�в�ѯ�������˹ؼ��ֵ���־");

			} else {
				logPanel.getHistoryArea().setText("��������־");
			}
		}

	}

}
