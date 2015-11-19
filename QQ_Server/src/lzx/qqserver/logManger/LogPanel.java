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
 * ��־������
 * @author luozhixiao
 */
public class LogPanel extends JPanel {

	private static final long serialVersionUID = 1057909297258493021L;

	private JPanel historyPanel;

	/** �ϱ� */
	private JPanel topPanel;

	/** �±� */
	private JPanel downPanel;

	/** �������� */
	private JComboBox yearComboBox;

	/** �������� */
	private JComboBox monthComboBox;

	/** �������� */
	private JComboBox dayComboBox;

	/** �ؼ��� */
	private JTextField keyWorldField;

	/** ��ʷ��־�����ı��� */
	private JTextArea historyArea;

	/** ��ѯ��ť */
	private JButton selectButton;

	/** �߿���ɫ */
	private Border line = BorderFactory.createLineBorder(Color.GRAY);

	/**
	 * ���췽��
	 */
	public LogPanel() {
		this.setLayout(new BorderLayout());

		initPanel();

		this.add(historyPanel, BorderLayout.CENTER);

	}

	/**
	 * ��־��巽��
	 */
	public void initPanel() {

		historyPanel = new JPanel(new BorderLayout());

		topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder("��־����ѡ��"));

		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		int temp = 0;

		yearComboBox = new JComboBox();
		yearComboBox.setPreferredSize(new Dimension(60, 20));// ����������(wieth,height)
		for (int i = 2009; i <= 2016; i++) {
			yearComboBox.addItem(i);
			if (i == Integer.parseInt(year)) {
				temp = i - 2009;

			}

		}
		yearComboBox.setSelectedIndex(temp);// ������ѡ��Ϊ��ǰ����

		monthComboBox = new JComboBox();
		monthComboBox.setPreferredSize(new Dimension(40, 20));// ����������(wieth,height)
		for (int i = 1; i <= 12; i++) {
			monthComboBox.addItem(i);
			if (i == Integer.parseInt(month)) {
				temp = i - 1;

			}
		}
		monthComboBox.setSelectedIndex(temp);// ������ѡ��Ϊ��ǰ����

		dayComboBox = new JComboBox();
		dayComboBox.setPreferredSize(new Dimension(40, 20));// ����������(wieth,height)
		for (int i = 1; i <= 31; i++) {
			dayComboBox.addItem(i);
			if (i == Integer.parseInt(day)) {
				temp = i - 1;

			}
		}
		dayComboBox.setSelectedIndex(temp);// ������ѡ��Ϊ��ǰ����

		JPanel keyPanel = new JPanel();
		keyWorldField = new JTextField();
		keyWorldField.setPreferredSize(new Dimension(80, 24));// ����������(wieth,height)
		keyPanel.add(new JLabel("�ؼ��֣�"));
		keyPanel.add(keyWorldField);

		LogPanelListen logListen = new LogPanelListen(this);
		selectButton = new JButton("�� ѯ(S)");
		selectButton.addActionListener(logListen);
		selectButton.setMnemonic('s');// ���»���

		Box topBox = Box.createHorizontalBox();

		topBox.add(yearComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("��"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(monthComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("��"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(dayComboBox);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(new JLabel("��"));
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(keyPanel);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(selectButton);

		topPanel.add(topBox);

		downPanel = new JPanel(new BorderLayout());

		historyArea = new JTextArea();
		historyArea.setEditable(false);
		historyArea.setLineWrap(true);
		historyArea.setBorder(line);// ���ñ߽���ɫ
		JScrollPane jsContent = new JScrollPane(historyArea);

		downPanel.setBorder(BorderFactory.createTitledBorder("��ʷ��־"));
		downPanel.add( jsContent, BorderLayout.CENTER);

		historyPanel.add(topPanel, BorderLayout.NORTH);

		historyPanel.add(downPanel, BorderLayout.CENTER);

	}

	/**
	 * ��ȡ��ʷ��־�ı����ֵ��
	 * @return JTextField ��ʷ��־�ı����ֵ��
	 */
	public JTextArea getHistoryArea() {
		return historyArea;
	}
	
	/**
	 * ��ȡ���������ֵ��
	 * @return JTextField ���������ֵ��
	 */
	public JComboBox getYearComboBox() {
		return yearComboBox;
	}

	/**
	 * ��ȡ���������ֵ��
	 * @return JTextField ���������ֵ��
	 */
	public JComboBox getMonthComboBox() {
		return monthComboBox;
	}

	/**
	 * ��ȡ���������ֵ��
	 * @return JTextField ���������ֵ��
	 */
	public JComboBox getDayComboBox() {
		return dayComboBox;
	}

	/**
	 * ��ȡ��ѯ��ť��ֵ��
	 * @return JButton ��ѯ��ť��ֵ��
	 */
	public JButton getSelectButton() {
		return selectButton;
	}

	/**
	 * ��ȡ�ؼ����ı����ֵ��
	 * @return JTextField �ؼ����ı����ֵ��
	 */
	public JTextField getKeyWorldField() {
		return keyWorldField;
	}

}
