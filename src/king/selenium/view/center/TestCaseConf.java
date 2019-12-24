package king.selenium.view.center;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import king.selenium.data.CaseData;
import king.selenium.data.ParameterType;
import king.selenium.data.TableData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��2�� ����9:16:55
* @ClassName ...
* @Description ������������ҳ��
*/
public class TestCaseConf extends JPanel {

	private static final long serialVersionUID = 1L;
	private CaseData caseData;
	private JTextField nameText;
	private JCheckBox useLoop;
	private JTextField loopCountText;
	private JPanel selectConf;
	private JRadioButton tableConfRadio;
	private JRadioButton fileConfRadio;
	private JPanel parameterConf;
	private TablePane tablePane;
	private FilePane filePane;
	private boolean change = true;

	public TestCaseConf(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel jp = new JPanel();
		JLabel nameLabel = new JLabel("��������");
		nameText = new JTextField(30);
		jp.add(nameLabel);
		jp.add(nameText);
		
		JPanel jp11 = new JPanel();
		useLoop = new JCheckBox("����ѭ��");
		JLabel loopCountLabel = new JLabel("ѭ��������");
		loopCountText = new JTextField(20);
		loopCountText.setEnabled(false);
		jp11.add(useLoop);
		jp11.add(loopCountLabel);
		jp11.add(loopCountText);
		
		selectConf = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		tableConfRadio = new JRadioButton("ʹ�ñ�����ò���");
		fileConfRadio = new JRadioButton("���ļ���ȡ����");
		bg.add(tableConfRadio);
		bg.add(fileConfRadio);
		
		parameterConf = new JPanel();
		tablePane = new TablePane();
		filePane = new FilePane();
		
		this.add(jp);
		this.add(jp11);
		this.add(selectConf);
		this.add(parameterConf);
		//���������������Ӽ���̧������¼�������̧�𣬻�ȡ���ݣ���ֵ����ߵĲ˵��������
		nameText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				String value = getCaseName();
				Domain.getNode().setUserObject(value);
				Domain.getTree().updateUI();
				caseData.setName(value);
				Domain.setChangeFile(true);
			}
		});
		//����ѭ�� ��ѡ���״̬���ļ����¼�
		useLoop.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(useLoop.isSelected()){
					selectConf.add(tableConfRadio);
					selectConf.add(fileConfRadio);
					if(caseData.getParameterType() == ParameterType.FILE){
						fileConfRadio.setSelected(true);
						parameterConf.removeAll();
						parameterConf.add(filePane);
					}else{
						tableConfRadio.setSelected(true);
						tablePane.getTableConf().setTableData(caseData.getTableData());
						parameterConf.removeAll();
						parameterConf.add(tablePane);
					}
					loopCountText.setEnabled(true);
					caseData.setLoop(true);
				}else{
					selectConf.remove(tableConfRadio);
					selectConf.remove(fileConfRadio);
					parameterConf.removeAll();
					loopCountText.setEnabled(false);
					caseData.setLoop(false);
				}
				if(change) Domain.setChangeFile(true);
				selectConf.updateUI();
			}
		});
		
		loopCountText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e){
				String value = loopCountText.getText().trim();
				if(value.matches("\\d+")){
					caseData.setLoopCount(Integer.parseInt(value));
				}else{
					caseData.setLoopCount(0);
				}
				Domain.setChangeFile(true);
			}
		});
		//ʹ�ñ�����ò��� ��ѡ��ť��״̬���ļ����¼�
		tableConfRadio.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				parameterConf.removeAll();
				if(tableConfRadio.isSelected()){
					parameterConf.add(tablePane);
					caseData.setParameterType(ParameterType.TABLE);
				}else{
					parameterConf.add(filePane);
					caseData.setParameterType(ParameterType.FILE);
				}
				if(change) Domain.setChangeFile(true);
				parameterConf.updateUI();
			}	
		});
		
	}
	
	public CaseData getCaseData(){
		return caseData;
	}
	
	public void setCaseName(String caseName){
		nameText.setText(caseName);
	}
	
	public String getCaseName(){
		return nameText.getText();
	}
	
	public TablePane getTablePane(){
		return tablePane;
	}
	//��������ҳ�����ݣ�������ǵ����ģ�����Ҫ�������������ݵķ���
	public TestCaseConf resetData(){
		change = false;
		CaseData caseData = (CaseData) Domain.getNode().getData();
		this.caseData = caseData;
		setCaseName(caseData.getName());
		int loopCount = caseData.getLoopCount();
		if(loopCount == 0){
			loopCountText.setText("");
		}else{
			loopCountText.setText(loopCount + "");
		}
		tablePane.getTableConf().setTableData(caseData.getTableData());
		filePane.setFileData(caseData.getFileData());
		
		if(caseData.getLoop()){
			useLoop.setSelected(true);
			switch(caseData.getParameterType()){
			case TABLE:
				tableConfRadio.setSelected(true);
				parameterConf.add(tablePane);
				break;
			case FILE:
				fileConfRadio.setSelected(true);
				parameterConf.add(filePane);
				break;
			default:
				tableConfRadio.setSelected(true);
				tablePane.getTableConf().setTableData(new TableData());
			}
		}else{
			useLoop.setSelected(false);
		}
		change = true;
		return this;
	}
}
