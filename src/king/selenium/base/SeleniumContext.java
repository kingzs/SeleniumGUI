package king.selenium.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月27日 下午2:35:16
* @ClassName ...
* @Description ...
*/
public abstract class SeleniumContext {

	protected static WebDriver driver;
	protected static WebElement element;
	
	public void setDriver(WebDriver drive){
		driver = drive;
	}
	
	public void setElement(WebElement elem){
		element = elem;
	}
	
}
