package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月2日 下午5:17:29
* @ClassName ...
* @Description 测试计划数据的定义
*/
public class PlanData implements IData {

	private String planName = "测试计划";//测试计划的名称，给定默认值
	private boolean status = true;//状态。默认为启用状态
	private DriverType driverType = DriverType.values()[0];//测试计划选择的浏览器，给定默认值
	private String driverPath = "";//测试计划的浏览器驱动的路径，给定默认值
	private String followPlanPath = "";//测试计划执行完后，需要继续运行的测试计划的路径
	private List<IData> models = new ArrayList<>();//测试计划下测试模块的集合
	private IData parent;//测试计划的父元素，为null
	private boolean saveResult = false;
	private String saveResultPath = "";
	
	public String getName(){
		return planName;
	}
	public void setName(String planName){
		this.planName = planName;
	}
	public boolean getStatus(){
		return status;
	}
	public void setStatus(boolean status){
		this.status = status;
	}
	public DriverType getDriverType() {
		return driverType;
	}
	public void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}
	public String getDriverPath() {
		return driverPath;
	}
	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}
	public String getFollowPlanPath() {
		return followPlanPath;
	}
	public void setFollowPlanPath(String followPlanPath) {
		this.followPlanPath = followPlanPath;
	}
	public List<IData> getDatas() {
		return models;
	}
	public void setParent(IData data){
		this.parent = data;
	}
	public IData getParent(){
		return parent;
	}
	public boolean isLastData(){
		return true;
	}
	public IData getData(int index){
		return models.get(index);
	}
	public void delData(int index){
		models.remove(index);
	}
	@Override
	public void addData(IData data) {
		data.setParent(this);
		models.add(data);
	}
	public void addData(int index, IData data){
		data.setParent(this);
		models.add(index, data);
	}
	
	public int indexOf(IData data){
		return models.indexOf(data);
	}
	
	public void delData(IData data){
		models.remove(data);
	}
	
	public void setSaveResult(boolean saveResult){
		this.saveResult = saveResult;
	}
	
	public boolean getSaveResult(){
		return saveResult;
	}
	
	public void setSaveResultPath(String saveResultPath){
		this.saveResultPath = saveResultPath;
	}
	
	public String getSaveResultPath(){
		return saveResultPath;
	}
	public void init(){
		status = true;
		driverType = DriverType.values()[0];
		driverPath = "";
		followPlanPath = "";
		models.clear();
		saveResult = false;
		saveResultPath = "";
	}
}
