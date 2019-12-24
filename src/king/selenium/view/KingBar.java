package king.selenium.view;

import java.awt.FlowLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月26日 上午9:50:03
* @ClassName ...
* @Description 界面菜单定义，暂时未添加事件
*/
public class KingBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public KingBar(){
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 2));
		JMenu fileMenu = new JMenu("文件");
		JMenuItem newItem = new JMenuItem("新建");
		JMenuItem openItem = new JMenuItem("打开");
		JMenuItem saveItem = new JMenuItem("保存");
		JMenuItem exitItem = new JMenuItem("退出");
		fileMenu.add(newItem);
		fileMenu.addSeparator();
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		JMenu editMenu = new JMenu("编辑");
		JMenuItem addItem = new JMenuItem("添加");
		JMenuItem copyItem = new JMenuItem("复制");
		JMenuItem pasteItem = new JMenuItem("粘贴");
		editMenu.add(addItem);
		editMenu.addSeparator();
		editMenu.add(copyItem);
		editMenu.addSeparator();
		editMenu.add(pasteItem);
		
		JMenu searchMenu = new JMenu("查找");
		JMenuItem search = new JMenuItem("查找");
		JMenuItem resetSearch = new JMenuItem("重置");
		searchMenu.add(search);
		searchMenu.addSeparator();
		searchMenu.add(resetSearch);
		
		JMenu helpMenu = new JMenu("帮助");
		JMenuItem help = new JMenuItem("使用说明");
		JMenuItem about = new JMenuItem("关于");
		helpMenu.add(help);
		helpMenu.addSeparator();
		helpMenu.add(about);
		
		this.add(fileMenu);
		this.add(editMenu);
		this.add(searchMenu);
		this.add(helpMenu);
	}
}
