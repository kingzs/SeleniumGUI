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
* @time 2019年10月25日 下午2:53:09
* @ClassName ...
* @Description 测试用例开启循环，使用表格配置参数，表格数据配置组件的定义
*/
public class TablePane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane sp;
	private DefaultTableModel tableModel;
	private TableConf tableConf;
	
	public TablePane(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		tableModel = new DefaultTableModel(new Object[][]{{""}}, new Object[]{"数据说明"});
		tableConf = new TableConf(tableModel);
		sp = new JScrollPane(tableConf);
		sp.setPreferredSize(new Dimension(742,500));
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel jp = new JPanel();
		JButton addRow = new JButton("添加行");
		JButton removeRow = new JButton("删除行");
		JButton addColumn = new JButton("添加列");
		JButton removeColumn = new JButton("删除列");
		jp.add(addRow);
		jp.add(removeRow);
		jp.add(addColumn);
		jp.add(removeColumn);
		
		JPanel explainPanel = new JPanel();
		JLabel explainLabel = new JLabel("如果数据需要自动更新(每次脚本运行前数据会更新)，则在数据之后添加一个#号");
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
