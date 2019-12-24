package king.selenium.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import king.selenium.base.SeleniumContext;
import king.selenium.tools.KingClassLoader;
import king.selenium.tools.KingUtils;
import king.selenium.tools.XMLTools;
import king.selenium.view.Domain;
import king.selenium.view.left.KingType;
import king.selenium.view.left.ResultNode;
import king.selenium.view.left.ResultNodeHandler;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��29�� ����10:20:21
* @ClassName ...
* @Description ���нű����̶߳���
*/
public class PlanRunThread extends Thread {
	
	private KingClassLoader kcl;
	private boolean canInterrupt = true;//�߳��ܷ񱻴��
	//�̴߳�ϱ�ǣ������߳���Ҫ��ֹ����̣߳����ɴ��ʱ�������ֵ���ó�true��
	//�߳����е����Դ��ʱ����ȡ���ֵ���������Ƿ���ֹ�̵߳����С�
	private boolean interrupt = false;
	
	public void setKingClassLoader(KingClassLoader kcl){
		this.kcl = kcl;
	}
	public synchronized boolean getCanInterrupt(){
		return canInterrupt;
	}
	public synchronized void setCanInterrupt(boolean b){
		canInterrupt = b;
	}
	public void setInterrupt(boolean b){
		interrupt = b;
	}
	
	@Override
	public void run(){
		while(true){
			//�߳�һ�����������ͽ���ȴ�״̬����Ҫ��������ִ�У�ֻ��Ҫ����notifyAll����������ֵȴ�״̬����
			synchronized(Domain.class){
				try {
					Domain.class.wait();
					String resultFileName = Domain.getSimpleDateFormat().format(new Date());
					PlanData  planData = Domain.getPlanData();//��ȡ���Լƻ�����
					runDriver(planData);//���������
					setAllEntryDriver();//���������û��Զ�������ʵ����driverΪ�����������
					runPlan(planData, resultFileName);//���в��Լƻ�
				} catch (InterruptedException e) {
					continue;
				}catch(Exception e){
					e.printStackTrace();
					continue;
				}finally{
					XMLTools.writeDataToFile(Domain.getPlanData(), Domain.getCurrentFile());
					Domain.getToolBar().getRunPlan().setEnabled(true);
					Domain.closeDriver();
				}
			}
		}
	}
	
	private void runDriver(PlanData planData) throws InterruptedException, Exception {
		setCanInterrupt(false);//������������Ĺ����У��������̱߳������̴߳��
		try{
			String driverPath = planData.getDriverPath();
			DriverType driverType = planData.getDriverType();
			if("".equals(driverPath)){//���û������������·��������Ĭ��·������
				driverPath = new File("").getAbsolutePath() + File.separator + "driver" 
						+ File.separator + driverType.getDriverName();
			}
			System.setProperty(driverType.getPropertyName(), driverPath);
			Domain.setDriver((WebDriver) Class.forName(driverType.getClassName()).newInstance());
			
			sleep(100);
			Domain.getDriver().manage().window().maximize();//����������������
			setCanInterrupt(true);//�������������֮�������߳̿��Ա������̴߳��
			if(interrupt){
				interrupt = false;
				throw new InterruptedException();
			}
		}catch(Exception e){
			throw e;
		}
	}
	
	public void setAllEntryDriver(){
		for(SeleniumContext sc : Domain.getClassLoader().getAllEntry()){
			sc.setDriver(Domain.getDriver());
		}
	}
	
	private void runPlan(PlanData planData, String resultFileName) throws InterruptedException, Exception {
		Result resultPlan = Domain.getResultPlan();
		resultPlan.removeAllChildren();
		//�������Լƻ��µ����ݣ���ִ��
		try{
			for(IData modelData : planData.getDatas()){
				if(modelData.getStatus()){
					ResultNode modelNode = new ResultNode(modelData.getName(), true, KingType.MODEL);
					Result resultModel = new Result(modelData.getName());
					resultPlan.addChild(resultModel);
					modelNode.setResult(resultModel);
					Domain.addResultNodeHandler(new ResultNodeHandler(Domain.getResultRootNode(), modelNode));
					for(IData caseData : modelData.getDatas()){
						if(caseData.getStatus()){
							runCaseDataAll(modelNode, (CaseData) caseData);
						}
					}
				}
			}
		}finally{
			if(planData.getSaveResult()){
				String saveResultPath = planData.getSaveResultPath();
				String codeName = Domain.getCurrentFile().getName();
				codeName = codeName.substring(0, codeName.lastIndexOf("."));
				File resultDir = null;
				if("".equals(saveResultPath)){
					resultDir = new File("./result/" + codeName);
					if(!resultDir.exists()){
						resultDir.mkdirs();
					}
				}else{
					resultDir = new File(saveResultPath + codeName);
					if(!resultDir.exists()){
						resultDir.mkdirs();
					}
				}
				File resultFile = new File(resultDir.getAbsolutePath() + File.separator + resultFileName + ".xml");
				resultFile.createNewFile();
				XMLTools.writeResultToFile(resultPlan, resultFile);
			}
			synchronized(Domain.getResultTree()){
				Domain.getResultTree().notifyAll();
			}
		}
	}
	
