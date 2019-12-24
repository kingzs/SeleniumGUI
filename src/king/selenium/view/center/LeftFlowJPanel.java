package king.selenium.view.center;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月26日 上午10:54:09
* @ClassName ...
* @Description 居左流式布局的JPanel
*/
public class LeftFlowJPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public LeftFlowJPanel(){
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
}
