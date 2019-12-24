package king.selenium.view.left;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import king.selenium.data.Result;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月17日 下午4:34:07
* @ClassName ...
* @Description 结果树的渲染器
*/
public class ResultTreeCellRenderer extends JLabel implements TreeCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		ResultTree resultTree = (ResultTree) tree;
		ResultNode resultNode = (ResultNode) value;
		Result result = resultNode.getResult();
		this.setOpaque(true);
		if(leaf){
			this.setIcon(resultTree.getLeafIcon());
			this.setText(result.getCaseName());
		}else{
			this.setIcon(resultTree.getNotLeafIcon());
			this.setText(result.getCaseName());
		}
		
		if(selected){
			this.setBackground(Color.LIGHT_GRAY);
		}else if(result == null || result.getStatus() == null){
			this.setBackground(Color.WHITE);
		}else if("失败".equals(result.getStatus())){
			this.setBackground(new Color(250, 216, 216));
		}else{
			this.setBackground(Color.WHITE);
		}
		
		return this;
	}

}
