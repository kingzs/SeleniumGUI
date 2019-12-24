package king.selenium.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import king.selenium.tools.KingUtils;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年11月1日 下午4:06:38
* @ClassName ...
* @Description 测试用例开启循环，使用表格配置数据，表格数据的定义
*/
public class TableData {

	private List<Object> parameterNames = new ArrayList<>();
	private Map<Object, List<Object>> parameterMapper = new HashMap<>();
	private int rows;
	
	public TableData(){
		parameterNames.add("用例名称");
		ArrayList<Object> list = new ArrayList<>();
		list.add("");
		parameterMapper.put("用例名称", list);
		rows = 1;
	}
	
	public void init(){
		parameterNames.clear();
		parameterMapper.clear();
	}
	
	public int getRows(){
		return rows;
	}
	
	public void setRows(int rows){
		this.rows = rows;
	}
	
	public Map<Object, List<Object>> getParameterMapper(){
		return parameterMapper;
	}
	
	public void addRow(int row, Object[] objs){
		int index = 0;
		for(Object name : parameterNames){
			parameterMapper.get(name).add(row, objs[index++]);
		}
		++rows;
	}
	
	public void addRow(Object[] objs){
		addRow(rows, objs);
	}
	
	public void removeRow(int row){
		for(Map.Entry<Object, List<Object>> entry : parameterMapper.entrySet()){
			entry.getValue().remove(row);
		}
		--rows;
	}
	
	public void addColumn(Object parameterName){
		List<Object> colList = new ArrayList<>();
		for(int i=0; i<rows; ++i){
			colList.add("");
		}
		addColumn(parameterName, colList);
	}
	
	public void addColumn(Object parameterName, List<Object> colData){
		parameterNames.add(parameterName);
		parameterMapper.put(parameterName, colData);
	}
	
	public void removeColumn(int col){
		parameterMapper.remove(parameterNames.get(col));
		parameterNames.remove(col);
	}
	
	public List<Object> getParameterNames(){
		return parameterNames;
	}
	
	public Map<Object, List<Object>> getDatas(){
		return parameterMapper;
	}
	
	public Object getParameter(int column, int row){
		return parameterMapper.get(parameterNames.get(column)).get(row);
	}
	
	public void setParameter(int column, int row, Object value){
		parameterMapper.get(parameterNames.get(column)).set(row, value);
	}
	
	public Object getParameter(Object columnName, int row){
		return parameterMapper.get(columnName).get(row);
	}
	
	public Object[][] getAllDataForTable(){
		int cols = parameterMapper.size();
		Object[][] datas = new Object[rows][cols];
		for(int i=0; i<rows; ++i){
			datas[i] = new Object[cols];
		}
		for(int i=0; i<cols; ++i){
			List<Object> colData = parameterMapper.get(parameterNames.get(i));
			for(int j=0; j<rows; ++j){
				datas[j][i] = colData.get(j);
			}
		}
		return datas;
	}
	
	public void setData(Vector<?> data){
		Enumeration<?> en = data.elements();
		int i = 0, j = 0;
		while(en.hasMoreElements()){
			Vector<?> rowData = (Vector<?>) en.nextElement();
			Enumeration<?> rowEn = rowData.elements();
			j = 0;
			while(rowEn.hasMoreElements()){
				String value = (String) rowEn.nextElement();
				parameterMapper.get(j).set(i, value);
				++j;
			}
			++i;
		}
	}
	
	public void moveColumn(int from, int to){
		Object moveObj = parameterNames.get(from);
		if(from > to){
			for(int i=from; i>to; --i){
				parameterNames.set(i, parameterNames.get(i-1));
			}
		}else{
			for(int i=from; i<to; ++i){
				parameterNames.set(i, parameterNames.get(i+1));
			}
		}
		parameterNames.set(to, moveObj);
	}
	
	public void modifyData(int lines){
		int loop = lines<=rows ? lines : rows;
		parameterMapper.forEach((key, value)->{
			if(!key.equals("测试用例")){
				for(int i=0; i<loop; ++i){
					value.set(i, KingUtils.parameterAdd(String.valueOf(value.get(i))));
				}
			}
		});
	}
	
	public void copyData(TableData tableData){
		this.parameterNames.clear();
		this.parameterNames.addAll(tableData.parameterNames);
		this.parameterMapper.clear();
		tableData.parameterMapper.forEach((key, value) -> {
			this.parameterMapper.put(key, new ArrayList<>(value));
		});
		this.rows = tableData.rows;
	}
}
