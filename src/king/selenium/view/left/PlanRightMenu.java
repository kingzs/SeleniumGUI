package king.selenium.view.left;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��3�� ����9:36:55
* @ClassName ...
* @Description ���Լƻ��Ҽ��˵�����
*/
public class PlanRightMenu extends KingJPopupMenu {

	private static final long serialVersionUID = 1L;
	private JMenuItem paste;

	public PlanRightMenu(){
		JMenuItem add = new JMenuItem("��Ӳ���ģ��");
		JMenuItem copy = new JMenuItem("����");
		paste = new JMenuItem("ճ��");
		paste.setEnabled(false);
		
		this.add(add);
		this.addSeparator();
		this.add(copy);
		this.addSeparator();
		this.add(paste);
		
		//��Ӳ���ģ��˵������¼�
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Tools.setCurrentNode(kingNode.addChild(null), true);
				Domain.setChangeFile(true);
			}	
		});
		//���Ʋ˵������¼�
		copy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Domain.setCopyData(kingNode.getData());
			}		
		});
		//ճ���˵������¼�
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
