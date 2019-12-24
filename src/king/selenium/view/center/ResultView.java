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
* @time 2019年9月23日 上午10:33:56
* @ClassName ...
* @Description 测试步骤的运行结果详细信息显示界面
*/
public class ResultView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField statusText;//运行结果的输入框
	private JTextArea discInfo;//查找元素的运行结果的详细信息的文本域输入框
	private JTextArea followInfo;//后续查找的运行结果的详细信息的文本域输入框
	private JTextArea actionInfo;//操作的运行结果的详细信息的文本域输入框

	public ResultView(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel status = new JLabel("运行结果：");
		statusText = new JTextField(30);
		JPanel jp = new LeftFlowJPanel();
		jp.add(status);
		jp.add(statusText);
		
		JLabel discription = new JLabel("查找元素信息：");
		JPanel jp2 = new LeftFlowJPanel();
		jp2.add(discription);
		
		JScrollPane discScroll = new JScrollPane();
		discScroll.setPreferredSize(new Dimension(742, 180));
		discScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		discInfo = new JTextArea();
		Font font = discInfo.getFont();
		discInfo.setFont(new Font(font.getFamily(), font.getStyle(), 16));
		discScroll.setViewportView(discInfo);
		
		JLabel follow = new JLabel("后续操作信息：");
		JPanel jp3 = new LeftFlowJPanel();
		jp3.add(follow);
		
		JScrollPane followScroll = new JScrollPane();
		followScroll.setPreferredSize(new Dimension(742, 180));
		followScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		followInfo = new JTextArea();
		followInfo.setFont(new Font(font.getFamily(), font.getStyle(), 16));
		followScroll.setViewportView(followInfo);
		
		JLabel action = new JLabel("操作信息：");
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
