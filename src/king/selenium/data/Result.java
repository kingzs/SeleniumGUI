package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月26日 下午6:12:30
* @ClassName ...
* @Description 脚本运行结果的数据定义
*/
public class Result {

	private String caseName;//测试步骤运行结果的名称，就是测试步骤的名称
	private String status = "成功";//运行结果的状态，“成功”或“失败”
	private Result parent;
	private List<Result> children = new ArrayList<>();
	private String findInfo;//查找元素的方法运行的详细信息
	private String followInfo;//后续查找的方法运行的详细信息
	private String actionInfo;//操作的方法运行的详细信息
	
	public Result(){}
	
	public Result(String caseName){
		this.caseName = caseName;
	}
	
	public String getCaseName(){
		return caseName;
	}
	public void setCaseName(String caseName){
		this.caseName = caseName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Result getParent(){
		return parent;
	}
	public void setParent(Result result){
		this.parent = result;
	}
	public String getFindInfo() {
		return findInfo;
	}
	public void setFindInfo(String findInfo) {
		this.findInfo = findInfo;
	}
	public String getFollowInfo() {
		return followInfo;
	}
	public void setFollowInfo(String followInfo) {
		this.followInfo = followInfo;
	}
	public String getActionInfo() {
		return actionInfo;
	}
	public void setActionInfo(String actionInfo) {
		this.actionInfo = actionInfo;
	}
	public List<Result> getChildren(){
		return children;
	}
	public void addChild(Result result){
		children.add(result);
		result.setParent(this);
	}
	public void removeAllChildren(){
		children.clear();
	}
}
