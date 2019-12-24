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
* @time 2019��9��20�� ����9:41:53
* @ClassName ...
* @Description �����࣬�������ý����Ͻű��ĵ�ǰѡ����Լ����н���еĵ�ǰѡ���
*/
public class Tools {

	public static void setCurrentNode(KingNode node){
		setCurrentNode(node, false);
	}
	//����һ��KingNode���ͻ�����node����Ϊ��ǰnode��
	//�ٸ����µĵ�ǰnode��KingType�������ұߵĽ�����ʾʲô���ý���
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
	
	//����һ��ResultNode���Ѵ����ResultNode�����óɵ�ǰ��resultNode
	//ԭ�ȵĵ�ǰresultNode������״̬����ԭ�ɶ�Ӧ״̬�ı���ɫ
	//���µĵ�ǰresultNode�Ľ�����ݣ��������ұߵĽ��������ʾ����
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
