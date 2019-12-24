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
* @time 2019年8月30日 下午2:14:33
* @ClassName ...
* @Description 测试计划的配置页面
*/
public class PlanConf extends JPanel {

	private static final long serialVersionUID = 1L;
	private PlanData data;//测试计划数据
	private JComboBox<Object> driverSelectType;//浏览器类型选择的下拉框
	private JTextField driverPathText;//浏览器驱动路径的输入框
	private JTextField followText;//后续测试计划的路径输入框
	private JCheckBox saveResult;//是否保存结果复选框
	private JTextField resultPathField;//保存结果的路径输入框
	private boolean change = true;//是否监控修改
	
	public PlanConf(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		LeftFlowJPanel driverJPanel = new LeftFlowJPanel();
		JLabel driverType = new JLabel("浏览器：");
		driverSelectType = new JComboBox<>(DriverType.getBrowserNames());
		JLabel driverPathLabel = new JLabel("驱动器路径：");
		driverPathText = new JTextField(20);
		driverJPanel.add(driverType);
		driverJPanel.add(driverSelectType);
		driverJPanel.add(driverPathLabel);
		driverJPanel.add(driverPathText);
		
		LeftFlowJPanel followJPanel = new LeftFlowJPanel();
		JLabel followLabel = new JLabel("测试执行完后，执行其它测试计划：");
		followText = new JTextField(20);
		followText.setText("尚未实现");
		followText.setEnabled(false);
		followJPanel.add(followLabel);
		followJPanel.add(followText);
		
		LeftFlowJPanel saveResultPanel = new LeftFlowJPanel();
		saveResult = new JCheckBox("保存运行结果");
		saveResultPanel.add(saveResult);
		
		LeftFlowJPanel resultJPanel = new LeftFlowJPanel();
		JLabel resultPathLabel = new JLabel("保存结果的路径：");
		resultPathField = new JTextField(30);
		resultPathField.setEnabled(false);
		resultJPanel.add(resultPathLabel);
		resultJPanel.add(resultPathField);
		
		LeftFlowJPanel tishiPanel1 = new LeftFlowJPanel();
		JLabel tishi1 = new JLabel("路径为一个文件夹，可以不填，不填则默认保存在程序目录下的result目录");
		tishiPanel1.add(tishi1);
		LeftFlowJPanel tishiPanel2 = new LeftFlowJPanel();
		JLabel tishi2 = new JLabel("保存时，会以脚本名称建一个文件夹，以脚本运行的时间为文件名来保存结果");
		tishiPanel2.add(tishi2);
		
		this.add(Box.createHorizontalGlue());
		this.add(driverJPanel);
		this.add(followJPanel);
		this.add(saveResultPanel);
		this.add(resultJPanel);
		this.add(tishiPanel1);
		this.add(tishiPanel2);
		this.add(Box.createHorizontalGlue());
		//浏览器类型下拉框选项更改事件
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
		//浏览器路径键盘弹起事件
		driverPathText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(change) Domain.setChangeFile(true);
				data.setDriverPath(driverPathText.getText());
			}
		});
		//后续执行计划路径键盘弹起事件
		followText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(change) Domain.setChangeFile(true);
				data.setFollowPlanPath(followText.getText());	
			}	
		});
		//是否保存结果复选框状态更改事件
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
		//保存结果路径输入框的键盘弹起事件
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
