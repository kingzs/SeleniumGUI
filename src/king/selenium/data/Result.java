package king.selenium.data;

import java.util.ArrayList;
import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��26�� ����6:12:30
* @ClassName ...
* @Description �ű����н�������ݶ���
*/
public class Result {

	private String caseName;//���Բ������н�������ƣ����ǲ��Բ��������
	private String status = "�ɹ�";//���н����״̬�����ɹ�����ʧ�ܡ�
	private Result parent;
	private List<Result> children = new ArrayList<>();
	private String findInfo;//����Ԫ�صķ������е���ϸ��Ϣ
	private String followInfo;//�������ҵķ������е���ϸ��Ϣ
	private String actionInfo;//�����ķ������е���ϸ��Ϣ
	
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
