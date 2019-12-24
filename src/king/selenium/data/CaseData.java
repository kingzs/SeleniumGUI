package king.selenium.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月2日 下午5:18:00
* @ClassName ...
* @Description 测试用例数据定义
*/
public class CaseData implements IData {

	private String caseName = "测试用例";//测试用例的名称，给定默认值
	private boolean status = true;//状态，默认为启用状态
	private List<IData> steps = new ArrayList<>();//测试用例下的测试步骤的集合
	private IData parent;//测试用例的父元素，即这个测试用例是属于哪个测试模块的
	private boolean loop;//是否开启循环
	private int loopCount;
	private ParameterType parameterType;//参数配置方式
	private TableData tableData;//表格配置参数，表格数据
	private FileData fileData;//文件配置参数，文件配置
	private Map<String, String> variableMap;
	
	public CaseData(){
		parameterType = ParameterType.TABLE;
		tableData = new TableData();
		fileData = new FileData();
		variableMap = new HashMap<>();
	}
	
	public String getName() {
		return caseName;
	}
	public void setName(String caseName) {
		this.caseName = caseName;
	}
	public boolean getStatus(){
		return status;
	}
	public void setStatus(boolean status){
		this.status = status;
	}
	public List<IData> getDatas() {
		return steps;
	}
	public void setParent(IData data){
		this.parent = data;
	}
	public IData getParent(){
		return parent;
	}
	public boolean isLastData(){
		return parent.getData(parent.getDatas().size()-1) == this;
	}
	public IData getData(int index){
		return steps.get(index);
	}
	public void addData(IData step){
		step.setParent(this);
		steps.add(step);
	}
	public void addData(int index, IData data){
		data.setParent(this);
		steps.add(index, data);
	}
	public int indexOf(IData data){
		return steps.indexOf(data);
	}
	public void delData(int index){
		steps.remove(index);
	}
	public void delData(IData data){
		steps.remove(data);
	}
	public boolean getLoop(){
		return loop;
	}
	public void setLoop(boolean loop){
		this.loop = loop;
	}
	public int getLoopCount(){
		return loopCount;
	}
	public void setLoopCount(int loopCount){
		this.loopCount = loopCount;
	}
	public ParameterType getParameterType(){
		return parameterType;
	}
	public void setParameterType(ParameterType parameterType){
		this.parameterType = parameterType;
	}
	public TableData getTableData(){
		return tableData;
	}
	public void setTableData(TableData tableData){
		this.tableData = tableData;
	}
	public FileData getFileData(){
		return fileData;
	}
	public void setFileData(FileData fileData){
		this.fileData = fileData;
	}
	public void putVariable(String key, String value){
		variableMap.put(key, value);
	}
	public Map<String, String> getVariableMap(){
		return variableMap;
	}
	
	public CaseData copyData(){
		CaseData copyData = new CaseData();
		copyData.caseName = this.caseName;
		copyData.status = this.status;
		this.steps.forEach(step -> copyData.addData(((StepData) step).copyData()));
		copyData.loop = this.loop;
		copyData.loopCount = this.loopCount;
		copyData.parameterType = this.parameterType;
		copyData.tableData.copyData(this.tableData);
		copyData.fileData.copyData(this.fileData);
		return copyData;
	}
}
