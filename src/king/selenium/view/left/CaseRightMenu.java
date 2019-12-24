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
* @time 2019��9��3�� ����9:43:02
* @ClassName ...
* @Description �����������Ҽ��˵�
*/
public class CaseRightMenu extends KingJPopupMenu {

	private static final long serialVersionUID = 1L;
	private JMenuItem useable;
	private JMenuItem paste;

	public CaseRightMenu(){
		JMenuItem add = new JMenuItem("���Ӳ��Բ���");
		JMenuItem copy = new JMenuItem("����");
		paste = new JMenuItem("ճ��");
		paste.setEnabled(false);
		useable = new JMenuItem("����");
		JMenuItem del = new JMenuItem("ɾ��");
		
		add(add);
		addSeparator();
		add(copy);
		addSeparator();
		add(paste);
		addSeparator();
		add(useable);
		addSeparator();
		add(del);
		
		//������Ӳ��Բ��谴ť�¼�
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
		//����������ð�ť
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
		
		//���ɾ����ť�¼�
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
			useable.setText("����");
			useable.setForeground(Color.DARK_GRAY);
		}else{
			useable.setText("����");
			useable.setForeground(Color.LIGHT_GRAY);
		}
	}
	
	public void setPasteStatus(boolean status){
		paste.setEnabled(status);
	}
}