package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月2日 下午5:18:13
* @ClassName ...
* @Description 测试步骤的数据定义
*/
public class StepData implements IData {

	private String stepName = "测试步骤";//测试步骤的名称，给定默认值
	private boolean status = true;//状态。默认为启用状态
	private String findType;//查找元素的方法名称，即下拉框中选中的选项的内容
	private List<String> findParams = new ArrayList<>();//查找元素的方法的参数集
	private int timeout;//查找元素的超时时间
	private String followType;//后续操作的方法名称
	private List<String> followParams = new ArrayList<>();//后续操作的方法的参数集
	private String actionType;//操作的方法名称
	private List<String> actionParams = new ArrayList<>();//操作的方法的参数集
	private String saveType;//保存数据的类型，暂未实现
	private String saveName;//保存数据的变量名，暂未实现
	private IData parent;//父元素，即这个测试步骤是属于哪个测试用例的
	
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
