package king.selenium.view.left;

import javax.swing.tree.DefaultMutableTreeNode;

import king.selenium.data.CaseData;
import king.selenium.data.IData;
import king.selenium.data.ModelData;
import king.selenium.data.StepData;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月29日 下午6:14:56
* @ClassName ...
* @Description 脚本树中节点的定义
*/
public class KingNode extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = 1L;
	private KingType kingType;//类型，是测试计划、测试模块、测试用例、测试步骤中的一种
	private IData data;//数据，一个TopJPanel绑定一个数据
	private boolean moveStatus;
	
	public KingNode(Object useObject, boolean b, KingType kingType){
		super(useObject, b);
		this.kingType = kingType;
	}
	
	public KingType getKingType(){
		return kingType;
	}
	
	public KingNode addChild(IData childData){
		KingNode node = null;
		switch(kingType){	
		case PLAN:
			if(childData == null){
				childData = new ModelData();
				data.addData(childData);
			}
			node = new KingNode(childData.getName(), true, KingType.MODEL);
			node.setData(childData);
			add(node);
			break;
		case MODEL:
			if(childData == null){
				childData = new CaseData();
				data.addData(childData);
			}
			node = new KingNode(childData.getName(), true, KingType.CASE);
			node.setData(childData);
			add(node);
			break;
		case CASE:
			if(childData == null){
				childData = new StepData();
				data.addData(childData);
			}
			node = new KingNode(childData.getName(), false, KingType.STEP);
			node.setData(childData);
			add(node);
			break;
		default:
			
		}
		return node;
	}
	
	public IData getData(){
		return data;
	}
	
	public void setData(IData data){
		this.data = data;
	}
	
	public boolean getMoveStatus(){
		return moveStatus;
	}
	
	public void setMoveStatus(boolean moveStatus){
		this.moveStatus = moveStatus;
	}
}
