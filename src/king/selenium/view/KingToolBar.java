package king.selenium.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import king.selenium.data.CaseData;
import king.selenium.data.ModelData;
import king.selenium.data.PlanData;
import king.selenium.data.Result;
import king.selenium.data.StepData;
import king.selenium.tools.XMLTools;
import king.selenium.view.left.KingNode;
import king.selenium.view.left.KingType;
import king.selenium.view.left.ResultNode;
import king.selenium.view.left.Tools;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��26�� ����9:56:10
* @ClassName ...
* @Description ���湤�����˵�����
*/
public class KingToolBar extends JToolBar {

	private static final long serialVersionUID = 1L;
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("XML[*.xml]", "xml");
	private JButton newPlan;
	private JButton openFile;
	private JButton savePlan;
	private JButton savePlanOther;
	private JButton runPlan;
	private JButton stopPlan;
	private JButton showResult;
	private Color originalBackground;
	private JButton clearResult;
	private JButton openResult;
	
	public JButton getRunPlan(){
		return runPlan;
	}
	
	public KingToolBar(){
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
		newPlan = new JButton("�½�(N)");
		openFile = new JButton("��(O)");
		savePlan = new JButton("����(S)");
		savePlanOther = new JButton("���Ϊ");
		runPlan = new JButton("����");
		stopPlan = new JButton("ֹͣ");
		showResult = new JButton("�鿴���");
		originalBackground = showResult.getBackground();
		clearResult = new JButton("��ս��");
		openResult = new JButton("�򿪽��");
		add(newPlan);
		add(openFile);
		add(savePlan);
		add(savePlanOther);
		add(runPlan);
		add(stopPlan);
		add(showResult);
		add(clearResult);
		add(openResult);
		//�½���ť�ĵ���¼�
		newPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newFile();
			}	
		});
		//�򿪰�ť�ĵ���¼�
		openFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		//���水ť�ĵ���¼�
		savePlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}		
		});
		//���Ϊ��ť�ĵ���¼�
		savePlanOther.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				saveFileNoCurrentFile(true);
			}
		});
		//���а�ť�ĵ���¼�
		runPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Domain.getCurrentFile() == null){
					int code = JOptionPane.showConfirmDialog(runPlan.getRootPane(), "�ű���һ�����У���Ҫ�ȱ���", "��ʾ", JOptionPane.YES_NO_OPTION);
					if(code == JOptionPane.YES_OPTION){
						saveFile(false);
					}else{
						return;
					}
				}
				runPlan.setEnabled(false);
				synchronized(Domain.class){
					Domain.class.notifyAll();
				}
			}
		});
		//ֹͣ��ť�ĵ���¼�
		stopPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Domain.getPlanRunThread().getCanInterrupt()){//�߳��ܷ�ֱ�Ӵ��
					Domain.getPlanRunThread().interrupt();//�ܴ�Ͼ͵����̵߳Ĵ�Ϸ���
				}else{
					//���ܴ�Ͼ������̵߳Ĵ�ϱ��Ϊtrue���̵߳��˿ɴ�ϵ�ʱ����ж����ֵ��
					//Ȼ��������ֵ��ȷ��Ҫ��Ҫֹͣ�ű���ִ��
					Domain.getPlanRunThread().setInterrupt(true);
				}
			}		
		});
		//�鿴�����ť�ĵ���¼�
		showResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Domain.getCodeOrResult()){
					showResult.setText("�鿴���");
					showResult.setBackground(originalBackground);
					Domain.setCodeOrResult(false);
					Domain.getLeftJsp().setViewportView(Domain.getTree());
					Tools.setCurrentNode(Domain.getNode());
				}else{
					showResult.setText("�鿴�ű�");
					showResult.setBackground(Color.GREEN);
					Domain.setCodeOrResult(true);
					Domain.getLeftJsp().setViewportView(Domain.getResultTree());
					Tools.setCurrentResultNode(Domain.getResultNode());
				}
			}		
		});
		//��ս����ť�ĵ���¼�
		clearResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Domain.getResultRootNode().removeAllChildren();
				Domain.getResultRootNode().getResult().setStatus("�ɹ�");
				Domain.getResultTree().updateUI();
				Domain.setResultNode(null);
				Domain.getResultView().init();
			}		
		});
		//�򿪽��
		openResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath()+File.separator+"result");
				chooser.setFileFilter(filter);
				
				if(chooser.showOpenDialog(openFile.getRootPane()) == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					Result resultPlan = XMLTools.readResultFromFile(file);
					ResultNode resultPlanNode = Domain.getResultRootNode();
					resultPlanNode.removeAllChildren();
					resultPlanNode.setResult(resultPlan);
					Domain.setResultPlan(resultPlan);
					for(Result resultModel : resultPlan.getChildren()){
						ResultNode resultModelNode = new ResultNode(resultModel.getCaseName(), true, KingType.MODEL);
						resultModelNode.setResult(resultModel);
						resultPlanNode.add(resultModelNode);
						for(Result resultCase : resultModel.getChildren()){
							ResultNode resultCaseNode = new ResultNode(resultCase.getCaseName(), true, KingType.CASE);
							resultCaseNode.setResult(resultCase);
							resultModelNode.add(resultCaseNode);
							for(Result resultStep : resultCase.getChildren()){
								ResultNode resultStepNode = new ResultNode(resultStep.getCaseName(), false, KingType.STEP);
								resultStepNode.setResult(resultStep);
								resultCaseNode.add(resultStepNode);
							}
						}
					}
					Domain.getResultTree().updateUI();
				}
			}	
		});
		
	}
	
	public boolean changeHandle(){
		if(Domain.getChangeFile()){//�����ǰ�ű��иĶ�δ����
			int result = JOptionPane.showConfirmDialog(newPlan.getRootPane(), "��ǰ�ű��иĶ���δ���棬�Ƿ��ȱ��棿");
			if(result == 2){//����û�����ȡ�����򷵻�true
				return true;
			}else if(result == 0){//����û������ǣ��򱣴浱ǰ�ű�
				saveFile(false);
			}else{//�����û����ǵ��˷���ʲôҲ����
				
			}
		}
		return false;
	}
	//���浱ǰ�ű�������Ϊtrue������ɹ���ᵯ��ʾ��Ϊfalse������ɹ��󲻻ᵯ��ʾ��
	public void saveFile(boolean b){
		PlanData planData = (PlanData) Domain.getPlan().getData();
		if(Domain.getCurrentFile() != null){//�����ǰ�ļ���Ϊ�գ���ű�ֱ�ӱ��浽��ǰ�ļ�
			XMLTools.writeDataToFile(planData, Domain.getCurrentFile());
			Domain.setChangeFile(false);//���ýű�Ϊδ�޸�״̬
			if(b){
				JOptionPane.showMessageDialog(savePlan.getRootPane(), "����ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
			return;
		}
		//��ǰ�ļ�Ϊ�գ��򵯳��ļ�ѡ����û�ѡ�������һ���ļ���������ű�
		saveFileNoCurrentFile(b);
	}
	
	public void saveFileNoCurrentFile(boolean b){
		PlanData planData = (PlanData) Domain.getPlan().getData();
		JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath()+File.separator+"script");
		chooser.setFileFilter(filter);
		
		if(chooser.showSaveDialog(savePlan.getRootPane()) == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			//�ж��ļ����Ƿ���.xml��β��������ǣ������.xml��׺
			if(!file.getName().toLowerCase().endsWith(".xml")){
				file = new File(file.getAbsolutePath()+".xml");
			}
			//�ж��ļ��Ƿ���ڣ�������ڣ���ʾ�û��Ƿ�Ҫ����
			if(file.exists()){
				int code = JOptionPane.showConfirmDialog(savePlan.getRootPane(), "���ļ��Ѿ����ڣ��Ƿ�Ҫ���ǣ�", "��ʾ", JOptionPane.YES_NO_OPTION);
				if(code != JOptionPane.YES_OPTION){//�û�ѡ�Ĳ���ȷ�����򷵻أ��������ļ�
					return;
				}
			}
			XMLTools.writeDataToFile(planData, file);
			Domain.setCurrentFile(file);
			Domain.setChangeFile(false);
			if(b){
				JOptionPane.showMessageDialog(savePlan.getRootPane(), "����ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public void openFile(){
		//�ж�ԭ�ȵĽű��Ƿ��иĶ�δ���棬��������ʾ������û�����ʾ���е㡰ȡ��������ֱ�ӷ��أ�����Ĵ��벻ִ��
		if(changeHandle()){
			return;
		}
		//�½�һ���ļ�ѡ���·��ָ��������Ŀ¼�µ�scriptĿ¼
		JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath()+File.separator+"script");
		chooser.setFileFilter(filter);//�����ļ���������ֻ��ʾxml��׺���ļ�
		
		if(chooser.showOpenDialog(openFile.getRootPane()) == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			PlanData pd = XMLTools.readDataFromFile(file);
			KingNode plan = Domain.getPlan();
			plan.removeAllChildren();
			plan.setData(pd);
			Domain.setPlanData(pd);
			
			for(int i=0, len=pd.getDatas().size(); i<len; ++i){
				ModelData imd = (ModelData) pd.getData(i);
				KingNode modelNode = (KingNode) plan.addChild(imd);
				modelNode.setUserObject(imd.getName());
				for(int j=0, clen=imd.getDatas().size(); j<clen; ++j){
					CaseData icd = (CaseData) imd.getData(j);
					KingNode caseNode = (KingNode) modelNode.addChild(icd);
					caseNode.setUserObject(icd.getName());
					for(int k=0, slen=icd.getDatas().size(); k<slen; ++k){
						StepData isd = (StepData) icd.getData(k);
						KingNode stepNode = (KingNode) caseNode.addChild(isd);
						stepNode.setUserObject(isd.getName());
					}
				}
			}
			if(Domain.getNode() != Domain.getPlan()){
				Tools.setCurrentNode(Domain.getPlan());
			}
			Domain.getPlanConf().resetData();
			Domain.setCurrentFile(file);
			Domain.setChangeFile(false);
		}
		Domain.getTree().updateUI();
	}

	public void newFile(){
		//�ж�ԭ�ȵĽű��Ƿ��иĶ�δ���棬��������ʾ������û�����ʾ���е㡰ȡ��������ֱ�ӷ��أ�����Ĵ��벻ִ��
		if(changeHandle()){
			return;
		};
		Domain.setCurrentFile(null);
		KingNode plan = Domain.getPlan();
		plan.removeAllChildren();
		PlanData planData = new PlanData();
		plan.setData(planData);
		Domain.setPlanData(planData);
		Tools.setCurrentNode(plan);
		Domain.getTree().updateUI();
	}
}
