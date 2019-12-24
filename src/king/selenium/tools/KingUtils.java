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
* @time 2019��10��29�� ����9:40:17
* @ClassName ...
* @Description ������
*/
public class KingUtils {
	
	public static final char[] NUMS = "0123456789".toCharArray();
	public static final char[] HEXS = "abcdef".toCharArray();
	public static final char[] OTHERS = "ghijklmnopqrstuvwxyz".toCharArray();

	//���Ҷ����ڶ��������е��±�
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
	
	//�����ַ����ַ������е��±�
	public static int indexOf(char[] cs, char c){
		for(int i=0,len=cs.length; i<len; ++i){
			if(cs[i] == c){
				return i;
			}
		}
		return -1;
	}
	
	//�ƶ����������е�Ԫ��
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
	
	//ʹ�����ַ����ָ��ַ���
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
	
	//�����û�����Ĳ�����������${}Χ�����ı����滻�ɶ�Ӧ��ֵ
	public static Object[] parameterHandle(Map<String, String> map, List<String> paramList){
		String[] result = new String[paramList.size()];
		for(int i=0,len=paramList.size(); i<len; ++i){
			result[i] = replaceVariable(map, paramList.get(i));
		}
		return result;
	}
	
	//�����жϣ��ԡ�#����β�����������
	public static String parameterAdd(String parameter){
		if(!parameter.endsWith("#")){
			return parameter;
		}
		return addString(parameter.substring(0, parameter.length()-1)) + "#";
	}
	//�ַ��������ķ�����ֻ�ܶ����ֺ���ĸ��������
	public static String addString(String param){
		int index = param.length();//��ǰ������ַ���λ��
		int charIndex = 0;//��ǰ������ַ����ַ������е�λ��
		boolean flag = true;//�Ƿ���Ҫ��λ
		boolean isUpper = false;//�Ƿ��Ǵ�д
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
	//�ж��ַ��ǲ��Ǵ�д
	public static boolean upper(char c){
		return c > 64 && c < 91;
	}
	//�����ļ�·�������Ծ���·�������·����Ĭ��·��
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
	 * ���ַ�������${}Χ�������ַ�����Ϊ������map��ȡֵ���滻��${}�����������
	 * ���map��û�д��Ӧ��ֵ����${}Χ�����Ĳ��֣�ԭ�����
	 * @param ������ֵ��ŵĵط�
	 * @param Ҫ������ַ���
	 * @param Ҫ����Ŀ�ʼλ��
	 * @return	�������ַ���
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
		//�����һ����}��֮ǰ���С�${���Ŀ�ʼλ�ã�����У���ǰ��
		//��������ַ����С�${���ȡ�}���������࣬����Ҫǰ��
		List<Integer> starts = new ArrayList<>();
		starts.add(start);
		int middleIndex = var.indexOf("${", start+2);
		while(middleIndex != -1 && middleIndex < end){
			starts.add(middleIndex);
			middleIndex = var.indexOf("${", middleIndex+2);
		}
		//�ҳ���һ����}��֮ǰ�����С�${������Ҫ�ں�������֮ƥ��ġ�}��������
		int needEndCount = starts.size() - 1;
		//���ݵĽ���λ�ã��ڵ�һ����}��֮���ҡ�}�����ҵ��Ĳ�һ���Ǻ�starts������±괦�ġ�${��ƥ��ġ�
		//��Ϊ�ڵ�һ����}��֮���ҡ�}����������֮�䣬���ܻ����С�${������������£������}������������
		//��${��������ʱ������Ҫ����
		int backEnd = end;
		//�ڡ�}��֮���ҡ�}�����ҵ�������֮�䡰${��������
		int backCount = 0;
		//�ڡ�}��֮����ҡ�}����λ�ñ���
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
	//���ַ����п�ʼλ�úͽ���λ��֮��������ַ���������
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
