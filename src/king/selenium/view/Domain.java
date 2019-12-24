package king.selenium.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import org.openqa.selenium.WebDriver;

import king.selenium.data.CaseData;
import king.selenium.data.IData;
import king.selenium.data.ModelData;
import king.selenium.data.PlanData;
import king.selenium.data.PlanRunThread;
import king.selenium.data.Result;
import king.selenium.data.StepData;
import king.selenium.tools.KingClassLoader;
import king.selenium.view.center.PlanConf;
import king.selenium.view.center.ResultView;
import king.selenium.view.center.TestCaseConf;
import king.selenium.view.center.TestModelConf;
import king.selenium.view.center.TestStepConf;
import king.selenium.view.left.CaseRightMenu;
import king.selenium.view.left.KingJPopupMenu;
import king.selenium.view.left.KingNode;
import king.selenium.view.left.KingTree;
import king.selenium.view.left.KingType;
import king.selenium.view.left.ModelRightMenu;
import king.selenium.view.left.PlanRightMenu;
import king.selenium.view.left.ResultNode;
import king.selenium.view.left.ResultNodeHandler;
import king.selenium.view.left.ResultTree;
import king.selenium.view.left.ResultTreeTask;
import king.selenium.view.left.StepRightMenu;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��23�� ����5:37:17
* @ClassName ...
* @Description �����࣬������ƣ����𴴽��ͱ�������Թ�������ʹ��
*/
public class Domain {

	public static Domain domain = new Domain();
	//ʱ���ʽת������������ʱ������ǰʱ��ʹ�����ת����ʽ����Ϊ�ļ���
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��_HHʱmm��ss��");
	private RootPane rootPane;//��panel
	private KingToolBar ktb;//���߲˵�
	private JScrollPane leftJsp;//ҳ����߷��õ���һ������ҳ��
	private KingTree tree;//�ű���
	private KingNode plan;//�ű��ĸ��ڵ㣬�����Լƻ��ڵ�
	private PlanData planData;//�ű��ĸ����ݣ��ڵ��������һһ��Ӧ��
	private JPanel center;//ҳ����м䲿��
	private PlanConf planConf;//���Լƻ�������ҳ��
	private TestStepConf stepConf;//���Բ��������ҳ��
	private TestModelConf modelConf;//����ģ�������ҳ��
	private TestCaseConf caseConf;//��������������ҳ��
	private WebDriver driver;//���������
	private KingClassLoader classLoader;//�Զ����������
	private KingNode oldNode;//�ű��е�ǰѡ�е�node
	private KingNode moveNode;//�ű�������������һ��node�ƶ��У��ƶ�����node
	private KingNode borderNode;//��ק�У�Ҫ���±߿��node
	private PlanRightMenu planRightMenu;//���Լƻ����Ҽ��˵�
	private ModelRightMenu modelRightMenu;//����ģ����Ҽ��˵�
	private CaseRightMenu caseRightMenu;//�����������Ҽ��˵�
	private StepRightMenu stepRightMenu;//���Բ�����Ҽ��˵�
	private MatteBorder border;
	//��ǰ�ļ��������е��ļ����򿪵��ļ�Ϊ��ǰ�ļ��������½ű���������ļ�Ϊ��ǰ�ļ������´��ٱ���ʱ����ֱ�ӱ��浽��ǰ�ļ�������Ҫ��ѡ���ļ�
	private File currentFile;
	//��ǰ�ű��Ƿ��и��£��и��£����½��ű������ļ����رմ���ʱ��������ʾ���Ƿ���Ҫ�������
	private boolean changeFile;
	//�ڽű��༭ҳ�滹�ǲ鿴���ҳ��
	private boolean codeOrResult;
	private ResultView resultView;//���н����������ʾҳ��
	private PlanRunThread planRunThread;//���нű����߳�
	private ResultTree resultTree;//�����
	private ResultNode resultRootNode;//������ĸ��ڵ�
	private Result resultPlan;//������ĸ�����
	private ResultNode resultNode;//������У���ǰѡ�еĽڵ�
	private IData copyData;//������ƣ�������ʱ�������������
	//�������Ҫ����Ľڵ�ԣ����нű��Ĺ����У����Ҫ�����Ľڵ�ԣ���һ���ڵ������һ���ڵ㣬���ڵ���ӽڵ�Ϊһ���ڵ�ԣ�
	//��װ��һ��ResultNodeHandler���棬��UI�߳��������Ӳ���
	private LinkedList<ResultNodeHandler> resultNodeHandlers;
	private ResultTreeTask resultTreeTask;//��ӽڵ�Ĵ������
	
