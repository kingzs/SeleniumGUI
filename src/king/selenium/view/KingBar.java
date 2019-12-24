package king.selenium.view;

import java.awt.FlowLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��26�� ����9:50:03
* @ClassName ...
* @Description ����˵����壬��ʱδ����¼�
*/
public class KingBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public KingBar(){
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 2));
		JMenu fileMenu = new JMenu("�ļ�");
		JMenuItem newItem = new JMenuItem("�½�");
		JMenuItem openItem = new JMenuItem("��");
		JMenuItem saveItem = new JMenuItem("����");
		JMenuItem exitItem = new JMenuItem("�˳�");
		fileMenu.add(newItem);
		fileMenu.addSeparator();
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		JMenu editMenu = new JMenu("�༭");
		JMenuItem addItem = new JMenuItem("���");
		JMenuItem copyItem = new JMenuItem("����");
		JMenuItem pasteItem = new JMenuItem("ճ��");
		editMenu.add(addItem);
		editMenu.addSeparator();
		editMenu.add(copyItem);
		editMenu.addSeparator();
		editMenu.add(pasteItem);
		
		JMenu searchMenu = new JMenu("����");
		JMenuItem search = new JMenuItem("����");
		JMenuItem resetSearch = new JMenuItem("����");
		searchMenu.add(search);
		searchMenu.addSeparator();
		searchMenu.add(resetSearch);
		
		JMenu helpMenu = new JMenu("����");
		JMenuItem help = new JMenuItem("ʹ��˵��");
		JMenuItem about = new JMenuItem("����");
		helpMenu.add(help);
		helpMenu.addSeparator();
		helpMenu.add(about);
		
		this.add(fileMenu);
		this.add(editMenu);
		this.add(searchMenu);
		this.add(helpMenu);
	}
}
