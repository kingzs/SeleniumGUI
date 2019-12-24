package king.selenium.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import king.selenium.tools.KingUtils;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年11月1日 下午4:06:50
* @ClassName ...
* @Description 测试用例开启循环，使用文件配置参数，文件配置数据的定义
*/
public class FileData {

	private String filePath;
	private String fileEncoding;
	private String separator;
	private List<String> parameterNames;
	
	public FileData(){
		filePath = "";
		fileEncoding="GBK";
		separator = ",";
		parameterNames = new ArrayList<>();
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileEncoding(){
		return fileEncoding;
	}
	public void setFileEncoding(String fileEncoding){
		this.fileEncoding = fileEncoding;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public List<String> getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(String parameterNameStr) {
		parameterNames.clear();
		parameterNames.addAll(KingUtils.splitString(parameterNameStr, separator));
	}
	public String getParameterNameStr(){
		if(parameterNames.isEmpty()){
			return "";
		}else{
			StringBuffer buffer = new StringBuffer();
			for(String param : parameterNames){
				buffer.append(separator + param);
			}
			return buffer.substring(separator.length());
		}
	}
	
	public void modifyFile(int lines) throws IOException{
		BufferedReader reader = null;
		BufferedWriter writer = null;
		boolean flag = false;
		File file = KingUtils.analysisFileName(filePath);
		File tempFile = File.createTempFile("seleniumTemp", null);
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), fileEncoding));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), fileEncoding));
			String content = null;
			int line = 0;
			if("utf-8".equalsIgnoreCase(fileEncoding)){
				if((content=reader.readLine()) != null){
					if(lines == 0 || ++line <= lines){
						writer.write(modifyLine(content, separator).replace("\uFEFF", ""));
					}else{
						writer.write(content.replace("\uFEFF", ""));
					}
					writer.newLine();
				}
			}
			while((content=reader.readLine()) != null){
				if(lines == 0 || ++line <= lines){
					writer.write(modifyLine(content, separator));
				}else{
					writer.write(content);
				}
				writer.newLine();
			}
			flag = true;
		}finally{
			if(writer != null){
				writer.close();
			}
			if(reader != null){
				reader.close();
			}
			if(flag)
			copyFile(tempFile, file);
			tempFile.delete();
		}
	}
	
	private String modifyLine(String content, String separator){
		StringBuffer buffer = new StringBuffer();
		int len = separator.length();
		int start = 0;
		int index = 0;
		while(true){
			index = content.indexOf(separator, start);
			if(index < 0){
				buffer.append(KingUtils.parameterAdd(content.substring(start).trim()));
				break;
			}else{
				buffer.append(KingUtils.parameterAdd(content.substring(start, index).trim()) + separator);
			}
			start = index + len;
		}
		return buffer.toString();
	}
	
	private void copyFile(File sourceFile, File targetFile){
		FileInputStream in = null;
		FileOutputStream out = null;
		FileChannel sourceFileChannel = null;
		FileChannel targetFileChannel = null;
		try{
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(targetFile);
			sourceFileChannel = in.getChannel();
			targetFileChannel = out.getChannel();
			targetFileChannel.transferFrom(sourceFileChannel, 0, sourceFileChannel.size());
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(targetFileChannel != null) targetFileChannel.close();
				if(sourceFileChannel != null) sourceFileChannel.close();
				if(out != null) out.close();
				if(in != null) in.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public void copyData(FileData fileData){
		this.filePath = fileData.filePath;
		this.fileEncoding = fileData.fileEncoding;
		this.separator = fileData.separator;
		this.parameterNames.addAll(fileData.parameterNames);
	}
}