	private Domain(){
		rootPane = new RootPane();
		ktb = new KingToolBar();
		leftJsp = new JScrollPane();
		leftJsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		leftJsp.setPreferredSize(new Dimension(240, 200));
		plan = new KingNode("���Լƻ�", true, KingType.PLAN);
		planData = new PlanData();
		plan.setData(planData);
		oldNode = plan;
		tree = new KingTree(plan, true);
		leftJsp.setViewportView(tree);
		center = new JPanel();
		planConf = new PlanConf();
		planConf.setPlanData(planData);
		center.add(planConf);
		modelConf = new TestModelConf();
		caseConf = new TestCaseConf();
		rootPane.add("North", ktb);
		rootPane.add(leftJsp, BorderLayout.WEST);
		rootPane.add(center, BorderLayout.CENTER);
		planRunThread = new PlanRunThread();
		resultNodeHandlers = new LinkedList<>();
		planRunThread.start();
		resultTreeTask = new ResultTreeTask();
		changeFile = false;
	}
	
	public static SimpleDateFormat getSimpleDateFormat(){
		return domain.sdf;
	}
	
	public static KingToolBar getToolBar(){
		return domain.ktb;
	}
	
	public static KingTree getTree(){
		return domain.tree;
	}
	
	public static KingNode getPlan(){
		return domain.plan;
	}
	
	public static PlanData getPlanData(){
		return domain.planData;
	}
	
	public static void setPlanData(PlanData planData){
		domain.planData = planData;
	}
	
	public static RootPane getRootPane(){
		return domain.rootPane;
	}
	
	public static JScrollPane getLeftJsp(){
		return domain.leftJsp;
	}
	
	public static void setClassLoader(KingClassLoader classLoader){
		domain.classLoader = classLoader;
		domain.planRunThread.setKingClassLoader(classLoader);
	}
	
	public static KingClassLoader getClassLoader(){
		return domain.classLoader;
	}
	
	public static TestStepConf getStepConf(){
		if(domain.stepConf == null){
			synchronized(Domain.class){
				if(domain.stepConf == null){
					domain.stepConf = new TestStepConf(domain.classLoader);
				}
			}
		}
		return domain.stepConf;
	}
	
	public static JPanel getCenter(){
		return domain.center;
	}
	
	public static PlanConf getPlanConf(){
		return domain.planConf;
	}
	
	public static TestModelConf getModelConf(){
		return domain.modelConf;
	}
	
	public static TestCaseConf getCaseConf(){
		return domain.caseConf;
	}
	
	public static void setDriver(WebDriver driver){
		domain.driver = driver;
	}
	
	public static WebDriver getDriver(){
		return domain.driver;
	}
	
	public static void closeDriver(){
		if(domain.driver != null){
			domain.driver.quit();
			domain.driver = null;
		}
	}
	
	public static void setNode(KingNode node){
		domain.oldNode = node;
	}
	
	public static KingNode getNode(){
		return domain.oldNode;
	}
	
	public static void setMoveNode(KingNode node){
		domain.moveNode = node;
	}
	
	public static KingNode getMoveNode(){
		return domain.moveNode;
	}
	
	public static void setBorderNode(KingNode borderNode){
		domain.borderNode = borderNode;
	}
	
	public static KingNode getBorderNode(){
		return domain.borderNode;
	}
	
	public static KingJPopupMenu getRightMenu(KingNode kingNode){
		KingJPopupMenu rightMenu;
		boolean pasteStatus = false;
		IData copyData = getCopyData();
		switch(kingNode.getKingType()){
		case PLAN:
			rightMenu = getPlanRightMenu().setKingNode(kingNode);
			if(copyData != null && copyData instanceof ModelData)
				pasteStatus = true;
			break;
		case MODEL:
			rightMenu = getModelRightMenu().setKingNode(kingNode);
			if(copyData != null && copyData instanceof CaseData)
				pasteStatus = true;
			break;
		case CASE:
			rightMenu = getCaseRightMenu().setKingNode(kingNode);
			if(copyData != null && copyData instanceof StepData)
				pasteStatus = true;
			break;
		case STEP:
			rightMenu = getStepRightMenu().setKingNode(kingNode);
			break;
		default:
			return null;
		}
		
		rightMenu.setPasteStatus(pasteStatus);
		return rightMenu;
	}
	
