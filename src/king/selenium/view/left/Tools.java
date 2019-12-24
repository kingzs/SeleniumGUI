package king.selenium.view.left;

import javax.swing.tree.TreePath;

import king.selenium.data.CaseData;
import king.selenium.data.IData;
import king.selenium.data.ModelData;
import king.selenium.data.StepData;
import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月20日 上午9:41:53
* @ClassName ...
* @Description 工具类，用于设置界面上脚本的当前选中项，以及运行结果中的当前选中项。
*/
public class Tools {

	public static void setCurrentNode(KingNode node){
		setCurrentNode(node, false);
	}
	//传入一个KingNode，就会把这个node设置为当前node，
	//再根据新的当前node的KingType，决定右边的界面显示什么配置界面
	public static void setCurrentNode(KingNode node, boolean update){
		if(node == null){
			return;
		}
		if(Domain.getNode() != node){
			if(update){
				Domain.getTree().updateUI();
			}
			Domain.getTree().setSelectionPath(new TreePath(node.getPath()));
			Domain.setNode(node);
		}
		
		Domain.getCenter().removeAll();
		switch(node.getKingType()){
		case PLAN:
			Domain.getCenter().add(Domain.getPlanConf().resetData());
			break;
		case MODEL:
			Domain.getCenter().add(Domain.getModelConf().resetData());
			break;
		case CASE:
			Domain.getCenter().add(Domain.getCaseConf().resetData());
			break;
		case STEP:
			Domain.getCenter().add(Domain.getStepConf().resetData());
			break;
		default:
			
		}
		Domain.getCenter().updateUI();
	}
	
	//传入一个ResultNode，把传入的ResultNode，设置成当前的resultNode
	//原先的当前resultNode，根据状态，还原成对应状态的背景色
	//把新的当前resultNode的结果数据，解析到右边的结果详情显示界面
	public static void setCurrentResultNode(ResultNode resultNode){
		if(resultNode == null){
			return;
		}
		Domain.setResultNode(resultNode);
		
		Domain.getCenter().removeAll();
		if(resultNode.getKingType() == KingType.STEP){
			Domain.getCenter().add(Domain.getResultView().assicResult(resultNode.getResult()));
		}
		Domain.getCenter().updateUI();
	}
	
	public static void pasteData(KingNode node){
		IData copyData = Domain.getCopyData();
		if(node == null){
			node = Domain.getNode();
		}
		switch(Domain.getNode().getKingType()){
		case PLAN:
			if(copyData != null && copyData instanceof ModelData){
				ModelData mData = (ModelData) Domain.getCopyData();
				ModelData copyModelData = mData.copyData();
				KingNode modelNode = node.addChild(copyModelData);
				node.getData().addData(copyModelData);
				for(IData caseData : copyModelData.getDatas()){
					KingNode caseNode = modelNode.addChild(caseData);
					for(IData stepData : caseData.getDatas()){
						caseNode.addChild(stepData);
					}
				}
				Domain.setChangeFile(true);
				Domain.getTree().updateUI();
			}
			break;
		case MODEL:
			if(copyData != null && copyData instanceof CaseData){
				CaseData cData = (CaseData) Domain.getCopyData();
				CaseData copyCaseData = cData.copyData();
				KingNode addNode = node.addChild(copyCaseData);
				node.getData().addData(copyCaseData);
				for(IData stepData : copyCaseData.getDatas()){
					addNode.addChild(stepData);
				}
				Domain.setChangeFile(true);
				Domain.getTree().updateUI();
			}
			break;
		case CASE:
			if(copyData != null && copyData instanceof StepData){
				StepData sData = (StepData) Domain.getCopyData();
				StepData copyStepData = sData.copyData();
				node.addChild(copyStepData);
				node.getData().addData(copyStepData);
				Domain.setChangeFile(true);
				Domain.getTree().updateUI();
			}
			break;
		default:
			
		}
	}
	
}
