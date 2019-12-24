package king.selenium.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年10月29日 上午9:40:17
* @ClassName ...
* @Description 工具类
*/
public class KingUtils {
	
	public static final char[] NUMS = "0123456789".toCharArray();
	public static final char[] HEXS = "abcdef".toCharArray();
	public static final char[] OTHERS = "ghijklmnopqrstuvwxyz".toCharArray();

	//查找对象在对象数组中的下标
	public static int indexOf(Object[] objs, Object obj){
		if(obj == null){
			return -1;
		}
		for(int i=0,len=objs.length; i<len; ++i){
			if(obj.equals(objs[i])){
				return i;
			}
		}
		return -1;
	}
	
	//查找字符在字符数组中的下标
	public static int indexOf(char[] cs, char c){
		for(int i=0,len=cs.length; i<len; ++i){
			if(cs[i] == c){
				return i;
			}
		}
		return -1;
	}
	
	//移动对象数组中的元素
	public static void moveArrayElement(Object[] objs, int from, int to){
		Object moveObj = objs[from];
		if(from > to){
			for(int i=from; i>to; --i){
				objs[i] = objs[i-1];
			}
		}else{
			for(int i=from; i<to; ++i){
				objs[i] = objs[i+1];
			}
		}
		objs[to] = moveObj;
	}
	
	//使用子字符串分割字符串
	public static List<String> splitString(String original, String separator){
		List<String> list = new ArrayList<>();
		int len = separator.length();
		int start = 0;
		int index = 0;
		while(true){
			index = original.indexOf(separator, start);
			String value = null;
			if(index < 0){
				value = original.substring(start).trim();
				if(value.endsWith("#")) value = value.substring(0, value.length()-1);
				list.add(value);
				break;
			}else{
				value = original.substring(start, index).trim();
				if(value.endsWith("#")) value = value.substring(0, value.length()-1);
				list.add(value);
			}
			start = index + len;
		}
		return list;
	}
	
	//处理用户输入的参数，把里面${}围起来的变量替换成对应的值
	public static Object[] parameterHandle(Map<String, String> map, List<String> paramList){
		String[] result = new String[paramList.size()];
		for(int i=0,len=paramList.size(); i<len; ++i){
			result[i] = replaceVariable(map, paramList.get(i));
		}
		return result;
	}
	
	//参数判断，以“#”结尾，则进行自增
	public static String parameterAdd(String parameter){
		if(!parameter.endsWith("#")){
			return parameter;
		}
		return addString(parameter.substring(0, parameter.length()-1)) + "#";
	}
	//字符串自增的方法，只能对数字和字母进行自增
	public static String addString(String param){
		int index = param.length();//当前处理的字符的位置
		int charIndex = 0;//当前处理的字符在字符数组中的位置
		boolean flag = true;//是否需要进位
		boolean isUpper = false;//是否是大写
		StringBuffer buffer = new StringBuffer();
		while(flag){
			if(--index < 0){
				index = 0;
				break;
			} 
			char c = param.charAt(index);
			if(upper(c)){
				c = (char) (c + 32);
				isUpper = true;
			}
			if((charIndex = indexOf(NUMS, c)) != -1){
				if(charIndex == 9){
					buffer.insert(0, NUMS[0]);
				}else{
					buffer.insert(0, NUMS[charIndex+1]);
					flag = false;
				}
			}else if((charIndex = indexOf(HEXS, c)) != -1){
				if(charIndex == 5){
					buffer.insert(0, isUpper?(char)(HEXS[0]-32):HEXS[0]);
				}else{
					buffer.insert(0, isUpper?(char)(HEXS[charIndex+1]-32):HEXS[charIndex+1]);
					flag = false;
				}
			}else if((charIndex = indexOf(OTHERS, c)) != -1){
				if(charIndex == 19){
					buffer.insert(0, isUpper?(char)(OTHERS[0]-32):OTHERS[0]);
				}else{
					buffer.insert(0, isUpper?(char)(OTHERS[charIndex+1]-32):OTHERS[charIndex+1]);
					flag = false;
				}
			}else{
				buffer.insert(0, c);
			}
			isUpper = false;
		}
		
		return param.substring(0, index) + buffer.toString();
	}
	//判断字符是不是大写
	public static boolean upper(char c){
		return c > 64 && c < 91;
	}
	//解析文件路径，可以绝对路径、相对路径、默认路径
	public static File analysisFileName(String fileName){
		File file = null;
		if(!fileName.contains("/") && !fileName.contains("\\")){
			file = new File("./parameter/" + fileName);
			if(file.exists()){
				return file;
			}else{
				return new File(fileName);
			}
		}
		if(fileName.startsWith("./") || fileName.startsWith("../")){
			return new File(fileName);
		}
		if(fileName.startsWith("/")){
			file = new File("." + fileName);
			if(file.exists()){
				return file;
			}else{
				return new File(fileName);
			}
		}
		if(fileName.contains("/")){
			return new File("./" + fileName);
		}
		return new File(fileName);
	}
	
