package king.selenium.view.left;

import javax.swing.JPopupMenu;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��18�� ����9:54:59
* @ClassName ...
* @Description ���Լƻ�������ģ�顢�������������Բ����Ҽ��˵��ĸ���
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
