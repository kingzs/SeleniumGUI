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
* @time 2019��10��25�� ����2:09:14
* @ClassName ...
* @Description �Զ��������
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
	//����У���ѡ���У���ѡ������������У�û��ѡ���У��������һ��֮�������
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
	//ɾ���У�ɾ��ѡ���У����û��ѡ���У�������
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
	//����У���Ҫ��������е��������ſ��Խ�����ӣ�������������أ���ӵ�����������֮��
	public boolean addColumn(){
		String columnName = JOptionPane.showInputDialog(getRootPane(), "��������������������Ϊ������ʹ�ã�");
		if(columnName == null || "".equals(columnName.trim())){
			return false;
		}
		for(int i=0,len=dataModel.getColumnCount(); i<len; ++i){
			if(columnName.equals(dataModel.getColumnName(i))){
				JOptionPane.showMessageDialog(getRootPane(), "�����Ѿ����ڣ���ʹ������������");
				return false;
			}
		}
		((DefaultTableModel)dataModel).addColumn(columnName);
		tableData.addColumn(columnName);
		return true;
	}
	//ɾ���У�ɾ��ѡ�е��У�����һ���Ǳ���Ҫ�е��У����ܱ�ɾ����û��ѡ����ʱ���������
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
	//�ӱ�������ģ����ɾ��һ��
	private Vector<?> removeDataAtIndex(Vector<?> vector, int index){
		Enumeration<?> datas = vector.elements();
		while(datas.hasMoreElements()){
			Vector<?> rowDatas = (Vector<?>) datas.nextElement();
			rowDatas.remove(index);
		}
		return vector;
	}
	//������������ģ�������UI��һ�£������н����϶��󣬵����������
	private void changeModelColumn(){
		int count = dataModel.getColumnCount();
		Object[] tableColumnNames = new Object[count];
		Object[] modelColumnNames = new Object[count];
		for(int i=0; i<count; ++i){
			tableColumnNames[i] = getColumnName(i);
			modelColumnNames[i] = dataModel.getColumnName(i);
		}
		
		int explainIndex = KingUtils.indexOf(tableColumnNames, "��������");
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
	//��������ģ�ͣ����ݽ����ƶ����ƶ��������Ǽ򵥵Ľ��������磺һ�����ݴ��±�Ϊ5��λ���ƶ����±�Ϊ3��λ��
	//Ҫ���±�Ϊ5�������ƶ���3����ԭ����3������Ҫ�ƶ���4��ԭ����4������Ҫ�ƶ���5��
	private void moveModelColumn(int from, int to){
		Vector<?> vector = ((DefaultTableModel)dataModel).getDataVector();
		Enumeration<?> datas = vector.elements();
		while(datas.hasMoreElements()){
			@SuppressWarnings("unchecked")
			Vector<Object> rowVector = (Vector<Object>) datas.nextElement();
			moveVectorElement(rowVector, from, to);
		}	
	}
	//���ϵ����ݣ����ݽ����ƶ�
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
