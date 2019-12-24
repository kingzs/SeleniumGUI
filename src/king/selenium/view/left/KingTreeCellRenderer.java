package king.selenium.view.left;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import king.selenium.data.IData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月9日 下午5:15:04
* @ClassName ...
* @Description 脚本树的渲染器
*/
public class KingTreeCellRenderer extends JLabel implements TreeCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		KingTree kingTree = (KingTree) tree;
		KingNode node = (KingNode) value;
		IData data = node.getData();
		if(leaf){
			this.setIcon(kingTree.getLeafIcon());
			this.setText(node.getUserObject().toString());
		}else{
			this.setIcon(kingTree.getNotLeafIcon());
			this.setText(node.getUserObject().toString());
		}
		if(selected){
			this.setOpaque(true);
			this.setBackground(Color.LIGHT_GRAY);
		}else{
			this.setOpaque(false);
			this.setBackground(Color.WHITE);
		}
		if(data.getStatus()){
			this.setForeground(Color.BLACK);
		}else{
			this.setForeground(Color.GRAY);
		}
		if(node.getMoveStatus()){
			this.setBorder(Domain.getBorder());
		}else{
			this.setBorder(null);
		}
		return this;
	}

}
