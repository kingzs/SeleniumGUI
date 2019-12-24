package king.selenium.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月26日 上午9:59:51
* @ClassName ...
* @Description 根panel，界面上除了菜单，其它的都放在这个panel里面
*/
public class RootPane extends JPanel {

	private static final long serialVersionUID = 1L;

	public RootPane(){
		this.setLayout(new BorderLayout());
	}
}
