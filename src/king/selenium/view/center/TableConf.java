package king.selenium.view.center;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import king.selenium.data.TableData;
import king.selenium.tools.KingUtils;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年10月25日 下午2:09:14
* @ClassName ...
* @Description 自定义表格组件
*/
public class TableConf extends JTable {

	private static final long serialVersionUID = 1L;
	private boolean dragged;
	private TableData tableData;

	public TableConf(TableModel tableModel){
		super(tableModel);
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		tableHeader.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				dragged = true;
			}
		});
		
		tableHeader.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				if(dragged){
					changeModelColumn();
					dragged = false;
				}
			}
		});
		
		dataModel.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				int changeRow = e.getFirstRow();
				int changeCol = e.getColumn();
				if(e.getType() == TableModelEvent.UPDATE && changeRow>=0 && changeCol>=0){
					tableData.setParameter(changeCol, changeRow, dataModel.getValueAt(changeRow, changeCol));
					Domain.setChangeFile(true);
				}
			}	
		});
		
	}
	
	public void setTableData(TableData tableData){
		this.tableData = tableData;
		((DefaultTableModel) dataModel).setDataVector(tableData.getAllDataForTable(), tableData.getParameterNames().toArray());
	}
	
	public TableData getTableData(){
		return tableData;
	}
	//添加行，有选中行，在选中行上面添加行，没有选中行，则在最后一行之后添加行
	public boolean addRow(){
		int selectedLine = getSelectedRow();
		int count = dataModel.getColumnCount();
		Object[] newLine = new Object[count];
		Arrays.fill(newLine, "");
		
		if(selectedLine < 0){
			((DefaultTableModel)dataModel).addRow(newLine);
			tableData.addRow(newLine);
		}else{
			((DefaultTableModel)dataModel).insertRow(selectedLine, newLine);
			this.setRowSelectionInterval(selectedLine, selectedLine);
			tableData.addRow(selectedLine, newLine);
		}
		return true;
	}
	//删除行，删除选中行，如果没有选中行，不操作
	public boolean removeRow(){
		boolean flag = false;
		int selectedLine = getSelectedRow();
		if(selectedLine != -1){
			((DefaultTableModel)dataModel).removeRow(selectedLine);
			tableData.removeRow(selectedLine);
			flag = true;
		}
		if(--selectedLine >= 0){
			this.setRowSelectionInterval(selectedLine, selectedLine);
		}
		return flag;
	}
	//添加列，需要输入添加列的列名，才可以进行添加，列名会进行判重，添加的列在所有列之后
	public boolean addColumn(){
		String columnName = JOptionPane.showInputDialog(getRootPane(), "请输入列名，列名将作为变量名使用：");
		if(columnName == null || "".equals(columnName.trim())){
			return false;
		}
		for(int i=0,len=dataModel.getColumnCount(); i<len; ++i){
			if(columnName.equals(dataModel.getColumnName(i))){
				JOptionPane.showMessageDialog(getRootPane(), "列名已经存在，请使用其它列名！");
				return false;
			}
		}
		((DefaultTableModel)dataModel).addColumn(columnName);
		tableData.addColumn(columnName);
		return true;
	}
	//删除列，删除选中的列，但第一列是必须要有的列，不能被删除，没有选中列时，不会操作
	public boolean removeColumn(){
		int index = getSelectedColumn();
		int cols = getColumnCount()-1;
		if(index < 1){
			return false;
		}
		Vector<Object> names = new Vector<>(cols+1);
		for(int i=0; i<=cols; ++i){
			names.addElement(getColumnName(i));
		}
		names.remove(index);
		removeColumn(getColumnModel().getColumn(index));
		DefaultTableModel defaultTableModel = (DefaultTableModel) dataModel;
		defaultTableModel.setDataVector(removeDataAtIndex(defaultTableModel.getDataVector(), index), names);
		tableData.removeColumn(index);
		return true;
	}
	//从表格的数据模型中删除一列
	private Vector<?> removeDataAtIndex(Vector<?> vector, int index){
		Enumeration<?> datas = vector.elements();
		while(datas.hasMoreElements()){
			Vector<?> rowDatas = (Vector<?>) datas.nextElement();
			rowDatas.remove(index);
		}
		return vector;
	}
	//调整表格的数据模型与表格的UI层一致，表格的列进行拖动后，调用这个方法
	private void changeModelColumn(){
		int count = dataModel.getColumnCount();
		Object[] tableColumnNames = new Object[count];
		Object[] modelColumnNames = new Object[count];
		for(int i=0; i<count; ++i){
			tableColumnNames[i] = getColumnName(i);
			modelColumnNames[i] = dataModel.getColumnName(i);
		}
		
		int explainIndex = KingUtils.indexOf(tableColumnNames, "用例名称");
		if(explainIndex > 0){
			KingUtils.moveArrayElement(tableColumnNames, explainIndex, 0);
		}
		
		for(int i=0; i<count; ++i){
			if(!tableColumnNames[i].equals(modelColumnNames[i])){
				int reference = KingUtils.indexOf(modelColumnNames, tableColumnNames[i]);
				if(reference-i==1){
					int j = KingUtils.indexOf(tableColumnNames, modelColumnNames[i]);
					moveModelColumn(i, j);
					tableData.moveColumn(i, j);
				}else{
					moveModelColumn(reference, i);
					tableData.moveColumn(reference, i);
				}
				Domain.setChangeFile(true);
				break;
			}
		}
		((DefaultTableModel)dataModel).setColumnIdentifiers(tableColumnNames);
	}
	//表格的数据模型，数据进行移动。移动，并不是简单的交换，比如：一个数据从下标为5的位置移动到下标为3的位置
	//要把下标为5的数据移动到3，而原先在3的数据要移动到4，原先在4的数据要移动到5，
	private void moveModelColumn(int from, int to){
		Vector<?> vector = ((DefaultTableModel)dataModel).getDataVector();
		Enumeration<?> datas = vector.elements();
		while(datas.hasMoreElements()){
			@SuppressWarnings("unchecked")
			Vector<Object> rowVector = (Vector<Object>) datas.nextElement();
			moveVectorElement(rowVector, from, to);
		}	
	}
	//集合的数据，数据进行移动
	private void moveVectorElement(Vector<Object> objs, int from, int to){
		Object moveObj = objs.get(from);
		if(from > to){
			for(int i=from; i>to; --i){
				objs.set(i, objs.get(i-1));
			}
		}else{
			for(int i=from; i<to; ++i){
				objs.set(i, objs.get(i+1));
			}
		}
		objs.set(to, moveObj);
	}
}
