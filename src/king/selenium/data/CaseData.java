package king.selenium.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��2�� ����5:18:00
* @ClassName ...
* @Description �����������ݶ���
*/
public class CaseData implements IData {

	private String caseName = "��������";//�������������ƣ�����Ĭ��ֵ
	private boolean status = true;//״̬��Ĭ��Ϊ����״̬
	private List<IData> steps = new ArrayList<>();//���������µĲ��Բ���ļ���
	private IData parent;//���������ĸ�Ԫ�أ���������������������ĸ�����ģ���
	private boolean loop;//�Ƿ���ѭ��
	private int loopCount;
	private ParameterType parameterType;//�������÷�ʽ
	private TableData tableData;//������ò������������
	private FileData fileData;//�ļ����ò������ļ�����
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