	public static String replaceVariable(Map<String,String> map, String var) {
		return replaceVariable(map, var, 0);
	}
	/**
	 * 把字符串中用${}围起来的字符串作为键，到map中取值，替换掉${}及里面的内容
	 * 如果map中没有存对应的值，则${}围起来的部分，原样输出
	 * @param 变量与值存放的地方
	 * @param 要处理的字符串
	 * @param 要处理的开始位置
	 * @return	处理后的字符串
	 */
	public static String replaceVariable(Map<String,String> map, String var, int startIndex) {
		int start = var.indexOf("${", startIndex);
		if(start < 0){
			return var;
		}
		int end = var.indexOf("}", start+2);
		if(end < 0){
			return var;
		}
		//保存第一个“}”之前所有“${”的开始位置，用于校验和前溯
		//如果整个字符串中“${”比“}”的数量多，则需要前溯
		List<Integer> starts = new ArrayList<>();
		starts.add(start);
		int middleIndex = var.indexOf("${", start+2);
		while(middleIndex != -1 && middleIndex < end){
			starts.add(middleIndex);
			middleIndex = var.indexOf("${", middleIndex+2);
		}
		//找出第一个“}”之前的所有“${”，需要在后面找与之匹配的“}”的数量
		int needEndCount = starts.size() - 1;
		//回溯的结束位置，在第一个“}”之后找“}”，找到的不一定是和starts里面的下标处的“${”匹配的。
		//因为在第一个“}”之后找“}”，在两者之间，可能还会有“${”，这种情况下，如果“}”的数量少于
		//“${”的数量时，就需要回溯
		int backEnd = end;
		//在“}”之后找“}”，找到后，两者之间“${”的数量
		int backCount = 0;
		//在“}”之后查找“}”，位置保存
		int finalEnd = var.indexOf("}", end+1);
		while(needEndCount > 0 && finalEnd != -1){
			int temp =  findSubCount(var, end, finalEnd, "${");
			if(temp == 0){
				backCount = backCount == 0 ? 0 : backCount - 1;
				if(backCount == 0) backEnd = finalEnd;
			}else{
				needEndCount += temp;
				backCount += temp;
			}
			end = finalEnd;
			finalEnd = var.indexOf("}", end+1);
			--needEndCount;
		}
		
		if(needEndCount >= starts.size()){
			needEndCount = starts.size() - 1;
		}
		start = starts.get(needEndCount);
		String result = replaceVariable(map, var.substring(start+2, backEnd), 0);
		String value = map.get(result);
		String startStr = null;
		if(value == null){
			startStr = var.substring(0, start+2) + result;
			return replaceVariable(map, startStr + var.substring(backEnd), startStr.length());
		}else{
			startStr = var.substring(0, start) + value;
			return replaceVariable(map, startStr + var.substring(backEnd+1), startStr.length());	
		}
	}
	//在字符串中开始位置和结束位置之间查找子字符串的数量
	public static int findSubCount(String str, int start, int end, String sub){
		int count = 0;
		int len = sub.length();
		start = str.indexOf(sub, start);
		while(start != -1 && start < end){
			++count;
			start = str.indexOf(sub, start+len);
		}
		return count;
	}
	
	public static void main(String[] args) throws IOException{
		Map<String, String> map = new HashMap<>();
		map.put("td", "yyy");
		map.put("dd", "haha");
		map.put("chahacc", "wawa");

		System.out.println(replaceVariable(map, "wer${sc${td}---${c${dd}cc}sass}"));
		System.out.println(replaceVariable(map, "wer${sc${td}---${c${dd}ccsass"));
		System.out.println(replaceVariable(map, "wersc${td}---cdd}cc}sass"));
		
//		modifyFile("parameter/test.txt", ",", "GBK", 3);
		
	}
}
