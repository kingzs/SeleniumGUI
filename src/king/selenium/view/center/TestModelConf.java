package king.selenium.view.center;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月2日 上午9:09:05
* @ClassName ...
* @Description 测试模块配置页面
*/
public class TestModelConf extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameText;

	public TestModelConf(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel jp = new JPanel();
		JLabel nameLabel = new JLabel("模块名称");
		nameText = new JTextField(30);
		
		nameText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent arg0) {
				String value = getModelName();
				Domain.getNode().setUserObject(value);
				Domain.getTree().updateUI();
				Domain.getNode().getData().setName(value);
				Domain.setChangeFile(true);
			}	
		});
		
		jp.add(nameLabel);
		jp.add(nameText);
		this.add(jp);
	}
	
	public void setModelName(String modelName){
		nameText.setText(modelName);
	}
	
	public String getModelName(){
		return nameText.getText();
	}
	
	public TestModelConf resetData(){
		setModelName(Domain.getNode().getData().getName());
		return this;
	}
}
