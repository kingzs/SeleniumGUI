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
* @time 2019年8月26日 上午9:56:10
* @ClassName ...
* @Description 界面工具栏菜单定义
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
		newPlan = new JButton("新建(N)");
		openFile = new JButton("打开(O)");
		savePlan = new JButton("保存(S)");
		savePlanOther = new JButton("另存为");
		runPlan = new JButton("运行");
		stopPlan = new JButton("停止");
		showResult = new JButton("查看结果");
		originalBackground = showResult.getBackground();
		clearResult = new JButton("清空结果");
		openResult = new JButton("打开结果");
		add(newPlan);
		add(openFile);
		add(savePlan);
		add(savePlanOther);
		add(runPlan);
		add(stopPlan);
		add(showResult);
		add(clearResult);
		add(openResult);
		//新建按钮的点击事件
		newPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newFile();
			}	
		});
		//打开按钮的点击事件
		openFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		//保存按钮的点击事件
		savePlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}		
		});
		//另存为按钮的点击事件
		savePlanOther.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				saveFileNoCurrentFile(true);
			}
		});
		//运行按钮的点击事件
		runPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Domain.getCurrentFile() == null){
					int code = JOptionPane.showConfirmDialog(runPlan.getRootPane(), "脚本第一次运行，需要先保存", "提示", JOptionPane.YES_NO_OPTION);
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
		//停止按钮的点击事件
		stopPlan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Domain.getPlanRunThread().getCanInterrupt()){//线程能否被直接打断
					Domain.getPlanRunThread().interrupt();//能打断就调用线程的打断方法
				}else{
					//不能打断就设置线程的打断标记为true，线程到了可打断的时候会判断这个值，
					//然后根据这个值，确定要不要停止脚本的执行
					Domain.getPlanRunThread().setInterrupt(true);
				}
			}		
		});
		//查看结果按钮的点击事件
		showResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Domain.getCodeOrResult()){
					showResult.setText("查看结果");
					showResult.setBackground(originalBackground);
					Domain.setCodeOrResult(false);
					Domain.getLeftJsp().setViewportView(Domain.getTree());
					Tools.setCurrentNode(Domain.getNode());
				}else{
					showResult.setText("查看脚本");
					showResult.setBackground(Color.GREEN);
					Domain.setCodeOrResult(true);
					Domain.getLeftJsp().setViewportView(Domain.getResultTree());
					Tools.setCurrentResultNode(Domain.getResultNode());
				}
			}		
		});
		//清空结果按钮的点击事件
		clearResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Domain.getResultRootNode().removeAllChildren();
				Domain.getResultRootNode().getResult().setStatus("成功");
				Domain.getResultTree().updateUI();
				Domain.setResultNode(null);
				Domain.getResultView().init();
			}		
		});
		//打开结果
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
		if(Domain.getChangeFile()){//如果当前脚本有改动未保存
			int result = JOptionPane.showConfirmDialog(newPlan.getRootPane(), "当前脚本有改动尚未保存，是否先保存？");
			if(result == 2){//如果用户点了取消，则返回true
				return true;
			}else if(result == 0){//如果用户点了是，则保存当前脚本
				saveFile(false);
			}else{//否则用户就是点了否，则什么也不做
				
			}
		}
		return false;
	}
	//保存当前脚本，参数为true，保存成功后会弹提示框，为false，保存成功后不会弹提示框
	public void saveFile(boolean b){
		PlanData planData = (PlanData) Domain.getPlan().getData();
		if(Domain.getCurrentFile() != null){//如果当前文件不为空，则脚本直接保存到当前文件
			XMLTools.writeDataToFile(planData, Domain.getCurrentFile());
			Domain.setChangeFile(false);//设置脚本为未修改状态
			if(b){
				JOptionPane.showMessageDialog(savePlan.getRootPane(), "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			return;
		}
		//当前文件为空，则弹出文件选择框，用户选择或输入一个文件名，保存脚本
		saveFileNoCurrentFile(b);
	}
	
	public void saveFileNoCurrentFile(boolean b){
		PlanData planData = (PlanData) Domain.getPlan().getData();
		JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath()+File.separator+"script");
		chooser.setFileFilter(filter);
		
		if(chooser.showSaveDialog(savePlan.getRootPane()) == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			//判断文件名是否以.xml结尾，如果不是，则加上.xml后缀
			if(!file.getName().toLowerCase().endsWith(".xml")){
				file = new File(file.getAbsolutePath()+".xml");
			}
			//判断文件是否存在，如果存在，提示用户是否要覆盖
			if(file.exists()){
				int code = JOptionPane.showConfirmDialog(savePlan.getRootPane(), "该文件已经存在，是否要覆盖？", "提示", JOptionPane.YES_NO_OPTION);
				if(code != JOptionPane.YES_OPTION){//用户选的不是确定，则返回，不保存文件
					return;
				}
			}
			XMLTools.writeDataToFile(planData, file);
			Domain.setCurrentFile(file);
			Domain.setChangeFile(false);
			if(b){
				JOptionPane.showMessageDialog(savePlan.getRootPane(), "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public void openFile(){
		//判断原先的脚本是否有改动未保存，并弹出提示，如果用户在提示框中点“取消”，则直接返回，后面的代码不执行
		if(changeHandle()){
			return;
		}
		//新建一个文件选择框，路径指定到程序目录下的script目录
		JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath()+File.separator+"script");
		chooser.setFileFilter(filter);//设置文件过滤器，只显示xml后缀的文件
		
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
		//判断原先的脚本是否有改动未保存，并弹出提示，如果用户在提示框中点“取消”，则直接返回，后面的代码不执行
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
