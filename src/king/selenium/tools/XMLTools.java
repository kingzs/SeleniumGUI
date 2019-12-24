package king.selenium.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import king.selenium.data.CaseData;
import king.selenium.data.DriverType;
import king.selenium.data.FileData;
import king.selenium.data.IData;
import king.selenium.data.ModelData;
import king.selenium.data.ParameterType;
import king.selenium.data.PlanData;
import king.selenium.data.Result;
import king.selenium.data.StepData;
import king.selenium.data.TableData;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月3日 下午5:33:38
* @ClassName ...
* @Description XML读写工具类，四个静态方法：测试计划写入文件，测试计划从文件读取，运行结果写入文件，运行结果从文件读取
*/
public class XMLTools {
	//把测试计划的数据，写到文件中
	public static void writeDataToFile(PlanData data, File file){
		FileOutputStream fos = null;
		XMLWriter writer = null;
		try {
			Document doc = DocumentHelper.createDocument();
			Element plan = doc.addElement("plan");
			plan.addAttribute("planName", data.getName());
			plan.addAttribute("driverType", data.getDriverType().getBrowserName());
			plan.addAttribute("driverPath", data.getDriverPath());
			plan.addAttribute("followPlanPath", data.getFollowPlanPath());
			plan.addAttribute("status", String.valueOf(data.getStatus()));
			plan.addAttribute("saveResult", String.valueOf(data.getSaveResult()));
			plan.addAttribute("saveResultPath", data.getSaveResultPath());
			for(IData md : data.getDatas()){
				ModelData imd = (ModelData) md;
				Element model = plan.addElement("model");
				model.addAttribute("modelName", imd.getName());
				model.addAttribute("status", String.valueOf(imd.getStatus()));
				for(IData cd : imd.getDatas()){
					CaseData icd = (CaseData) cd;
					Element caseElem = model.addElement("case");
					caseElem.addAttribute("caseName", icd.getName());
					caseElem.addAttribute("status", String.valueOf(icd.getStatus()));
					caseElem.addAttribute("useLoop", String.valueOf(icd.getLoop()));
					caseElem.addAttribute("loopCount", icd.getLoopCount()+"");
					caseElem.addAttribute("parameterType", String.valueOf(icd.getParameterType()));
					Element tableDataElem = caseElem.addElement("tableData");
					TableData tableData = icd.getTableData();
					tableDataElem.addAttribute("rows", tableData.getRows()+"");
					for(Object name : tableData.getParameterNames()){
						Element columnElement = tableDataElem.addElement("column");
						columnElement.addAttribute("colName", String.valueOf(name));
						for(Object colData : tableData.getParameterMapper().get(name)){
							columnElement.addElement("colData").setText(String.valueOf(colData));
						}
					}
					Element fileDataElem = caseElem.addElement("fileData");
					FileData fileData = icd.getFileData();
					fileDataElem.addAttribute("filePath", fileData.getFilePath());
					fileDataElem.addAttribute("fileEncoding", fileData.getFileEncoding());
					fileDataElem.addAttribute("separator", fileData.getSeparator());
					for(String pname:fileData.getParameterNames()){
						fileDataElem.addElement("pname").setText(pname);
					}
					for(IData sd : icd.getDatas()){
						StepData isd = (StepData) sd;
						Element step = caseElem.addElement("step");
						step.addElement("stepName").setText(isd.getName());
						step.addElement("status").setText(String.valueOf(isd.getStatus()));
						step.addElement("findType").setText(isd.getFindType());
						Element findParams = step.addElement("findParams");
						for(String param : isd.getFindParams()){
							findParams.addElement("param").setText(param);
						}
						step.addElement("timeout").setText(String.valueOf(isd.getTimeout()));
						step.addElement("followType").setText(isd.getFollowType());
						Element followParams = step.addElement("followParams");
						for(String param : isd.getFollowParams()){
							followParams.addElement("param").setText(param);
						}
						step.addElement("actionType").setText(isd.getActionType());
						Element actionParams = step.addElement("actionParams");
						for(String param : isd.getActionParams()){
							actionParams.addElement("param").setText(param);
						}
					}
				}
			}
			
			fos = new FileOutputStream(file);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			
			writer = new XMLWriter(fos, format);
			writer.write(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//从文件中读取测试计划的数据，用于渲染到界面上
	public static PlanData readDataFromFile(File file){
		PlanData planData = new PlanData();
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element plan = doc.getRootElement();
			planData.setName(plan.attributeValue("planName", "测试计划"));
			planData.setStatus(Boolean.parseBoolean(plan.attributeValue("status", "false")));
			String dt = plan.attributeValue("driverType", "谷歌浏览器");
			planData.setDriverType(DriverType.getDriverType(dt));
			planData.setDriverPath(plan.attributeValue("driverPath", ""));
			planData.setFollowPlanPath(plan.attributeValue("followPlanPath", ""));
			planData.setSaveResult(Boolean.parseBoolean(plan.attributeValue("saveResult", "false")));
			planData.setSaveResultPath(plan.attributeValue("saveResultPath", ""));
			Iterator<?> mit = plan.elementIterator("model");
			while(mit.hasNext()){
				ModelData modelData = new ModelData();
				Element melem = (Element) mit.next();
				modelData.setName(melem.attributeValue("modelName", "测试模块"));
				modelData.setStatus(Boolean.parseBoolean(melem.attributeValue("status", "false")));
				Iterator<?> cit = melem.elementIterator("case");
				while(cit.hasNext()){
					CaseData caseData = new CaseData();
					Element celem = (Element) cit.next();
					caseData.setName(celem.attributeValue("caseName", ""));
					caseData.setStatus(Boolean.parseBoolean(celem.attributeValue("status", "false")));
					caseData.setLoop(Boolean.parseBoolean(celem.attributeValue("useLoop", "false")));
					caseData.setLoopCount(Integer.parseInt(celem.attributeValue("loopCount", "0")));
					String pType = celem.attributeValue("parameterType", "TABLE");
					if("FILE".equals(pType)){
						caseData.setParameterType(ParameterType.FILE);
					}else{
						caseData.setParameterType(ParameterType.TABLE);
					}
					TableData tableData = caseData.getTableData();
					tableData.init();
					Element tableDataElem = celem.element("tableData");
					tableData.setRows(Integer.parseInt(tableDataElem.attributeValue("rows", "0")));
					Iterator<?> colit = tableDataElem.elementIterator("column");
					while(colit.hasNext()){
						Element colDataElem = (Element) colit.next();
						String colName = colDataElem.attributeValue("colName");
						List<Object> list = new ArrayList<Object>();
						Iterator<?> colDatait = colDataElem.elementIterator("colData");
						while(colDatait.hasNext()){
							list.add(((Element)colDatait.next()).getText());
						}
						tableData.addColumn(colName, list);
					}
					FileData fileData = caseData.getFileData();
					Element fileDataElem = celem.element("fileData");
					fileData.setFilePath(fileDataElem.attributeValue("filePath", ""));
					fileData.setFileEncoding(fileDataElem.attributeValue("fileEncoding", "GBK"));
					fileData.setSeparator(fileDataElem.attributeValue("separator", ","));
					Iterator<?> fpit = fileDataElem.elementIterator("pname");
					while(fpit.hasNext()){
						fileData.getParameterNames().add(((Element) fpit.next()).getText());
					}
					Iterator<?> sit = celem.elementIterator("step");
					while(sit.hasNext()){
						StepData stepData = new StepData();
						Element selem = (Element) sit.next();
						stepData.setName(selem.element("stepName").getText());
						stepData.setStatus(Boolean.parseBoolean(selem.element("status").getText()));
						stepData.setFindType(selem.element("findType").getText());
						Element findParams = selem.element("findParams");
						Iterator<?> findParamIt = findParams.elementIterator("param");
						while(findParamIt.hasNext()){
							Element param = (Element) findParamIt.next();
							stepData.addFindParam(param.getText());
						}
						stepData.setTimeout(Integer.parseInt(selem.element("timeout").getText()));
						stepData.setFollowType(selem.element("followType").getText());
						Element followParams = selem.element("followParams");
						Iterator<?> followParamIt = followParams.elementIterator("param");
						while(followParamIt.hasNext()){
							Element param = (Element) followParamIt.next();
							stepData.addFollowParam(param.getText());
						}
						stepData.setActionType(selem.element("actionType").getText());
						Element actionParams = selem.element("actionParams");
						Iterator<?> actionParamIt = actionParams.elementIterator("param");
						while(actionParamIt.hasNext()){
							Element param = (Element) actionParamIt.next();
							stepData.addActionParam(param.getText());
						}
						caseData.addData(stepData);
					}
					modelData.addData(caseData);
				}
				planData.addData(modelData);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return planData;
	}
	
	public static void writeResultToFile(Result result, File file){
		FileOutputStream fos = null;
		XMLWriter writer = null;
		try {
			Document doc = DocumentHelper.createDocument();
			Element resultPlanElem = doc.addElement("resultPlan");
			resultPlanElem.addAttribute("resultName", resultPlanElem.attributeValue("resultName"));
			resultPlanElem.addAttribute("status", result.getStatus());
			for(Result resultModel : result.getChildren()){
				Element resultModelElem = resultPlanElem.addElement("resultModel");
				resultModelElem.addAttribute("resultName", resultModel.getCaseName());
				resultModelElem.addAttribute("status", resultModel.getStatus());
				for(Result resultCase : resultModel.getChildren()){
					Element resultCaseElem = resultModelElem.addElement("resultCase");
					resultCaseElem.addAttribute("resultName", resultCase.getCaseName());
					resultCaseElem.addAttribute("status", resultCase.getStatus());
					for(Result resultStep : resultCase.getChildren()){
						Element resultStepElem = resultCaseElem.addElement("resultStep");
						resultStepElem.addAttribute("resultName", resultStep.getCaseName());
						resultStepElem.addAttribute("status", resultStep.getStatus());
						String findInfo = resultStep.getFindInfo();
						if(findInfo != null){
							resultStepElem.addAttribute("findInfo", findInfo.replace("\n", "&#10"));
						}
						String followInfo = resultStep.getFollowInfo();
						if(followInfo != null){
							resultStepElem.addAttribute("followInfo", followInfo.replace("\n", "&#10"));
						}
						String actionInfo = resultStep.getActionInfo();
						if(actionInfo != null){
							resultStepElem.addAttribute("actionInfo", actionInfo.replace("\n", "&#10"));
						}
					}
				}
			}
			
			fos = new FileOutputStream(file);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			
			writer = new XMLWriter(fos, format);
			writer.write(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Result readResultFromFile(File file){
		Result resultPlanData = new Result();
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element resultPlanElem = doc.getRootElement();
			resultPlanData.setCaseName(resultPlanElem.attributeValue("resultName", "测试计划"));
			Iterator<?> mit = resultPlanElem.elementIterator("resultModel");
			while(mit.hasNext()){
				Element melem = (Element) mit.next();
				Result resultModelData = new Result();
				resultModelData.setCaseName(melem.attributeValue("resultName", "测试模块"));
				resultModelData.setStatus(melem.attributeValue("status", "true"));
				Iterator<?> cit = melem.elementIterator("resultCase");
				while(cit.hasNext()){
					Element celem = (Element) cit.next();
					Result resultCaseData = new Result();
					resultCaseData.setCaseName(celem.attributeValue("resultName", "测试用例"));
					resultCaseData.setStatus(celem.attributeValue("status", "true"));
					Iterator<?> sit = celem.elementIterator("resultStep");
					while(sit.hasNext()){
						Element selem = (Element) sit.next();
						Result resultStepData = new Result();
						resultStepData.setCaseName(selem.attributeValue("resultName", "测试步骤"));
						resultStepData.setStatus(selem.attributeValue("status", "true"));
						resultStepData.setFindInfo(selem.attributeValue("findInfo", "").replace("&#10", "\n"));
						resultStepData.setFollowInfo(selem.attributeValue("followInfo", "").replace("&#10", "\n"));
						resultStepData.setActionInfo(selem.attributeValue("actionInfo", "").replace("&#10", "\n"));
						
						resultCaseData.addChild(resultStepData);
					}
					resultModelData.addChild(resultCaseData);
				}
				resultPlanData.addChild(resultModelData);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return resultPlanData;
	}
}


