package king.selenium.data;
/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月3日 下午1:49:39
* @ClassName ...
* @Description 浏览器类型定义，需要添加新的浏览器，只需要添加新的枚举实例就可以了
*/
public enum DriverType {

	CHROME("谷歌浏览器", "webdriver.chrome.driver", "chromedriver.exe", "org.openqa.selenium.chrome.ChromeDriver"),
	FIREFOX("火狐浏览器", "webdriver.gecko.driver", "geckodriver.exe", "org.openqa.selenium.firefox.FirefoxDriver"),
	IE("IE浏览器", "webdriver.ie.driver", "IEDriverServer.exe", "org.openqa.selenium.ie.InternetExplorerDriver");
	
	private String browserName;//浏览器的名称，会显示在测试计划配置页面中浏览器的下拉选择框中
	private String propertyName;//启动浏览器时，设置系统配置的key值
	private String driverName;//驱动器文件的文件名
	private String className;//selenium中启动浏览器所使用的类的类全名
	
	private DriverType(String browserName, String propertyName, String driverName, String className){
		this.browserName = browserName;
		this.propertyName = propertyName;
		this.driverName = driverName;
		this.className = className;
	}
	
	public String getBrowserName(){
		return browserName;
	}
	
	public String getPropertyName(){
		return propertyName;
	}
	
	public String getDriverName(){
		return driverName;
	}
	
	public String getClassName(){
		return className;
	}
	
	public static DriverType getDriverType(String browserName){
		for(DriverType driverType : values()){
			if(driverType.getBrowserName().equals(browserName)){
				return driverType;
			}
		}
		throw new RuntimeException("没有配置该浏览器的相关信息！");
	}
	
	public static String[] getBrowserNames(){
		DriverType[] driverTypes = values();
		String[] browserNames = new String[driverTypes.length];
		for(int i=0,len=driverTypes.length; i<len; ++i){
			browserNames[i] = driverTypes[i].getBrowserName();
		}
		return browserNames;
	}
}
