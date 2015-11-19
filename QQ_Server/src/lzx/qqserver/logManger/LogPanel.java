package lzx.qqserver.logManger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * 日志管理类
 * @author luozhixiao
 */
public class LogPanel extends JPanel {

	private static final long serialVersionUID = 1057909297258493021L;

	private JPanel historyPanel;

	/** 上边 */
	private JPanel topPanel;

	/** 下边 */
	private JPanel downPanel;

	/** 年下拉框 */
	private JComboBox yearComboBox;

	/** 月下拉框 */
	private JComboBox monthComboBox;

	/** 天下拉框 */
	private JComboBox dayComboBox;

	/** 关键字 */
	private JTextField keyWorldField;

	/** 历史日志内容文本框 */
	private JTextArea historyArea;

	/** 查询按钮 */
	private JButton selectButton;

	/** 边框颜色 */
	private Border line = BorderFactory.createLineBorder(Color.GRAY);

	/**
	 * 构造方法
	 */
	public LogPanel() {
		this.setLayout(new BorderLayout());

		initPanel();

		this.add(historyPanel, BorderLayout.CENTER);

	}

	/**
	 * 日志面板方法
	 */
	public void initPanel() {

		historyPanel = new JPanel(new BorderLayout());

		topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder("日志日期选择"));

		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		int temp = 0;

		yearComboBox = new JComboBox();
		yearComboBox.setPreferredSize(new Dimension(60, 20));// 设置下拉框(wieth,height)
		for (int i = 2009; i <= 2016; i++) {
			yearComboBox.addItem(i);
			if (i == Integer.parseInt(year)) {
				temp = i - 2009;

			}

		}
		yearComboBox.setSelectedIndex(temp);// 设置首选项为当前日期

		monthComboBox = new JComboBox();
		monthComboBox.setPreferredSize(new Dimension(40, 20));// 设置下拉框(wieth,height)
		for (int i = 1; i <= 12; i++) {
			monthComboBox.addItem(i);
			if (i == Integer.parseInt(month)) {
				temp = i - 1;

			}
		}
		monthComboBox.setSelectedIndex(temp);// 设置首选项为当前日期

		dayComboBox = new JComboBox();
		dayComboBox.setPreferredSize(new Dimension(40, 20));// 设置下拉框(wieth,height)
		for (int i = 1; i <= 31; i++) {
			dayComboBox.addItem(i);
			if (i == Integer.parseInt(day)) {
				temp = i - 1;

			}
		}
		dayComboBox.setSelectedIndex(temp);// 设置首选项为当前日期

		JPanel keyPanel = new JPanel();
		keyWorldField = new JTextField();
		keyWorldField.setPreferredSize(new Dimension(80, 24));// 设置下拉框(wieth,height)
		keyPanel.add(new JLabel("关键字："));
		keyPanel.add(keyWorldField);

		LogPanelListen logListen = new LogPanelListen(this);
		selectButton = new JButton("查 询(S)");
		selectButton.addActionListener(logListen);
		selectButton.setMnemonic('s');// 加下划线

		Box topBox = Box.createHorizontalBox();

		topBox.add(yearComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("年"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(monthComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("月"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(dayComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("日"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(keyPanel);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(selectButton);

		topPanel.add(topBox);

		downPanel = new JPanel(new BorderLayout());

		historyArea = new JTextArea();
		historyArea.setEditable(false);
		historyArea.setLineWrap(true);
		historyArea.setBorder(line);// 设置边界颜色
		JScrollPane jsContent = new JScrollPane(historyArea);

		downPanel.setBorder(BorderFactory.createTitledBorder("历史日志"));
		downPanel.add( jsContent, BorderLayout.CENTER);

		historyPanel.add(topPanel, BorderLayout.NORTH);

		historyPanel.add(downPanel, BorderLayout.CENTER);

	}

	/**
	 * 获取历史日志文本框的值。
	 * @return JTextField 历史日志文本框的值。
	 */
	public JTextArea getHistoryArea() {
		return historyArea;
	}
	
	/**
	 * 获取年下拉框的值。
	 * @return JTextField 年下拉框的值。
	 */
	public JComboBox getYearComboBox() {
		return yearComboBox;
	}

	/**
	 * 获取月下拉框的值。
	 * @return JTextField 月下拉框的值。
	 */
	public JComboBox getMonthComboBox() {
		return monthComboBox;
	}

	/**
	 * 获取天下拉框的值。
	 * @return JTextField 天下拉框的值。
	 */
	public JComboBox getDayComboBox() {
		return dayComboBox;
	}

	/**
	 * 获取查询按钮的值。
	 * @return JButton 查询按钮的值。
	 */
	public JButton getSelectButton() {
		return selectButton;
	}

	/**
	 * 获取关键字文本框的值。
	 * @return JTextField 关键字文本框的值。
	 */
	public JTextField getKeyWorldField() {
		return keyWorldField;
	}

}
