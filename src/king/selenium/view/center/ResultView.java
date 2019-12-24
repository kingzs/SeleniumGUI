package king.selenium.view.center;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import king.selenium.data.Result;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��23�� ����10:33:56
* @ClassName ...
* @Description ���Բ�������н����ϸ��Ϣ��ʾ����
*/
public class ResultView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField statusText;//���н���������
	private JTextArea discInfo;//����Ԫ�ص����н������ϸ��Ϣ���ı��������
	private JTextArea followInfo;//�������ҵ����н������ϸ��Ϣ���ı��������
	private JTextArea actionInfo;//���������н������ϸ��Ϣ���ı��������

	public ResultView(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel status = new JLabel("���н����");
		statusText = new JTextField(30);
		JPanel jp = new LeftFlowJPanel();
		jp.add(status);
		jp.add(statusText);
		
		JLabel discription = new JLabel("����Ԫ����Ϣ��");
		JPanel jp2 = new LeftFlowJPanel();
		jp2.add(discription);
		
		JScrollPane discScroll = new JScrollPane();
		discScroll.setPreferredSize(new Dimension(742, 180));
		discScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		discInfo = new JTextArea();
		Font font = discInfo.getFont();
		discInfo.setFont(new Font(font.getFamily(), font.getStyle(), 16));
		discScroll.setViewportView(discInfo);
		
		JLabel follow = new JLabel("����������Ϣ��");
		JPanel jp3 = new LeftFlowJPanel();
		jp3.add(follow);
		
		JScrollPane followScroll = new JScrollPane();
		followScroll.setPreferredSize(new Dimension(742, 180));
		followScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		followInfo = new JTextArea();
		followInfo.setFont(new Font(font.getFamily(), font.getStyle(), 16));
		followScroll.setViewportView(followInfo);
		
		JLabel action = new JLabel("������Ϣ��");
		JPanel jp4 = new LeftFlowJPanel();
		jp4.add(action);
		
		JScrollPane actionScroll = new JScrollPane();
		actionScroll.setPreferredSize(new Dimension(742, 180));
		actionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		actionInfo = new JTextArea();
		actionInfo.setFont(new Font(font.getFamily(), font.getStyle(), 16));
		actionScroll.setViewportView(actionInfo);
		
		add(jp);
		add(jp2);
		add(discScroll);
		add(jp3);
		add(followScroll);
		add(jp4);
		add(actionScroll);
	}
	
	public void init(){
		statusText.setText("");
		discInfo.setText("");
		followInfo.setText("");
		actionInfo.setText("");
	}
	
	public ResultView assicResult(Result result){
		statusText.setText(result.getStatus());
		discInfo.setText(result.getFindInfo());
		discInfo.setCaretPosition(0);
		followInfo.setText(result.getFollowInfo());
		followInfo.setCaretPosition(0);
		actionInfo.setText(result.getActionInfo());
		actionInfo.setCaretPosition(0);
		return this;
	}
}
