package king.selenium.start;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月11日 上午9:12:41
* @ClassName ...
* @Description ...
*/
public class Start {

	public static StringBuffer buffer = new StringBuffer();
	public static void main(String[] args){
		buffer.append("Class-Path:");
		String currentDir = new File("").getAbsolutePath();
		File lib = new File(currentDir + File.separator + "lib");
		getLibs(lib, " lib/");
		File selenium = new File(currentDir + File.separator + "lib" + File.separator + "selenium");
		getLibs(selenium, " lib/selenium/");
		
		File mf = createManifest(currentDir);
		changeManifest();
		if(mf.exists()){
			mf.delete();
		}
		startGUN();
	}
	
	public static void getLibs(File file, String prefix){
		File[] files = file.listFiles();
		for(File temp : files){
			if(temp.isFile() && temp.getName().endsWith(".jar")){
				buffer.append(prefix + temp.getName());
			}
		}
	}
	
	public static File createManifest(String currentDir){
		BufferedWriter writer = null;
		File file = new File(currentDir + File.separator + "lib" + File.separator + "MANIFEST.MF");
		try{
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("Manifest-Version: 1.0");
			writer.newLine();
			writer.write("Main-Class: king.selenium.view.Start");
			writer.newLine();
			writer.write(buffer.substring(0,70));
			writer.newLine();
			int len = buffer.length();
			int count = (len-70)/69;
			for(int i=0; i<count; ++i){
				writer.write(" " + buffer.substring(70+i*69, 70+(i+1)*69));
				writer.newLine();
			}
			int last = (len-70)%69;
			if(last > 0){
				writer.write(" " + buffer.substring(len-last));
				writer.newLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	public static void changeManifest(){
		Process ps = null;
		try {
			ps = Runtime.getRuntime().exec("jar umf lib/MANIFEST.MF SeleniumGUI.jar");
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(ps != null){
				ps.destroy();
			}
		}
	}
	
	public static void startGUN(){
		Process ps = null;
		try {
			ps = Runtime.getRuntime().exec("java -jar SeleniumGUI.jar");
			ps.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
