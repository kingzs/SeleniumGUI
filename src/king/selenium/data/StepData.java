package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��2�� ����5:18:13
* @ClassName ...
* @Description ���Բ�������ݶ���
*/
public class StepData implements IData {

	private String stepName = "���Բ���";//���Բ�������ƣ�����Ĭ��ֵ
	private boolean status = true;//״̬��Ĭ��Ϊ����״̬
	private String findType;//����Ԫ�صķ������ƣ�����������ѡ�е�ѡ�������
	private List<String> findParams = new ArrayList<>();//����Ԫ�صķ����Ĳ�����
	private int timeout;//����Ԫ�صĳ�ʱʱ��
	private String followType;//���������ķ�������
	private List<String> followParams = new ArrayList<>();//���������ķ����Ĳ�����
	private String actionType;//�����ķ�������
	private List<String> actionParams = new ArrayList<>();//�����ķ����Ĳ�����
	private String saveType;//�������ݵ����ͣ���δʵ��
	private String saveName;//�������ݵı���������δʵ��
	private IData parent;//��Ԫ�أ���������Բ����������ĸ�����������
	
	public String getName() {
		return stepName;
	}
	public void setName(String stepName) {
		this.stepName = stepName;
	}
	public boolean getStatus(){
		return status;
	}
	public void setStatus(boolean status){
		this.status = status;
	}
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	public List<String> getFindParams() {
		return findParams;
	}
	public void addFindParam(String findParam) {
		findParams.add(findParam);
	}
	public void removeAllFindParams(){
		findParams.clear();
	}
	public int getTimeout(){
		return timeout;
	}
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	public String getFollowType() {
		return followType;
	}
	public void setFollowType(String followType) {
		this.followType = followType;
	}
	public List<String> getFollowParams() {
		return followParams;
	}
	public void addFollowParam(String followParam) {
		followParams.add(followParam);
	}
	public void removeAllFollowParam(){
		followParams.clear();
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public List<String> getActionParams() {
		return actionParams;
	}
	public void addActionParam(String actionParam) {
		actionParams.add(actionParam);
	}
	public void removeAllActionParams(){
		actionParams.clear();
	}
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
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
	public List<IData> getDatas(){
		return null;
	}
	public void addData(IData data) {
		
	}
	public void addData(int index, IData data){}
	
	public int indexOf(IData data){return 0;}
	public IData getData(int index){
		return null;
	}
	public void delData(int index){
		
	}
	public void delData(IData data){}
	public StepData copyData(){
		StepData copyData = new StepData();
		copyData.stepName = this.stepName;
		copyData.status = this.status;
		copyData.findType = this.findType;
		copyData.findParams = new ArrayList<>(this.findParams);
		copyData.timeout = this.timeout;
		copyData.followType = this.followType;
		copyData.followParams = new ArrayList<>(this.followParams);
		copyData.actionType = this.actionType;
		copyData.actionParams = new ArrayList<>(this.actionParams);
		copyData.saveType = this.saveType;
		copyData.saveName = this.saveName;
		return copyData;
	}
}
