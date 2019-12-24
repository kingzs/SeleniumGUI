package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��2�� ����5:17:43
* @ClassName ...
* @Description ����ģ�����ݵĶ���
*/
public class ModelData implements IData {

	private String modelName = "����ģ��";//����ģ������ƣ�����Ĭ��ֵ
	private boolean status = true;//״̬��Ĭ��Ϊ����״̬
	private List<IData> cases = new ArrayList<>();//����ģ���²��������ļ���
	private IData parent;//����ģ��ĸ�Ԫ�أ����������ģ���������ĸ����Լƻ�
	
	public String getName() {
		return modelName;
	}
	public void setName(String modelName) {
		this.modelName = modelName;
	}
	public boolean getStatus(){
		return status;
	}
	public void setStatus(boolean status){
		this.status = status;
	}
	public List<IData> getDatas() {
		return cases;
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
		return cases.get(index);
	}
	public void addData(IData caseData){
		caseData.setParent(this);
		cases.add(caseData);
	}
	public void addData(int index, IData data){
		data.setParent(this);
		cases.add(index, data);
	}
	public int indexOf(IData data){
		return cases.indexOf(data);
	}
	public void delData(int index){
		cases.remove(index);
	}
	public void delData(IData data){
		cases.remove(data);
	}
	
	public ModelData copyData(){
		ModelData copyData = new ModelData();
		copyData.modelName = this.modelName;
		copyData.status = this.status;
		this.cases.forEach(testCase -> copyData.addData(((CaseData) testCase).copyData()));
		return copyData;
	}
}
