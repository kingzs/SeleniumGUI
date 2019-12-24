package king.selenium.init;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import king.selenium.base.*;
/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月27日 下午6:32:19
* @ClassName ...
* @Description ...
*/
public class Core extends SeleniumContext {
	
	@Operation(value="无", index=0)
	public void noOperation(){
		
	}
	
	@Operation("等待")
	public void waitTimes(@ParameterName("时间（秒）")String time) throws InterruptedException{
		time = time.trim();
		int timeMillis = Integer.parseInt(time)*1000;
		Thread.sleep(timeMillis);
	}

	/**
	 * 断言页面是否有出现文本
	 * @param text
	 */
	@Operation("断言页面出现文本")
	public void assertPageText(@ParameterName("文本")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入断言的文本，不能进行断言！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return;
			}
		}
		throw new NoSuchElementException("页面内没有找到 " + text);
	}
	
	/**
	 * 断言元素内是否有文本
	 * @param elem
	 * @param text
	 */
	@Operation("断言元素内出现文本")
	public void assertElementText(@ParameterName("文本")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入断言的文本，不能进行断言！");
		}
		List<WebElement> elems = element.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return;
			}
		}
		throw new NoSuchElementException("元素内没有找到 " + text);
	}
	
	/**
	 * 等待页面出现文本
	 * @param 要等待出现的文本
	 * @param 超时时间，单位：秒
	 */
	@Operation("等待页面出现文本")
	public void waitForText(@ParameterName("文本")String text, @ParameterName("超时时间（秒）")String timeText) throws InterruptedException {
		text = text.trim();
		timeText = timeText.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入断言的文本，不能进行断言！");
		}
		int time = 1;
		if(timeText.length() != 0){
			time = Integer.parseInt(timeText)*2;
		}
		while(time > 0){
			try{
				List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
				for(WebElement elem : elems){
					if(elem.isDisplayed()){
						return;
					}
				}
				Thread.sleep(500);
				--time;
			}catch(NoSuchElementException e){
				Thread.sleep(500);
				--time;
				continue;
			}
		}
		throw new NoSuchElementException("没有等到文本 " + text + " 出现");
	}
	
	@MethodMapping(value="无", index=0)
	public WebElement noMapping(){
		return null;
	}
	
	/**
	 * 通过xpath查找页面元素
	 * @param xpath
	 * @return
	 */
	@MethodMapping("通过xpath查找元素")
	public WebElement findElementByXpath(@ParameterName("xpath表达式")String xpath){
		xpath = xpath.trim();
		if(xpath.length() == 0){
			throw new RuntimeException("没有输入xpath表达式，不能查找元素！");
		}
		return driver.findElement(By.xpath(xpath));
	}
	
	/**
	 * 通过页面文本查找元素
	 * @param text
	 * @return
	 */
	@MethodMapping("通过文本查找元素")
	public WebElement findElementByText(@ParameterName("文本")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入参数，不能进行查找！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='"+text+"'][1]"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("页面内没有文本 " + text);
	}
	
	@MethodMapping("通过文本查找元素（忽略空格）")
	public WebElement findElementByTextIngoreSpace(@ParameterName("文本")String text){
		text = text.replaceAll("\\s", "");
		if(text.length() == 0){
			throw new RuntimeException("没有输入参数，不能进行查找。");
		}
		String first = text.substring(0, 1);
		List<WebElement> elems = driver.findElements(By.xpath("//*[starts-with(normalize-space(text()), '" + first + "')]"));
		for(WebElement elem : elems){
			if(elem.getText().replaceAll("\\s", "").equals(text) && elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("页面内没有文本 " + text);
	}
	
	/**
	 * 通过页面文本查找元素，当有多个元素匹配时，通过序号确定查找的元素
	 * @param text
	 * @param index
	 * @return
	 */
	@MethodMapping("通过文本+序号查找元素")
	public WebElement findElementByText(@ParameterName("文本")String text, @ParameterName("序号（从1开始）")String indexText){
		text = text.trim();
		indexText = indexText.trim();
		int index = Integer.parseInt(indexText);
		if(text.length() == 0 || index < 1){
			throw new RuntimeException("参数输入有误，不能查找元素！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='"+text+"']"));
		int currentIndex = 0;
		for(WebElement elem : elems){
			if(elem.isDisplayed() && ++currentIndex == index){
				return elem;
			}
		}
		throw new NoSuchElementException("页面内找不到 " + index + " 个文本 " + text);
	}
	
	/**
	 * 通过表单元素的value值查找元素
	 * @param value
	 * @return
	 */
	@MethodMapping("通过value属性查找元素")
	public WebElement findElementByValue(@ParameterName("value值")String value){
		value = value.trim();
		if(value.length() == 0){
			throw new RuntimeException("没有输入参数，不能查找元素！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[@value='"+value+"']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("页面上没有找到value为 " + value + " 的元素");
	}
	
	@MethodMapping("通过value属性查找元素（忽略空格）")
	public WebElement findElementByValueIngoreSpace(@ParameterName("value值")String value){
		value = value.replaceAll("\\s", "");
		if(value.length() == 0){
			throw new RuntimeException("没有输入参数，不能查找元素！");
		}
		String first = value.substring(0, 1);
		List<WebElement> elems = driver.findElements(By.xpath("//*[starts-with(normalize-space(@value), '" + first + "')]"));
		for(WebElement elem : elems){
			if(elem.getText().replaceAll("\\s", "").equals(value) && elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("页面上没有找到value为 " + value + " 的元素");
	}
	
	/**
	 * 通过表单元素的value值查找元素，有多个匹配时，通过序号确定
	 * @param value
	 * @param index
	 * @return
	 */
	@MethodMapping("通过value属性+序号查找元素")
	public WebElement findElementByValue(@ParameterName("value值")String value, @ParameterName("序号（从1开始）")String indexText){
		value = value.trim();
		indexText = indexText.trim();
		int index = Integer.parseInt(indexText);
		if(value.length() == 0 || index < 1){
			throw new RuntimeException("输入的参数有误，不能查找元素！");
		}
		int currentIndex = 0;
		List<WebElement> elems = driver.findElements(By.xpath("//*[@value='"+value+"'][" + index + "]"));
		for(WebElement elem : elems){
			if(elem.isDisplayed() && ++currentIndex == index){
				return elem;
			}
		}
		throw new NoSuchElementException("页面内找不到 " + index + " 个value为 " + value + " 的元素");
	}
	
	/**
	 * 查找某个文本后的第一个input(可以是输入框、单选按钮、复选按钮、时间组件、进度条等)
	 * @param text
	 * @return
	 */
	@MethodMapping("文本后查找input")
	public WebElement findInputAfterText(@ParameterName("文本")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入参数，不能查找元素！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				List<WebElement> inputs = elem.findElements(By.xpath("./following::input"));
				for(WebElement input : inputs){
					if(input.isDisplayed()){
						return input;
					}
				}
			}
		}
		throw new NoSuchElementException("在 " + text + " 后查找input元素，找不到");
	}
	
	/**
	 * 查找文本后面，第一个文本内容符合要求的元素
	 * @param 参照的文本
	 * @param 要查找元素的文本
	 * @return	匹配到的元素
	 */
	@MethodMapping("文本后查找文本元素")
	public WebElement findElementAfterText(@ParameterName("参照的文本")String text, @ParameterName("要找的文本")String curText){
		text = text.trim();
		curText = curText.trim();
		if(text.length() == 0 || curText.length() == 0){
			throw new RuntimeException("输入参数有误，不能进行查找！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				List<WebElement> results = elem.findElements(By.xpath("./following::*[normalize-space(text())='" + curText + "']"));
				for(WebElement result : results){
					if(result.isDisplayed()){
						return result;
					}
				}
			}
		}
		throw new NoSuchElementException("在 " + text + " 后面查找文本 " + curText + " 失败");
	}
	
	/**
	 * 查找某个文本之前的input标签
	 * @param text
	 * @return
	 */
	@MethodMapping("文本之前找input")
	public WebElement findInputBeforeText(@ParameterName("文本")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("没有输入参数，不能查找元素！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem.findElement(By.xpath("./preceding::input[1]"));
			}
		}
		throw new NoSuchElementException("在 " + text + " 之前找input元素失败");
	}
	
	/**
	 * 查找文本前面，第一个文本符合要求的元素
	 * @param 参照的文本
	 * @param 要找的文本
	 * @return
	 */
	@MethodMapping("文本之前查找文本元素")
	public WebElement findElementBeforeText(@ParameterName("参照的文本")String text, @ParameterName("要找的文本")String curText){
		text = text.trim();
		curText = curText.trim();
		if(text.length() == 0 || curText.length() == 0){
			throw new RuntimeException("输入的参数有误，不能查找元素！");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				List<WebElement> results = elem.findElements(By.xpath("./preceding::*[normalize-space(text())='" + curText + "']"));
				for(int i=results.size()-1; i>=0; --i){
					if(results.get(i).isDisplayed()){
						return results.get(i);
					}
				}
			}
		}
		throw new NoSuchElementException("在 " + text + " 之前找文本 " + curText + " 失败");
	}
	
	/**
	 * 通过输入框的占位符查找输入框
	 * @param placeholder
	 * @return
	 */
	@MethodMapping("通过占位符查找输入框")
	public WebElement findInputByPlaceHolder(@ParameterName("占位符文本")String placeholder){
		placeholder = placeholder.trim();
		if(placeholder.length() == 0){
			throw new RuntimeException("没有输入参数，不能查询元素！");
		}
		List<WebElement> inputs =  driver.findElements(By.xpath("//input[@placeholder='"+placeholder+"']"));
		for(WebElement input : inputs){
			if(input.isDisplayed()){
				return input;
			}
		}
		throw new NoSuchElementException("没有找到占位符为 " + placeholder + " 的输入框");
	}
	
	@FollowAction(value="无", index=0)
	public WebElement noFollow(){
		return null;
	}
	
	/**
	 * 获取元素的父元素
	 * @param elem
	 * @return
	 */
	@FollowAction("获取父元素")
	public WebElement getParent(){
		return element.findElement(By.xpath(".."));
	}
	
	/**
	 * 获取元素的上一个兄弟节点
	 * @param elem
	 * @return
	 */
	@FollowAction("获取上一个兄弟节点")
	public WebElement getBeforeElement(){
		return element.findElement(By.xpath("./preceding-sibling::*[1]"));
	}
	
	/**
	 * 获取元素的下一个兄弟节点
	 * @param elem
	 * @return
	 */
	@FollowAction("获取下一个兄弟节点")
	public WebElement getAfterElement(){
		return element.findElement(By.xpath("./following-sibling::*[1]"));
	}
	
	@FollowAction("元素后面查找输入框")
	public WebElement getFollowInput(){
		List<WebElement> inputs = element.findElements(By.xpath("./following::input"));
		for(WebElement input : inputs){
			if(input.isDisplayed()){
				return input;
			}
		}
		throw new NoSuchElementException("元素后没有找到输入框！");
	}
	
	/**
	 * 获取alert弹窗的内容，并关闭alert弹窗
	 * @return
	 */
	@Operation("断言弹框内容并关闭弹框")
	public void closeAlertAndGetText(@ParameterName("弹框内容")String text){
		Alert alert = driver.switchTo().alert();
		String result = alert.getText();
		alert.accept();
		if(!text.equals(result)){
			throw new NoSuchElementException("提示语不符");
		}
	}
	
	@Operation("点击")
	public void click(){
		element.click();
	}
	
	@Operation("输入值")
	public void sendKeys(@ParameterName("值")String text){
		element.sendKeys(text);
	}
	
	@Operation("清空内容")
	public void clear(){
		element.clear();
	}
	
	@Operation("获取文本值")
	public String getText(){
		return element.getText();
	}
	
	@Operation("获取属性值")
	public String getAttribute(@ParameterName("属性名")String attr){
		return element.getAttribute(attr);
	}
	
	@Operation("打开网页")
	public void openPage(@ParameterName("网址")String pageAddr){
		pageAddr = pageAddr.trim();
		driver.get(pageAddr);
	}
	
}
