package king.selenium.view.left;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月3日 上午9:43:25
* @ClassName ...
* @Description 测试步骤的右键菜单定义
*/
public class StepRightMenu extends KingJPopupMenu {

	private static final long serialVersionUID = 1L;
	private JMenuItem useable;
	private JMenuItem paste;

	public StepRightMenu(){
		JMenuItem copy = new JMenuItem("复制");
		paste = new JMenuItem("粘贴");
		paste.setEnabled(false);
		useable = new JMenuItem("禁用");
		JMenuItem del = new JMenuItem("删除");
		
		this.add(copy);
		this.addSeparator();
		this.add(paste);
		this.addSeparator();
		this.add(useable);
		this.addSeparator();
		this.add(del);
		
		//复制菜单项点击事件
		copy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Domain.setCopyData(kingNode.getData());
			}		
		});
		//粘贴菜单项点击事件
		paste.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		//点击禁用/启用菜单项事件
		useable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean status = !kingNode.getData().getStatus();
				setStatus(status);
				kingNode.getData().setStatus(status);
				
				Domain.setChangeFile(true);
				Domain.getTree().updateUI();
			}		
		});
		
		//点击删除菜单项事件
		del.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Domain.setChangeFile(true);
				KingNode preNode = (KingNode) kingNode.getPreviousSibling();
				KingNode parent = (KingNode) kingNode.getParent();
				parent.remove(kingNode);
				if(preNode == null){
					Tools.setCurrentNode(parent);
				}else{
					Tools.setCurrentNode(preNode); 
				}
				parent.getData().delData(kingNode.getData());
				Domain.getTree().updateUI();
			}
		});
		
	}
	
	public void setStatus(boolean status){
		if(status){
			useable.setText("禁用");
			useable.setForeground(Color.DARK_GRAY);
		}else{
			useable.setText("启用");
			useable.setForeground(Color.LIGHT_GRAY);
		}
	}
	
	public void setPasteStatus(boolean status){
		paste.setEnabled(status);
	}
}
