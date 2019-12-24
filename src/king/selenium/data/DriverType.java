package king.selenium.data;
/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��3�� ����1:49:39
* @ClassName ...
* @Description ��������Ͷ��壬��Ҫ����µ��������ֻ��Ҫ����µ�ö��ʵ���Ϳ�����
*/
public enum DriverType {

	CHROME("�ȸ������", "webdriver.chrome.driver", "chromedriver.exe", "org.openqa.selenium.chrome.ChromeDriver"),
	FIREFOX("��������", "webdriver.gecko.driver", "geckodriver.exe", "org.openqa.selenium.firefox.FirefoxDriver"),
	IE("IE�����", "webdriver.ie.driver", "IEDriverServer.exe", "org.openqa.selenium.ie.InternetExplorerDriver");
	
	private String browserName;//����������ƣ�����ʾ�ڲ��Լƻ�����ҳ���������������ѡ�����
	private String propertyName;//���������ʱ������ϵͳ���õ�keyֵ
	private String driverName;//�������ļ����ļ���
	private String className;//selenium�������������ʹ�õ������ȫ��
	
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
		throw new RuntimeException("û�����ø�������������Ϣ��");
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
