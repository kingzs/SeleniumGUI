package king.selenium.view.center;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月6日 下午2:09:14
* @ClassName ...
* @Description 用于参数配置的JPanel
*/
public class ParameterJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;//定义一个label
	private JTextField textField;//定义一个输入框
	private List<String> list;//与index一起，确定一个数据，与输入框的内容进行绑定
	private int index;
	
	public ParameterJPanel(String name, List<String> datas, int ind){
		this.list = datas;
		this.index = ind;
		label = new JLabel(name);
		textField = new JTextField(10);
		textField.setText(datas.get(ind));
		textField.setToolTipText(textField.getText());
		this.add(label);
		this.add(textField);
		
		textField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent arg0) {
				list.set(index, textField.getText().trim());
				Domain.setChangeFile(true);
			}
		});
		
		textField.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				JTextField textField = (JTextField) e.getSource();
				textField.setToolTipText(textField.getText());
			}
		});
	}
	
	public void setLabelText(String text){
		label.setText(text);
	}
	
	public void setTextFieldValue(String value){
		textField.setText(value);
	}
	
	public void setDataList(List<String> list){
		this.list = list;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	//重置label和输入框的内容
	public void reset(String name, List<String> datas, int ind){
		this.list = datas;
		this.index = ind;
		label.setText(name);
		textField.setText(datas.get(ind));
	}
	
}
