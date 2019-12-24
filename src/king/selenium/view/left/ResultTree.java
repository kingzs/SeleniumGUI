package king.selenium.view.left;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月2日 下午3:32:41
* @ClassName ...
* @Description 结果树的定义
*/
public class ResultTree extends JTree {

	private static final long serialVersionUID = 1L;
	private Icon leafIcon;
	private Icon notLeafIcon;
	
	public ResultTree(){}
	
	public ResultTree(TreeNode node, boolean flag){
		super(node, flag);
		
		leafIcon = UIManager.getIcon("Tree.leafIcon");
		notLeafIcon = UIManager.getIcon("Tree.openIcon");
		
		addTreeSelectionListener(new TreeSelectionListener(){
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				ResultTree tree = (ResultTree) e.getSource();
				ResultNode node = (ResultNode) tree.getLastSelectedPathComponent();
				Tools.setCurrentResultNode(node);
			}	
		});
	}
	
	public Icon getLeafIcon(){
		return leafIcon;
	}
	
	public Icon getNotLeafIcon(){
		return notLeafIcon;
	}
}
