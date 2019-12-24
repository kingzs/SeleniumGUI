package king.selenium.view.left;
/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月12日 下午5:32:24
* @ClassName ...
* @Description 结果树添加节点，存放操作的节点对的容器
*/
public class ResultNodeHandler {

	public ResultNode parent;
	public ResultNode son;
	
	public ResultNodeHandler(ResultNode parent, ResultNode son){
		this.parent = parent;
		this.son = son;
	}
	
}