	private static PlanRightMenu getPlanRightMenu(){
		if(domain.planRightMenu == null){
			synchronized(Domain.class){
				if(domain.planRightMenu == null){
					domain.planRightMenu = new PlanRightMenu();
				}
			}
		}
		return domain.planRightMenu;
	}
	
	private static ModelRightMenu getModelRightMenu(){
		if(domain.modelRightMenu == null){
			synchronized(Domain.class){
				if(domain.modelRightMenu == null){
					domain.modelRightMenu = new ModelRightMenu();
				}
			}
		}
		return domain.modelRightMenu;
	}
	
	private static CaseRightMenu getCaseRightMenu(){
		if(domain.caseRightMenu == null){
			synchronized(Domain.class){
				if(domain.caseRightMenu == null){
					domain.caseRightMenu = new CaseRightMenu();
				}
			}
		}
		return domain.caseRightMenu;
	}
	
	private static StepRightMenu getStepRightMenu(){
		if(domain.stepRightMenu == null){
			synchronized(Domain.class){
				if(domain.stepRightMenu == null){
					domain.stepRightMenu = new StepRightMenu();
				}
			}
		}
		return domain.stepRightMenu;
	}
	
	public static MatteBorder getBorder(){
		if(domain.border == null){
			synchronized(Domain.class){
				if(domain.border == null){
					domain.border = new MatteBorder(0, 0, 1, 0, Color.BLUE);
				}
			}
		}
		return domain.border;
	}
	
	public static void setCurrentFile(File file){
		domain.currentFile = file;
	}
	
	public static File getCurrentFile(){
		return domain.currentFile;
	}
	
	public static void setChangeFile(boolean b){
		domain.changeFile = b;
	}
	
	public static boolean getChangeFile(){
		return domain.changeFile;
	}
	
	public static void setCodeOrResult(boolean flag){
		domain.codeOrResult = flag;
	}
	
	public static boolean getCodeOrResult(){
		return domain.codeOrResult;
	}
	
	public static ResultView getResultView(){
		if(domain.resultView == null){
			synchronized(Domain.class){
				if(domain.resultView == null){
					domain.resultView = new ResultView();
				}
			}
		}
		return domain.resultView;
	}
	
	public static PlanRunThread getPlanRunThread(){
		return domain.planRunThread;
	}
	
	public static ResultTree getResultTree(){
		if(domain.resultTree == null){
			synchronized(Domain.class){
				if(domain.resultTree == null){
					domain.resultTree = new ResultTree(getResultRootNode(), true);
				}
			}
		}
		return domain.resultTree;
	}
	
	public static ResultNode getResultRootNode(){
		if(domain.resultRootNode == null){
			synchronized(Domain.class){
				if(domain.resultRootNode == null){
					domain.resultRootNode = new ResultNode("���Լƻ�", true, KingType.PLAN);
					domain.resultPlan = new Result("���Լƻ�");
					domain.resultRootNode.setResult(domain.resultPlan);
				}
			}
		}
		return domain.resultRootNode;
	}
	
	public static Result getResultPlan(){
		return domain.resultPlan;
	}
	
	public static void setResultPlan(Result result){
		domain.resultPlan = result;
	}
	
	public static ResultNode getResultNode(){
		return domain.resultNode;
	}
	
	public static void setResultNode(ResultNode resultNode){
		domain.resultNode = resultNode;
	}

	public static IData getCopyData(){
		return domain.copyData;
	}
	
	public static void setCopyData(IData copyData){
		domain.copyData = copyData;
	}
	
	public static LinkedList<ResultNodeHandler> getResultNodeHandlers(){
		return domain.resultNodeHandlers;
	}
	
	public static void addResultNodeHandler(ResultNodeHandler resultNodeHandler){
		domain.resultNodeHandlers.addLast(resultNodeHandler);
	}
	
	public static ResultTreeTask getResultTreeTask(){
		return domain.resultTreeTask;
	}
}
