package king.selenium.view.center;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import king.selenium.data.FileData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年10月25日 下午3:04:28
* @ClassName ...
* @Description 测试用例配置页面，开启循环，使用文件配置参数，文件配置组件的定义
*/
public class FilePane extends JPanel {

	private static final long serialVersionUID = 1L;
	private FileData fileData;
	private JTextField filePathText;
	private JTextField encodingField;
	private JTextField paramSeparatorText;
	private JTextField paramNameListText;

	public FilePane(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel jp1 = new JPanel();
		JLabel filePathLabel = new JLabel("文件路径：");
		filePathText = new JTextField(30);
		jp1.add(filePathLabel);
		jp1.add(filePathText);
		
		JPanel explainPanel2 = new JPanel();
		JLabel explainLabel2 = new JLabel("文件路径支持默认路径、相对路径和绝对路径");
		explainPanel2.add(explainLabel2);
		
		JPanel encodingPanel = new JPanel();
		JLabel encodingLabel = new JLabel("文件的编码：");
		encodingField = new JTextField(30);
		encodingField.setText("GBK");
		encodingPanel.add(encodingLabel);
		encodingPanel.add(encodingField);
		
		JPanel jp2 = new JPanel();
		JLabel  paramSeparatorLabel = new JLabel("参数分隔符：");
		paramSeparatorText = new JTextField(30);
		paramSeparatorText.setText(",");
		jp2.add(paramSeparatorLabel);
		jp2.add(paramSeparatorText);
		
		JPanel jp3 = new JPanel();
		JLabel paramNameListLabel = new JLabel("参数名称列表：");
		paramNameListText = new JTextField(30);
		jp3.add(paramNameListLabel);
		jp3.add(paramNameListText);
		
		JPanel explainPanel = new JPanel();
		JLabel explainLabel = new JLabel("如果参数文件中有数据需要自动更新，则在数据之后添加一个#号");
		explainPanel.add(explainLabel);
		
		
		add(jp1);
		add(explainPanel2);
		add(encodingPanel);
		add(jp2);
		add(jp3);
		add(explainPanel);
		
		filePathText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				fileData.setFilePath(filePathText.getText());
				Domain.setChangeFile(true);
			}
		});
		
		encodingField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e){
				fileData.setFileEncoding(encodingField.getText());
				Domain.setChangeFile(true);
			}
		});
		
		paramSeparatorText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e){
				String separatorText = paramSeparatorText.getText().trim();
				if("".equals(separatorText)){
					fileData.setSeparator(",");
				}else{
					fileData.setSeparator(separatorText);
				}
				Domain.setChangeFile(true);
			}
		});
		
		paramNameListText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e){
				fileData.setParameterNames(paramNameListText.getText());
				Domain.setChangeFile(true);
			}
		});
	}
	
	public void setFileData(FileData fileData){
		this.fileData = fileData;
		filePathText.setText(fileData.getFilePath());
		encodingField.setText(fileData.getFileEncoding());
		paramSeparatorText.setText(fileData.getSeparator());
		paramNameListText.setText(fileData.getParameterNameStr());
	}
}
