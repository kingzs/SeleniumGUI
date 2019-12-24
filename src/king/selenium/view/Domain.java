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
* @time 2019年8月23日 下午5:37:17
* @ClassName ...
* @Description 容器类，单例设计，负责创建和保存对象，以供其它类使用
*/
public class Domain {

	public static Domain domain = new Domain();
	//时间格式转换器，保存结果时，将当前时间使用这个转换格式后，作为文件名
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日_HH时mm分ss秒");
	private RootPane rootPane;//根panel
	private KingToolBar ktb;//工具菜单
	private JScrollPane leftJsp;//页面左边放置的是一个滚动页面
	private KingTree tree;//脚本树
	private KingNode plan;//脚本的根节点，即测试计划节点
	private PlanData planData;//脚本的根数据，节点和数据是一一对应的
	private JPanel center;//页面的中间部分
	private PlanConf planConf;//测试计划的设置页面
	private TestStepConf stepConf;//测试步骤的设置页面
	private TestModelConf modelConf;//测试模块的设置页面
	private TestCaseConf caseConf;//测试用例的设置页面
	private WebDriver driver;//浏览器对象
	private KingClassLoader classLoader;//自定义类加载器
	private KingNode oldNode;//脚本中当前选中的node
	private KingNode moveNode;//脚本中鼠标左键按下一个node移动中，移动到的node
	private KingNode borderNode;//拖拽中，要加下边框的node
	private PlanRightMenu planRightMenu;//测试计划的右键菜单
	private ModelRightMenu modelRightMenu;//测试模块的右键菜单
	private CaseRightMenu caseRightMenu;//测试用例的右键菜单
	private StepRightMenu stepRightMenu;//测试步骤的右键菜单
	private MatteBorder border;
	//当前文件。打开已有的文件，打开的文件为当前文件；保存新脚本，保存的文件为当前文件，在下次再保存时，就直接保存到当前文件，不需要再选择文件
	private File currentFile;
	//当前脚本是否有更新，有更新，在新建脚本、打开文件、关闭窗口时，给出提示，是否需要保存更新
	private boolean changeFile;
	//在脚本编辑页面还是查看结果页面
	private boolean codeOrResult;
	private ResultView resultView;//运行结果的详情显示页面
	private PlanRunThread planRunThread;//运行脚本的线程
	private ResultTree resultTree;//结果树
	private ResultNode resultRootNode;//结果树的根节点
	private Result resultPlan;//结果树的根数据
	private ResultNode resultNode;//结果树中，当前选中的节点
	private IData copyData;//点击复制，数据暂时保存在这个变量
	//结果树中要处理的节点对，运行脚本的过程中，会把要操作的节点对（在一个节点下添加一个节点，父节点和子节点为一个节点对）
	//封装到一个ResultNodeHandler里面，在UI线程里，进行添加操作
	private LinkedList<ResultNodeHandler> resultNodeHandlers;
	private ResultTreeTask resultTreeTask;//添加节点的处理对象
	
	private Domain(){
		rootPane = new RootPane();
		ktb = new KingToolBar();
		leftJsp = new JScrollPane();
		leftJsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		leftJsp.setPreferredSize(new Dimension(240, 200));
		plan = new KingNode("测试计划", true, KingType.PLAN);
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
					domain.resultRootNode = new ResultNode("测试计划", true, KingType.PLAN);
					domain.resultPlan = new Result("测试计划");
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
