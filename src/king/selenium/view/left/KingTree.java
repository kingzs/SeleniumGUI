package king.selenium.view.left;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import king.selenium.data.IData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年11月29日 下午6:14:07
* @ClassName ...
* @Description 脚本树的定义
*/
public class KingTree extends JTree {

	private static final long serialVersionUID = 1L;
	private Icon notLeafIcon;
	private Icon leafIcon;
	
	
	public KingTree(){}
	
	public KingTree(TreeNode node, boolean flag){
		super(node, flag);
		
		notLeafIcon = UIManager.getIcon("Tree.openIcon");
		leafIcon = UIManager.getIcon("Tree.leafIcon");
		
		addTreeSelectionListener(new TreeSelectionListener(){
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				KingTree tree = (KingTree) e.getSource();
				KingNode node = (KingNode) tree.getLastSelectedPathComponent();
				Tools.setCurrentNode(node);
			}
		});
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					KingTree tree = (KingTree) e.getSource();
					int x = e.getX();
					int y = e.getY();
					TreePath path = tree.getPathForLocation(x, y);
					if(path != null){
						KingNode node = (KingNode) path.getLastPathComponent();
						Domain.getRightMenu(node).show(tree, x, y);
					}
				}
			}
		});
		
		//拖拽设置
		DragSource dragSource=DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new KingDragGestureListener());
		new DropTarget(this, new KingDropTargetListener());
		
	}
	
	public Icon getLeafIcon(){
		return leafIcon;
	}
	
	public Icon getNotLeafIcon(){
		return notLeafIcon;
	}
}

/**
 * 从被拖拽组件当中得到退拽数据
 * @author Administrator
 *
 */
class KingDragGestureListener implements DragGestureListener{
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		JTree tree = (JTree) dge.getComponent();
		TreePath path = tree.getSelectionPath();
		if (path != null) {
			dge.startDrag(DragSource.DefaultCopyDrop, new KingTransferable(), new KingDragSourceListener());
		}
	}
}

class KingTransferable implements Transferable {
	
	public KingTransferable(){}
 
	@Override
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		return null;
	}
 
	static DataFlavor flavors[] = { DataFlavor.stringFlavor };
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return true;
	}
}
 
/**
 * 设置拖拽鼠标样式的地方
 * @author Administrator
 *
 */
class  KingDragSourceListener implements  DragSourceListener{
 
	@Override
	public void dragDropEnd(DragSourceDropEvent arg0) {}
 
	@Override//进入拖拽
	public void dragEnter(DragSourceDragEvent e) {
	 	e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
	}
 
	@Override//离开拖拽
	public void dragExit(DragSourceEvent e) {
		e.getDragSourceContext().setCursor(DragSource.DefaultCopyNoDrop);
	}
 
	@Override
	public void dragOver(DragSourceDragEvent e) {}
 
	@Override
	public void dropActionChanged(DragSourceDragEvent e) {}
	
}
 
/**
 * 拖拽目的地所执行的操作
 * @author Administrator
 *
 */
class KingDropTargetListener implements DropTargetListener{
 
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {}
 
	@Override
	public void dragExit(DropTargetEvent dte) {}
 
	@Override//拖动过程中调用，鼠标移动到某个节点上，就为这个节点或其下最后一个叶子节点加个下边框，指示用户，松开鼠标，拖动的节点会移动到这个位置
	public void dragOver(DropTargetDragEvent dtde) {
		DropTarget target = (DropTarget) dtde.getSource();
        JTree targetTree = (JTree) target.getComponent();
        TreePath treePath=targetTree.getPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
        if(treePath == null){
        	if(Domain.getMoveNode() != null){
        		Domain.getBorderNode().setMoveStatus(false);
        		Domain.setMoveNode(null);
        		targetTree.updateUI();
        	}
        }else{
        	KingNode moveNode = (KingNode) treePath.getLastPathComponent();
        	if(moveNode == Domain.getNode()){
        		if(Domain.getBorderNode() != null){
        			Domain.getBorderNode().setMoveStatus(false);
        			Domain.setMoveNode(null);
        		}
        		targetTree.updateUI();
        		return;
        	}
    		if(Domain.getMoveNode() != null && (Domain.getBorderNode() != null)){
    			Domain.getBorderNode().setMoveStatus(false);
    		}
    		KingNode targetNode = findTargetNode(Domain.getNode(), moveNode);
    		if(targetNode == Domain.getNode()){
    			if(Domain.getBorderNode() != null) Domain.getBorderNode().setMoveStatus(false);
    			Domain.setMoveNode(null);
    			targetTree.updateUI();
    			return;
    		}
    		if(targetNode.getChildCount() > 0){
    			KingNode borderNode = (KingNode) targetNode.getLastChild();
    			borderNode.setMoveStatus(true);
    			Domain.setBorderNode(borderNode);
    		}else{
    			targetNode.setMoveStatus(true);//设置节点的moveStatus为true，自定义的渲染器就会为其加个下边框
    			Domain.setBorderNode(targetNode);
    		}
    		
    		Domain.setMoveNode(targetNode);
    		targetTree.updateUI();
        }
	}
 
	@Override//拖拽结束时候调用
	public void drop(DropTargetDropEvent dtde) {
		KingNode node = (KingNode) Domain.getNode();
		KingNode moveNode = Domain.getMoveNode();
		if(moveNode == null){
			return;
		}

		KingNode targetNode = findTargetNode(node, moveNode);
		KingNode parent = (KingNode) targetNode.getParent();
		if(parent == node.getParent() && targetNode == node){
			return;
		}
		
		if(targetNode.getKingType() != KingType.STEP && targetNode.getChildCount() <= 0 && 
				node.getKingType().ordinal() - targetNode.getKingType().ordinal() > 1){
			parent = targetNode;
			targetNode = targetNode.addChild(null);
		}
		
		IData data = node.getData();
		data.getParent().delData(data);
		((KingNode) node.getParent()).remove(node);
		
		if(node.getKingType().ordinal() - targetNode.getKingType().ordinal() == 1){
			targetNode.add(node);
			targetNode.getData().addData(data);
		}else{
			int index = parent.getIndex(targetNode);
			parent.insert(node, index+1);
			parent.getData().addData(index+1, data);
		}

		Domain.setChangeFile(true);
		Domain.getBorderNode().setMoveStatus(false);
		Domain.setMoveNode(null);
		Domain.getTree().updateUI();
	}
 
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	
	public KingNode findTargetNode(KingNode node, KingNode moveNode){
		if(moveNode.getKingType() == node.getKingType()){
			return moveNode;
		}else if(moveNode.getKingType().ordinal() > node.getKingType().ordinal()){
			return findTargetNode(node, (KingNode) moveNode.getParent());
		}else{
			if(moveNode.getChildCount() == 0){
				return moveNode;
			}else{
				return findTargetNode(node, (KingNode) moveNode.getLastChild());
			}
		}
	}
}
