package king.selenium.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import king.selenium.base.FollowAction;
import king.selenium.base.MethodMapping;
import king.selenium.base.Operation;
import king.selenium.base.ParameterName;
import king.selenium.base.SeleniumContext;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月27日 下午2:40:07
* @ClassName ...
* @Description 自定义类加载器，将lib/ext目录下的jar加载进来，并解析保存
*/
public class KingClassLoader extends ClassLoader {

	private static Map<String, byte[]> classMap = new ConcurrentHashMap<>();//存放class文件的二进制数据
	private List<SeleniumContext> scs = new ArrayList<>();//所有自定义类的实例集合
	private List<String> keys = new ArrayList<>();//查找元素的方法名称集合，即MethodMapping注解的value集合
	private Map<String, SeleniumContext> keyEntryMap = new HashMap<>();//查找元素的方法名称与对象的映射，要调用这个方法，使用哪个对象调用
	private Map<String, Method> keyMethodMap = new HashMap<>();//查找元素的方法名称与方法的映射
	private Map<String, String[]> keyNameMap = new HashMap<>();//查找元素的方法名称与参数名称列表的映射
	private Map<String, Parameter[]> keyParametersMap = new HashMap<>();//查找元素的方法名称与参数列表的映射
	
	private List<String> follows = new ArrayList<>();
	private Map<String, SeleniumContext> followEntryMap = new HashMap<>();
	private Map<String, Method> followMethodMap = new HashMap<>();
	private Map<String, String[]> followNameMap = new HashMap<>();
	private Map<String, Parameter[]> followParameterMap = new HashMap<>();
	
	private List<String> operates = new ArrayList<>();
	private Map<String, SeleniumContext> operationEntryMap = new HashMap<>();
	private Map<String, Method> operationMethodMap = new HashMap<>();
	private Map<String, String[]> operationNameMap = new HashMap<>();
	private Map<String, Parameter[]> operationParameterMap = new HashMap<>();

	public KingClassLoader() {
		String path = new File("").getAbsolutePath();
		preReadJarFile(path+File.separator+"lib"+File.separator+"ext"+File.separator);
	}

