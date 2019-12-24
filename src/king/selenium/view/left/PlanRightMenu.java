package king.selenium.view.left;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月3日 上午9:36:55
* @ClassName ...
* @Description 测试计划右键菜单定义
*/
public class PlanRightMenu extends KingJPopupMenu {

	private static final long serialVersionUID = 1L;
	private JMenuItem paste;

	public PlanRightMenu(){
		JMenuItem add = new JMenuItem("添加测试模块");
		JMenuItem copy = new JMenuItem("复制");
		paste = new JMenuItem("粘贴");
		paste.setEnabled(false);
		
		this.add(add);
		this.addSeparator();
		this.add(copy);
		this.addSeparator();
		this.add(paste);
		
		//添加测试模块菜单项点击事件
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Tools.setCurrentNode(kingNode.addChild(null), true);
				Domain.setChangeFile(true);
			}	
		});
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
				Tools.pasteData(kingNode);
			}
		});
	}
	
	public void setStatus(boolean status){
		
	}
	
	public void setPasteStatus(boolean status){
		paste.setEnabled(status);
	}
}
