package king.selenium.view.left;

import javax.swing.tree.DefaultMutableTreeNode;

import king.selenium.data.Result;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��12��2�� ����3:35:41
* @ClassName ...
* @Description ������ڵ�Ķ���
*/
public class ResultNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;
	private KingType kingType;
	private Result result;
	
	public ResultNode(){}
	
	public ResultNode(Object useObject, boolean b, KingType kingType){
		super(useObject, b);
		this.kingType = kingType;
	}
	
	public KingType getKingType(){
		return kingType;
	}

	public ResultNode setResult(Result result){
		this.result = result;
		return this;
	}
	
	public Result getResult(){
		return result;
	}
}