	@Override
	public Class<?> findClass(String name) {
		try {
			byte[] result = getClass(name);
			if (result == null) {
				throw new ClassNotFoundException();
			} else {
				return defineClass(name, result, 0, result.length);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> findClass(String name, byte[] data){
		Class<?> clazz = null;
		try{
			clazz = defineClass(name, data, 0, data.length);
		}catch(java.lang.LinkageError e){
			e.printStackTrace();
		}
		return clazz;
	}

	private byte[] getClass(String className) {
		if (classMap.containsKey(className)) {
			return classMap.get(className);
		} else {
			return null;
		}
	}

	private void preReadJarFile(String classPath) {
		File[] files = new File(classPath).listFiles();
		if (files != null) {
			for (File file : files) {
				scanJarFile(file);
			}
		}
	}

	private void scanJarFile(File file) {
		
		if (file.isFile() && file.getName().endsWith(".jar")) {
			try {
				readJAR(new JarFile(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				scanJarFile(f);
			}
		}
		
	}

	private void readJAR(JarFile jar) throws IOException {
		Enumeration<JarEntry> en = jar.entries();
		while (en.hasMoreElements()) {
			JarEntry je = en.nextElement();
			String name = je.getName();
			if (name.endsWith(".class")) {
				String className = name.replace("/", ".").replace(".class", "");
				if(!classMap.containsKey(className)){
					InputStream input = null;
					ByteArrayOutputStream baos = null;
					try {
						input = jar.getInputStream(je);
						baos = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int bytesNumRead = -1;
						while ((bytesNumRead = input.read(buffer)) != -1) {
							baos.write(buffer, 0, bytesNumRead);
						}
						Class<?> clazz = findClass(className, baos.toByteArray());
						assClass(clazz);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (baos != null) {
							baos.close();
						}
						if (input != null) {
							input.close();
						}
					}
				}
			}
		}
	}

	private void assClass(Class<?> clazz) throws Exception {
		if(clazz == null){
			return;
		}
		if(SeleniumContext.class.isAssignableFrom(clazz)){
			Method[] methods = clazz.getMethods();
			SeleniumContext obj = (SeleniumContext) clazz.newInstance();
			scs.add(obj);
			for(Method method : methods){
				if(method.isAnnotationPresent(MethodMapping.class)){
					String key = method.getDeclaredAnnotation(MethodMapping.class).value();
					if("无".equals(key)){
						keys.add(0, key);
					}else{
						keys.add(key);
					}
					keyEntryMap.put(key, obj);
					keyMethodMap.put(key, method);
					Parameter[] keyParameters = method.getParameters();
					keyParametersMap.put(key, keyParameters);
					String[] keyNames = new String[keyParameters.length];
					for(int i=0,len=keyParameters.length; i<len; ++i){
						if(keyParameters[i].isAnnotationPresent(ParameterName.class)){
							keyNames[i] = keyParameters[i].getDeclaredAnnotation(ParameterName.class).value()+"：";
						}else{
							keyNames[i] = "参数"+i+"：";
						}
					}
					keyNameMap.put(key, keyNames);
				}
				if(method.isAnnotationPresent(FollowAction.class)){
					String follow = method.getDeclaredAnnotation(FollowAction.class).value();
					if("无".equals(follow)){
						follows.add(0, follow);
					}else{
						follows.add(follow);
					}
					followEntryMap.put(follow, obj);
					followMethodMap.put(follow, method);
					Parameter[] followParameters = method.getParameters();
					followParameterMap.put(follow, followParameters);
					String[] followNames = new String[followParameters.length];
					for(int i=0,len=followParameters.length; i<len; ++i){
						if(followParameters[i].isAnnotationPresent(ParameterName.class)){
							followNames[i] = followParameters[i].getDeclaredAnnotation(ParameterName.class).value()+"：";
						}else{
							followNames[i] = "参数"+i+"：";
						}
					}
					followNameMap.put(follow, followNames);
				}
				if(method.isAnnotationPresent(Operation.class)){
					String operate = method.getDeclaredAnnotation(Operation.class).value();
					if("无".equals(operate)){
						operates.add(0, operate);
					}else{
						operates.add(operate);
					}
					operationEntryMap.put(operate, obj);
					operationMethodMap.put(operate, method);
					Parameter[] operateParameters = method.getParameters();
					operationParameterMap.put(operate, operateParameters);
					String[] operationNames = new String[operateParameters.length];
					for(int i=0,len=operateParameters.length; i<len; ++i){
						if(operateParameters[i].isAnnotationPresent(ParameterName.class)){
							operationNames[i] = operateParameters[i].getDeclaredAnnotation(ParameterName.class).value()+"：";
						}else{
							operationNames[i] = "参数"+i+"：";
						}
					}
					operationNameMap.put(operate, operationNames);
				}
			}
		}
	}
	
	public List<SeleniumContext> getAllEntry(){
		return scs;
	}
	public String[] getKeys(){
		return keys.toArray(new String[]{});
	}
	public SeleniumContext getKeyEntry(String key){
		return keyEntryMap.get(key);
	}
	public Method getKeyMethod(String key){
		return keyMethodMap.get(key);
	}
	public String[] getKeyNames(String key){
		return keyNameMap.get(key);
	}
	public Parameter[] getKeyParameter(String key){
		return keyParametersMap.get(key);
	}
	public String[] getFollows(){
		return follows.toArray(new String[]{});
	}
	public SeleniumContext getFollowEntry(String key){
		return followEntryMap.get(key);
	}
	public Method getFollowMethod(String key){
		return followMethodMap.get(key);
	}
	public String[] getFollowNames(String key){
		return followNameMap.get(key);
	}
	public Parameter[] getFollowParameter(String key){
		return followParameterMap.get(key);
	}
	public String[] getOperates(){
		return operates.toArray(new String[]{});
	}
	public SeleniumContext getOperationEntry(String key){
		return operationEntryMap.get(key);
	}
	public Method getOperationMethod(String key){
		return operationMethodMap.get(key);
	}
	public String[] getOperationNames(String key){
		return operationNameMap.get(key);
	}
	public Parameter[] getOperationParameter(String key){
		return operationParameterMap.get(key);
	}
}
