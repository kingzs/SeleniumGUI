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
* @time 2019��9��6�� ����2:09:14
* @ClassName ...
* @Description ���ڲ������õ�JPanel
*/
public class ParameterJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;//����һ��label
	private JTextField textField;//����һ�������
	private List<String> list;//��indexһ��ȷ��һ�����ݣ������������ݽ��а�
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
	//����label������������
	public void reset(String name, List<String> datas, int ind){
		this.list = datas;
		this.index = ind;
		label.setText(name);
		textField.setText(datas.get(ind));
	}
	
}
