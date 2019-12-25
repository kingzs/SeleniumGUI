package king.selenium.view.center;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import king.selenium.data.StepData;
import king.selenium.tools.KingClassLoader;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��23�� ����5:40:07
* @ClassName ...
* @Description ���Բ�������ҳ��
*/
public class TestStepConf extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameText;//������������
	private JComboBox<String> elementSelectType;//����Ԫ�ط���ѡ���������
	private JTextField timeField;//����Ԫ�س�ʱ�����
	private JPanel elementSelectParameterJPanel;//��������������ĸ�pane
	//��ѡȡԪ�ص�������ѡ��ı��ˣ����û�������������ĵģ�
	//�����л��˲��Բ�������ĸı䣬ͨ������������жϣ�
	//ͬ���ģ�������������������������������ж�
	private boolean reset = true;
	//��������أ����ڸ��ò������
	private LinkedList<ParameterJPanel> parameterJPanelPool = new LinkedList<>();
	private JComboBox<String> followOperator;//��������������ѡ���
	private JPanel followParameterJPanel;//���������Ĳ�������ĸ�pane
	private JComboBox<String> actionType;//����������ѡ���
	private JPanel actionParameterJPanel;//�����Ĳ�������ĸ�pane
//	private JComboBox<String> saveVariableType;//�������ݷ�ʽ������ѡ���
	private KingClassLoader classLoader;//�Զ��������������Ҫ������������
	private StepData sd;//���Բ�������
	private boolean change = true;

	public TestStepConf(KingClassLoader classLoader){
		this.classLoader = classLoader;
//===============================================  ���ֿ�ʼ    ===============================================
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		LeftFlowJPanel jp1 = new LeftFlowJPanel();
		JLabel nameLabel = new JLabel("�������ƣ�");
		nameText = new JTextField(30);
		jp1.add(nameLabel);
		jp1.add(nameText);
		
		LeftFlowJPanel jp29 = new LeftFlowJPanel();
		JLabel elementSelectLabel = new JLabel("Ԫ��ѡȡ��ʽ��");
		elementSelectType = new JComboBox<>(classLoader.getKeys());
		JLabel timeLabel = new JLabel("��ʱʱ�䣺");
		timeField = new JTextField(10);
		timeField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel secondLabel = new JLabel("  ��");
		jp29.add(elementSelectLabel);
		jp29.add(elementSelectType);
		jp29.add(timeLabel);
		jp29.add(timeField);
		jp29.add(secondLabel);
		
		elementSelectParameterJPanel = new LeftFlowJPanel();
		
		JPanel followSelectJPanel = new LeftFlowJPanel();
		JLabel followActionLabel = new JLabel("�������ң�");
		followOperator = new JComboBox<>(classLoader.getFollows());
		followSelectJPanel.add(followActionLabel);
		followSelectJPanel.add(followOperator);
		
		followParameterJPanel = new LeftFlowJPanel();
		
		JPanel actionSelectJPanel = new LeftFlowJPanel();
		JLabel opratorLabel = new JLabel("������");
		actionType = new JComboBox<>(classLoader.getOperates());
		actionSelectJPanel.add(opratorLabel);
		actionSelectJPanel.add(actionType);
		
		actionParameterJPanel = new LeftFlowJPanel();
		
//		LeftFlowJPanel jp5 = new LeftFlowJPanel();
//		JLabel saveVariableLabel = new JLabel("���������");
//		String[] variables = new String[]{"����Ԫ��", "����ֵ"};
//		saveVariableType = new JComboBox<>(variables);
//		JLabel variableNameLabel = new JLabel("�������ƣ�");
//		JTextField variableNameText = new JTextField(10);
//		jp5.add(saveVariableLabel);
//		jp5.add(saveVariableType);
//		jp5.add(variableNameLabel);
//		jp5.add(variableNameText);
		
		this.add(Box.createHorizontalStrut(10));
		this.add(jp1);
		this.add(Box.createHorizontalStrut(50));
		this.add(jp29);
		this.add(elementSelectParameterJPanel);
		this.add(followSelectJPanel);
		this.add(followParameterJPanel);
		this.add(actionSelectJPanel);
		this.add(actionParameterJPanel);
//		this.add(jp5);
		this.add(Box.createHorizontalStrut(100));
		
//===============================================  ���ֽ���    ===============================================
		
		//���������������Ӽ���̧���¼�
		nameText.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent arg0) {
				String value = nameText.getText();
				Domain.getNode().setUserObject(value);
				Domain.getTree().updateUI();
				sd.setName(value);
				Domain.setChangeFile(true);
			}	
		});	    
		
		//ѡ��Ԫ��������ѡ������¼�
		elementSelectType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					backParameterJPanel(elementSelectParameterJPanel);
					String value = (String) elementSelectType.getSelectedItem();
					sd.setFindType(value);
					if(reset){
						sd.removeAllFindParams();
						Domain.setChangeFile(true);
					}
					Parameter[] keys = classLoader.getKeyParameter(value);
					String[] keyNames = classLoader.getKeyNames(value);
					for(int i=0,len=keys.length; i<len; ++i){
						if(reset){
							sd.addFindParam("");
						}
						ParameterJPanel jp = getParameterJPanel(keyNames[i], sd.getFindParams(), i);
						elementSelectParameterJPanel.add(jp);
					}
					if(change) Domain.setChangeFile(true);
					elementSelectParameterJPanel.updateUI();
				}	
			}	
		});
		
		timeField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent arg0) {
				String value = timeField.getText().replaceAll("\\D*", "");
				timeField.setText(value);
				if("".equals(value)){
					sd.setTimeout(0);
				}else{
					sd.setTimeout(Integer.parseInt(value));
				}
				Domain.setChangeFile(true);
			}
		});
		
		//��������������ѡ������¼�
		followOperator.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					backParameterJPanel(followParameterJPanel);
					String value = (String)followOperator.getSelectedItem();
					sd.setFollowType(value);
					if(reset){
						sd.removeAllFollowParam();
						Domain.setChangeFile(true);
					}
					Parameter[] follows = classLoader.getFollowParameter(value);
					String[] followNames = classLoader.getFollowNames(value);
					for(int i=0, len=follows.length; i<len; ++i){
						if(reset){
							sd.addFollowParam("");
						}
						ParameterJPanel jp = getParameterJPanel(followNames[i], sd.getFollowParams(), i);
						followParameterJPanel.add(jp);
					}
					if(change) Domain.setChangeFile(true);
					followParameterJPanel.updateUI();
				}
			}	
		});
		
		//����������ѡ������¼�
		actionType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					backParameterJPanel(actionParameterJPanel);
					String value = (String)actionType.getSelectedItem();
					sd.setActionType(value);
					if(reset){
						sd.removeAllActionParams();
						Domain.setChangeFile(true);
					}
					Parameter[] operates = classLoader.getOperationParameter(value);
					String[] operateNames = classLoader.getOperationNames(value);
					for(int i=0, len=operates.length; i<len; ++i){
						if(reset){
							sd.addActionParam("");
						}
						ParameterJPanel jp = getParameterJPanel(operateNames[i], sd.getActionParams(), i);
						actionParameterJPanel.add(jp);
					}
					if(change) Domain.setChangeFile(true);
					actionParameterJPanel.updateUI();
				}
			}	
		});
		
	}
	//����ҳ������ݣ����ڽ����ϵ���˱�Ĳ��Բ��裬��Ҫ�������ҳ�������
	public TestStepConf resetData(){
		change = false;
		reset = false;
		sd = (StepData) Domain.getNode().getData();
		setStepName(sd.getName());
		if(sd.getFindType() == null){
			String value = classLoader.getKeys()[0];
			sd.setFindType(value);
			Parameter[] findParameters = classLoader.getKeyParameter(value);
			String[] findNames = classLoader.getKeyNames(value);
			elementSelectParameterJPanel.removeAll();
			for(int i=0,len=findParameters.length; i<len; ++i){
				sd.addFindParam("");
				elementSelectParameterJPanel.add(getParameterJPanel(findNames[i], sd.getFindParams(), i));
			}
			elementSelectType.setSelectedItem(classLoader.getKeys()[0]);
		}else{
			elementSelectType.setSelectedItem(sd.getFindType());
		}
		resetDatas(elementSelectParameterJPanel, sd.getFindParams());
		if(sd.getTimeout() == 0){
			timeField.setText("");
		}else{
			timeField.setText(String.valueOf(sd.getTimeout()));	
		}
		
		if(sd.getFollowType() == null){
			String value = classLoader.getFollows()[0];
			sd.setFollowType(value);
			Parameter[] followParameters = classLoader.getFollowParameter(value);
			String[] followNames = classLoader.getFollowNames(value);
			followParameterJPanel.removeAll();
			for(int i=0,len=followParameters.length; i<len; ++i){
				sd.addFollowParam("");
				ParameterJPanel pjp = getParameterJPanel(followNames[i], sd.getFollowParams(), i);
				followParameterJPanel.add(pjp);
			}
			followOperator.setSelectedItem(classLoader.getFollows()[0]);
		}else{
			followOperator.setSelectedItem(sd.getFollowType());
		}
		resetDatas(followParameterJPanel, sd.getFollowParams());
		
		if(sd.getActionType() == null){
			String value = classLoader.getOperates()[0];
			sd.setActionType(value);
			actionParameterJPanel.removeAll();
			Parameter[] actionParameters = classLoader.getOperationParameter(value);
			String[] actionNames = classLoader.getOperationNames(value);
			for(int i=0,len=actionParameters.length; i<len; ++i){
				sd.addActionParam("");
				ParameterJPanel pjp = getParameterJPanel(actionNames[i], sd.getActionParams(), i);
				actionParameterJPanel.add(pjp);
			}
			actionType.setSelectedItem(classLoader.getOperates()[0]);
		}else{
			actionType.setSelectedItem(sd.getActionType());
		}
		resetDatas(actionParameterJPanel, sd.getActionParams());
		
		updateUI();
		reset = true;
		change = true;
		return this;
	}
	
	public void setStepName(String stepName){
		nameText.setText(stepName);
	}
	//�Ӳ�����������ò������
	private ParameterJPanel getParameterJPanel(String labelText, List<String> list, int index){
		if(parameterJPanelPool.isEmpty()){//������������Ϊ�գ���newһ������
			return new ParameterJPanel(labelText, list, index);
		}else{//�����������ز�Ϊ�գ���Ӳ����������ȡ����һ��������һ�����ݣ�����
			ParameterJPanel pjp = parameterJPanelPool.remove();
			pjp.reset(labelText, list, index);
			return pjp;
		}
	}
	//�Ѳ�������Żص���������أ�����Ϊ��������ĸ����
	private void backParameterJPanel(JPanel e){
		for(Component comp : e.getComponents()){
			ParameterJPanel pjp = (ParameterJPanel) comp;
			parameterJPanelPool.addFirst(pjp);
			e.remove(comp);
		}
	}
	//���ò������������
	private void resetDatas(JPanel e, List<String> datas){
		int i = 0;
		for(Component comp : e.getComponents()){
			ParameterJPanel pjp = (ParameterJPanel) comp;
			pjp.setDataList(datas);
			pjp.setTextFieldValue(datas.get(i++));
		}
	}
	
}
