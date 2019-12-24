package king.selenium.view.center;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��10��25�� ����2:53:09
* @ClassName ...
* @Description ������������ѭ����ʹ�ñ�����ò��������������������Ķ���
*/
public class TablePane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane sp;
	private DefaultTableModel tableModel;
	private TableConf tableConf;
	
	public TablePane(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		tableModel = new DefaultTableModel(new Object[][]{{""}}, new Object[]{"����˵��"});
		tableConf = new TableConf(tableModel);
		sp = new JScrollPane(tableConf);
		sp.setPreferredSize(new Dimension(742,500));
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel jp = new JPanel();
		JButton addRow = new JButton("�����");
		JButton removeRow = new JButton("ɾ����");
		JButton addColumn = new JButton("�����");
		JButton removeColumn = new JButton("ɾ����");
		jp.add(addRow);
		jp.add(removeRow);
		jp.add(addColumn);
		jp.add(removeColumn);
		
		JPanel explainPanel = new JPanel();
		JLabel explainLabel = new JLabel("���������Ҫ�Զ�����(ÿ�νű�����ǰ���ݻ����)����������֮�����һ��#��");
		explainPanel.add(explainLabel);
		
		add(sp);
		add(jp);
		add(explainPanel);
		
		addRow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableConf.addRow())
					Domain.setChangeFile(true);
			}
		});
		
		removeRow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableConf.removeRow())
					Domain.setChangeFile(true);
			}
		});
		
		addColumn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableConf.addColumn())
					Domain.setChangeFile(true);
			}
		});
		
		removeColumn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableConf.removeColumn())
					Domain.setChangeFile(true);
			}
		});
	}
	
	public void setTableConf(TableConf tableConf){
		this.tableConf = tableConf;
		sp.setViewportView(tableConf);
	}
	
	public TableConf getTableConf(){
		return tableConf;
	}

}
