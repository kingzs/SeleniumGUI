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
* @time 2019��8��29�� ����6:14:56
* @ClassName ...
* @Description �ű����нڵ�Ķ���
*/
public class KingNode extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = 1L;
	private KingType kingType;//���ͣ��ǲ��Լƻ�������ģ�顢�������������Բ����е�һ��
	private IData data;//���ݣ�һ��TopJPanel��һ������
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
