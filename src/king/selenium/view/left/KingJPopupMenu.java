package king.selenium.view.left;

import javax.swing.JPopupMenu;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月18日 上午9:54:59
* @ClassName ...
* @Description 测试计划、测试模块、测试用例、测试步骤右键菜单的父类
*/
public abstract class KingJPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = 1L;
	protected KingNode kingNode;
	
	public KingJPopupMenu setKingNode(KingNode kingNode){
		this.kingNode = kingNode;
		setStatus(kingNode.getData().getStatus());
		return this;
	}
	
	public KingNode getKingNode(){
		return kingNode;
	}
	
	public abstract void setStatus(boolean status);
	
	public abstract void setPasteStatus(boolean status);

}