	private void runCaseDataAll(ResultNode modelNode, CaseData caseData) throws InterruptedException, Exception {
		if(caseData.getLoop()){
			switch(caseData.getParameterType()){
			case TABLE:
				runCaseDataForTable(modelNode, caseData);
				break;
			case FILE:
				runCaseDataForFile(modelNode, caseData);
				break;
			default:
				
			}
		}else{
			runCaseDataOne(modelNode, caseData, caseData.getName());
		}
		
	}
	
	private void runCaseDataForTable(ResultNode modelNode, CaseData caseData) throws InterruptedException {
		int loopCount = caseData.getLoopCount();
		TableData tableData = caseData.getTableData();
		int rows = tableData.getRows();
		if(loopCount == 0) loopCount = rows;
		for(int i=0; i<loopCount; ++i){
			int currentRow = i%rows;
			if(currentRow == 0){
				tableData.modifyData(loopCount-i);
			}
			for(Object pName : tableData.getParameterNames()){
				String value = (String) tableData.getParameterMapper().get(pName).get(currentRow);
				if(value.endsWith("#")) value = value.substring(0, value.length()-1);
				caseData.putVariable((String) pName, value);
			}
			if(i/rows == 0){
				runCaseDataOne(modelNode, caseData, (String) tableData.getParameterMapper().get("��������").get(currentRow));
			}else{
				runCaseDataOne(modelNode, caseData, (String) tableData.getParameterMapper().get("��������").get(currentRow) + "_" + i/rows);
			}
		}
	}
	
