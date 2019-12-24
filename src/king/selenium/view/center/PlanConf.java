package king.selenium.view.center;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import king.selenium.data.DriverType;
import king.selenium.data.PlanData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��30�� ����2:14:33
* @ClassName ...
* @Description ���Լƻ�������ҳ��
*/
public class PlanConf extends JPanel {

	private static final long serialVersionUID = 1L;
	private PlanData data;//���Լƻ�����
	private JComboBox<Object> driverSelectType;//���������ѡ���������
	private JTextField driverPathText;//���������·���������
	private JTextField followText;//�������Լƻ���·�������
	private JCheckBox saveResult;//�Ƿ񱣴�����ѡ��
	private JTextField resultPathField;//��������·�������
	private boolean change = true;//�Ƿ����޸�
	
	public PlanConf(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		LeftFlowJPanel driverJPanel = new LeftFlowJPanel();
		JLabel driverType = new JLabel("�������");
		driverSelectType = new JComboBox<>(DriverType.getBrowserNames());
		JLabel driverPathLabel = new JLabel("������·����");
		driverPathText = new JTextField(20);
		driverJPanel.add(driverType);
		driverJPanel.add(driverSelectType);
		driverJPanel.add(driverPathLabel);
		driverJPanel.add(driverPathText);
		
		LeftFlowJPanel followJPanel = new LeftFlowJPanel();
		JLabel followLabel = new JLabel("����ִ�����ִ���������Լƻ���");
		followText = new JTextField(20);
		followText.setText("��δʵ��");
		followText.setEnabled(false);
		followJPanel.add(followLabel);
		followJPanel.add(followText);
		
		LeftFlowJPanel saveResultPanel = new LeftFlowJPanel();
		saveResult = new JCheckBox("�������н��");
		saveResultPanel.add(saveResult);
		
		LeftFlowJPanel resultJPanel = new LeftFlowJPanel();
		JLabel resultPathLabel = new JLabel("��������·����");
		resultPathField = new JTextField(30);
		resultPathField.setEnabled(false);
		resultJPanel.add(resultPathLabel);
		resultJPanel.add(resultPathField);
		
		LeftFlowJPanel tishiPanel1 = new LeftFlowJPanel();
		JLabel tishi1 = new JLabel("·��Ϊһ���ļ��У����Բ��������Ĭ�ϱ����ڳ���Ŀ¼�µ�resultĿ¼");
		tishiPanel1.add(tishi1);
		LeftFlowJPanel tishiPanel2 = new LeftFlowJPanel();
		JLabel tishi2 = new JLabel("����ʱ�����Խű����ƽ�һ���ļ��У��Խű����е�ʱ��Ϊ�ļ�����������");
		tishiPanel2.add(tishi2);
		
		this.add(Box.createHorizontalGlue());
		this.add(driverJPanel);
		this.add(followJPanel);
		this.add(saveResultPanel);
		this.add(resultJPanel);
		this.add(tishiPanel1);
		this.add(tishiPanel2);
		this.add(Box.createHorizontalGlue());
		//���������������ѡ������¼�
		driverSelectType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					if(change) Domain.setChangeFile(true);
					String value = (String) driverSelectType.getSelectedItem();
					data.setDriverType(DriverType.getDriverType(value));
				}
			}
		});
		//�����·�����̵����¼�
		driverPathText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(change) Domain.setChangeFile(true);
				data.setDriverPath(driverPathText.getText());
			}
		});
		//����ִ�мƻ�·�����̵����¼�
		followText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(change) Domain.setChangeFile(true);
				data.setFollowPlanPath(followText.getText());	
			}	
		});
		//�Ƿ񱣴�����ѡ��״̬�����¼�
		saveResult.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(change) Domain.setChangeFile(true);
				if(saveResult.isSelected()){
					resultPathField.setEnabled(true);
					data.setSaveResult(true);
				}else{
					resultPathField.setEnabled(false);
					data.setSaveResult(false);
				}
			}
		});
		//������·�������ļ��̵����¼�
		resultPathField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e){
				Domain.setChangeFile(true);
				data.setSaveResultPath(resultPathField.getText());
			}
		});
		
	}
	
	public void setDriverType(DriverType driverType){
		driverSelectType.setSelectedItem(driverType.getBrowserName());
	}
	
	public void setDriverPath(String path){
		driverPathText.setText(path);
	}
	
	public void setFollowPlanPath(String path){
		followText.setText(path);
	}
	
	public void setPlanData(PlanData data){
		this.data = data;
	}

	public PlanConf resetData(){
		change = false;
		data = (PlanData) Domain.getNode().getData();
		setDriverType(data.getDriverType());
		setDriverPath(data.getDriverPath());
		setFollowPlanPath(data.getFollowPlanPath());
		saveResult.setSelected(data.getSaveResult());
		resultPathField.setText(data.getSaveResultPath());
		if(data.getSaveResult()){
			resultPathField.setEnabled(true);
		}else{
			resultPathField.setEnabled(false);
		}
		change = true;
		return this;
	}
}