	private void runCaseDataForFile(ResultNode modelNode, CaseData caseData) throws InterruptedException, IOException {
		int loopCount = caseData.getLoopCount();
		FileData fileData = caseData.getFileData();
		String filePath = fileData.getFilePath();
		String fileEncoding = fileData.getFileEncoding();
		int line = 0;
		String content = null;
		List<String> list = new ArrayList<>();
		
		if(loopCount == 0){
			fileData.modifyFile(loopCount);
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(KingUtils.analysisFileName(filePath)), fileEncoding))) {
				while((content = reader.readLine()) != null){
					list.clear();
					list.addAll(KingUtils.splitString(content, fileData.getSeparator()));
					for(int i=0,len=list.size(); i<len; ++i){
						caseData.putVariable(fileData.getParameterNames().get(i), list.get(i));
					}
					if(line == 0){
						runCaseDataOne(modelNode, caseData, caseData.getName());
					}else{
						runCaseDataOne(modelNode, caseData, caseData.getName()+"_"+line);
					}
					++line;
				}
			} catch (IOException e) {
				throw e;
			}
		}else{
			while(line < loopCount){
				fileData.modifyFile(loopCount-line);
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(KingUtils.analysisFileName(filePath)), fileEncoding))) {
					while(line < loopCount && (content = reader.readLine()) != null){
						list.clear();
						list.addAll(KingUtils.splitString(content, fileData.getSeparator()));
						for(int i=0,len=list.size(); i<len; ++i){
							caseData.putVariable(fileData.getParameterNames().get(i), list.get(i));
						}
						if(line == 0){
							runCaseDataOne(modelNode, caseData, caseData.getName());
						}else{
							runCaseDataOne(modelNode, caseData, caseData.getName()+"_"+line);
						}
						++line;
					}
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
	
	private void runCaseDataOne(ResultNode modelNode, CaseData caseData, String caseNodeName) throws InterruptedException {
		ResultNode caseNode = new ResultNode(caseNodeName, true, KingType.CASE);
		Result resultCase = new Result(caseNodeName);
		caseNode.setResult(resultCase);
		modelNode.getResult().addChild(resultCase);
		Domain.addResultNodeHandler(new ResultNodeHandler(modelNode, caseNode));
		for(IData stepData : caseData.getDatas()){
			if(stepData.getStatus()){
				StepData sData = (StepData) stepData;
				ResultNode stepNode = new ResultNode(sData.getName(), false, KingType.STEP);
				Result result = new Result();
				result.setCaseName(sData.getName());
				stepNode.setResult(result);
				resultCase.addChild(result);
				Domain.addResultNodeHandler(new ResultNodeHandler(caseNode, stepNode));
				
				Map<String, String> map = caseData.getVariableMap();
				
				try{
					runFindMethod(sData, result, map);
					runFollowMethod(sData, result, map);
					runActionMethod(sData, result, map);
					result.setStatus("�ɹ�");
				}catch(InterruptedException ie){
					throw ie;
				}catch(RunCodeException rce){
					
				}catch(Exception e){
					
				}
				
				errorBubble(result);
				
				synchronized(Domain.getResultTree()){
					Domain.getResultTree().notifyAll();
				}
			}
		}
	}
	
	private void runFindMethod(StepData stepData, Result result, Map<String, String> map) throws InterruptedException, RunCodeException, Exception {
		Method method = kcl.getKeyMethod(stepData.getFindType());
		SeleniumContext sc = kcl.getKeyEntry(stepData.getFindType());
		Object[] params = KingUtils.parameterHandle(map, stepData.getFindParams());
		try {
			int count = stepData.getTimeout()*2-1;
			boolean flag = true;
			WebElement element = null;
			while(count > 0){
				try{
					element = (WebElement) method.invoke(sc, params);
					flag = false;
					break;
				}catch(Exception e){
					--count;
					sleep(500);
					continue;
				}
			}
			if(flag){
				element = (WebElement) method.invoke(sc, params);
			}
			if(element != null){
				sc.setElement(element);
			}
			result.setFindInfo("��������" + stepData.getFindType() + "�������б�" + Arrays.toString(params)
					+ "\n\n������ɹ�");
		}catch(InvocationTargetException e1){
			Throwable t = e1.getTargetException();
			result.setFindInfo("��������" + stepData.getFindType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ�����񷽷��쳣��" + t.getMessage());
			result.setStatus("ʧ��");
			if(t instanceof InterruptedException) throw (InterruptedException) t;
			throw new RunCodeException();
		} catch (Exception e) {
			result.setFindInfo("��������" + stepData.getFindType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ��" + e.getMessage());
			result.setStatus("ʧ��");
			throw e;
		}
	}
	
	private void runFollowMethod(StepData stepData, Result result, Map<String, String> map) throws InterruptedException, RunCodeException, Exception {
		Method method = kcl.getFollowMethod(stepData.getFollowType());
		SeleniumContext sc = kcl.getFollowEntry(stepData.getFollowType());
		Object[] params = KingUtils.parameterHandle(map, stepData.getFollowParams());
		try{
			WebElement elementFollow = (WebElement) method.invoke(sc, params);
			if(elementFollow != null){
				sc.setElement(elementFollow);
			}
			result.setFollowInfo("��������" + stepData.getFollowType() + "�������б�" + Arrays.toString(params)
					+ "\n\n������ɹ�");
		}catch(InvocationTargetException e1){
			Throwable t = e1.getTargetException();
			result.setFollowInfo("��������" + stepData.getFollowType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ�����񷽷��쳣��" + t.getMessage());
			result.setStatus("ʧ��");
			if(t instanceof InterruptedException) throw (InterruptedException) t;
			throw new RunCodeException();
		} catch (Exception e) {
			result.setFollowInfo("��������" + stepData.getFollowType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ��" + e.getMessage());
			result.setStatus("ʧ��");
			if(e instanceof InterruptedException){
				throw e;
			}
		}
	}
	
	private void runActionMethod(StepData stepData, Result result, Map<String, String> map) throws InterruptedException, RunCodeException, Exception {
		Method method = kcl.getOperationMethod(stepData.getActionType());
		SeleniumContext sc = kcl.getOperationEntry(stepData.getActionType());
		Object[] params = KingUtils.parameterHandle(map, stepData.getActionParams());
		try{
			method.invoke(sc, params);
			result.setActionInfo("��������" + stepData.getActionType() + "�������б�" + Arrays.toString(params)
					+ "\n\n������ɹ�");
		}catch(InvocationTargetException e1){
			Throwable t = e1.getTargetException();
			result.setActionInfo("��������" + stepData.getActionType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ�����񷽷��쳣��" + t.getMessage());
			result.setStatus("ʧ��");
			if(t instanceof InterruptedException) throw (InterruptedException) t;
			throw new RunCodeException();
		} catch (Exception e) {
			result.setActionInfo("��������" + stepData.getActionType() + "�������б�" + Arrays.toString(params)
					+ "\n\n�����ʧ��\n��Ϣ��" + e.getMessage());
			result.setStatus("ʧ��");
			throw e;
		}
	}
	
	private void errorBubble(Result result){
		Result parent = result.getParent();
		if("ʧ��".equals(result.getStatus()) && "�ɹ�".equals(parent.getStatus())){
			parent.setStatus("ʧ��");
			if(parent.getParent() != null){
				errorBubble(parent);
			}
		}
	}
	
}
