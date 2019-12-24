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
* @time 2019��8��27�� ����6:32:19
* @ClassName ...
* @Description ...
*/
public class Core extends SeleniumContext {
	
	@Operation(value="��", index=0)
	public void noOperation(){
		
	}
	
	@Operation("�ȴ�")
	public void waitTimes(@ParameterName("ʱ�䣨�룩")String time) throws InterruptedException{
		time = time.trim();
		int timeMillis = Integer.parseInt(time)*1000;
		Thread.sleep(timeMillis);
	}

	/**
	 * ����ҳ���Ƿ��г����ı�
	 * @param text
	 */
	@Operation("����ҳ������ı�")
	public void assertPageText(@ParameterName("�ı�")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������Ե��ı������ܽ��ж��ԣ�");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return;
			}
		}
		throw new NoSuchElementException("ҳ����û���ҵ� " + text);
	}
	
	/**
	 * ����Ԫ�����Ƿ����ı�
	 * @param elem
	 * @param text
	 */
	@Operation("����Ԫ���ڳ����ı�")
	public void assertElementText(@ParameterName("�ı�")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������Ե��ı������ܽ��ж��ԣ�");
		}
		List<WebElement> elems = element.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return;
			}
		}
		throw new NoSuchElementException("Ԫ����û���ҵ� " + text);
	}
	
	/**
	 * �ȴ�ҳ������ı�
	 * @param Ҫ�ȴ����ֵ��ı�
	 * @param ��ʱʱ�䣬��λ����
	 */
	@Operation("�ȴ�ҳ������ı�")
	public void waitForText(@ParameterName("�ı�")String text, @ParameterName("��ʱʱ�䣨�룩")String timeText) throws InterruptedException {
		text = text.trim();
		timeText = timeText.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������Ե��ı������ܽ��ж��ԣ�");
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
		throw new NoSuchElementException("û�еȵ��ı� " + text + " ����");
	}
	
	@MethodMapping(value="��", index=0)
	public WebElement noMapping(){
		return null;
	}
	
	/**
	 * ͨ��xpath����ҳ��Ԫ��
	 * @param xpath
	 * @return
	 */
	@MethodMapping("ͨ��xpath����Ԫ��")
	public WebElement findElementByXpath(@ParameterName("xpath���ʽ")String xpath){
		xpath = xpath.trim();
		if(xpath.length() == 0){
			throw new RuntimeException("û������xpath���ʽ�����ܲ���Ԫ�أ�");
		}
		return driver.findElement(By.xpath(xpath));
	}
	
	/**
	 * ͨ��ҳ���ı�����Ԫ��
	 * @param text
	 * @return
	 */
	@MethodMapping("ͨ���ı�����Ԫ��")
	public WebElement findElementByText(@ParameterName("�ı�")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������������ܽ��в��ң�");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='"+text+"'][1]"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ����û���ı� " + text);
	}
	
	@MethodMapping("ͨ���ı�����Ԫ�أ����Կո�")
	public WebElement findElementByTextIngoreSpace(@ParameterName("�ı�")String text){
		text = text.replaceAll("\\s", "");
		if(text.length() == 0){
			throw new RuntimeException("û��������������ܽ��в��ҡ�");
		}
		String first = text.substring(0, 1);
		List<WebElement> elems = driver.findElements(By.xpath("//*[starts-with(normalize-space(text()), '" + first + "')]"));
		for(WebElement elem : elems){
			if(elem.getText().replaceAll("\\s", "").equals(text) && elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ����û���ı� " + text);
	}
	
	/**
	 * ͨ��ҳ���ı�����Ԫ�أ����ж��Ԫ��ƥ��ʱ��ͨ�����ȷ�����ҵ�Ԫ��
	 * @param text
	 * @param index
	 * @return
	 */
	@MethodMapping("ͨ���ı�+��Ų���Ԫ��")
	public WebElement findElementByText(@ParameterName("�ı�")String text, @ParameterName("��ţ���1��ʼ��")String indexText){
		text = text.trim();
		indexText = indexText.trim();
		int index = Integer.parseInt(indexText);
		if(text.length() == 0 || index < 1){
			throw new RuntimeException("�����������󣬲��ܲ���Ԫ�أ�");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='"+text+"']"));
		int currentIndex = 0;
		for(WebElement elem : elems){
			if(elem.isDisplayed() && ++currentIndex == index){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ�����Ҳ��� " + index + " ���ı� " + text);
	}
	
	/**
	 * ͨ����Ԫ�ص�valueֵ����Ԫ��
	 * @param value
	 * @return
	 */
	@MethodMapping("ͨ��value���Բ���Ԫ��")
	public WebElement findElementByValue(@ParameterName("valueֵ")String value){
		value = value.trim();
		if(value.length() == 0){
			throw new RuntimeException("û��������������ܲ���Ԫ�أ�");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[@value='"+value+"']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ����û���ҵ�valueΪ " + value + " ��Ԫ��");
	}
	
	@MethodMapping("ͨ��value���Բ���Ԫ�أ����Կո�")
	public WebElement findElementByValueIngoreSpace(@ParameterName("valueֵ")String value){
		value = value.replaceAll("\\s", "");
		if(value.length() == 0){
			throw new RuntimeException("û��������������ܲ���Ԫ�أ�");
		}
		String first = value.substring(0, 1);
		List<WebElement> elems = driver.findElements(By.xpath("//*[starts-with(normalize-space(@value), '" + first + "')]"));
		for(WebElement elem : elems){
			if(elem.getText().replaceAll("\\s", "").equals(value) && elem.isDisplayed()){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ����û���ҵ�valueΪ " + value + " ��Ԫ��");
	}
	
	/**
	 * ͨ����Ԫ�ص�valueֵ����Ԫ�أ��ж��ƥ��ʱ��ͨ�����ȷ��
	 * @param value
	 * @param index
	 * @return
	 */
	@MethodMapping("ͨ��value����+��Ų���Ԫ��")
	public WebElement findElementByValue(@ParameterName("valueֵ")String value, @ParameterName("��ţ���1��ʼ��")String indexText){
		value = value.trim();
		indexText = indexText.trim();
		int index = Integer.parseInt(indexText);
		if(value.length() == 0 || index < 1){
			throw new RuntimeException("����Ĳ������󣬲��ܲ���Ԫ�أ�");
		}
		int currentIndex = 0;
		List<WebElement> elems = driver.findElements(By.xpath("//*[@value='"+value+"'][" + index + "]"));
		for(WebElement elem : elems){
			if(elem.isDisplayed() && ++currentIndex == index){
				return elem;
			}
		}
		throw new NoSuchElementException("ҳ�����Ҳ��� " + index + " ��valueΪ " + value + " ��Ԫ��");
	}
	
	/**
	 * ����ĳ���ı���ĵ�һ��input(����������򡢵�ѡ��ť����ѡ��ť��ʱ���������������)
	 * @param text
	 * @return
	 */
	@MethodMapping("�ı������input")
	public WebElement findInputAfterText(@ParameterName("�ı�")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������������ܲ���Ԫ�أ�");
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
		throw new NoSuchElementException("�� " + text + " �����inputԪ�أ��Ҳ���");
	}
	
	/**
	 * �����ı����棬��һ���ı����ݷ���Ҫ���Ԫ��
	 * @param ���յ��ı�
	 * @param Ҫ����Ԫ�ص��ı�
	 * @return	ƥ�䵽��Ԫ��
	 */
	@MethodMapping("�ı�������ı�Ԫ��")
	public WebElement findElementAfterText(@ParameterName("���յ��ı�")String text, @ParameterName("Ҫ�ҵ��ı�")String curText){
		text = text.trim();
		curText = curText.trim();
		if(text.length() == 0 || curText.length() == 0){
			throw new RuntimeException("����������󣬲��ܽ��в��ң�");
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
		throw new NoSuchElementException("�� " + text + " ��������ı� " + curText + " ʧ��");
	}
	
	/**
	 * ����ĳ���ı�֮ǰ��input��ǩ
	 * @param text
	 * @return
	 */
	@MethodMapping("�ı�֮ǰ��input")
	public WebElement findInputBeforeText(@ParameterName("�ı�")String text){
		text = text.trim();
		if(text.length() == 0){
			throw new RuntimeException("û��������������ܲ���Ԫ�أ�");
		}
		List<WebElement> elems = driver.findElements(By.xpath("//*[normalize-space(text())='" + text + "']"));
		for(WebElement elem : elems){
			if(elem.isDisplayed()){
				return elem.findElement(By.xpath("./preceding::input[1]"));
			}
		}
		throw new NoSuchElementException("�� " + text + " ֮ǰ��inputԪ��ʧ��");
	}
	
	/**
	 * �����ı�ǰ�棬��һ���ı�����Ҫ���Ԫ��
	 * @param ���յ��ı�
	 * @param Ҫ�ҵ��ı�
	 * @return
	 */
	@MethodMapping("�ı�֮ǰ�����ı�Ԫ��")
	public WebElement findElementBeforeText(@ParameterName("���յ��ı�")String text, @ParameterName("Ҫ�ҵ��ı�")String curText){
		text = text.trim();
		curText = curText.trim();
		if(text.length() == 0 || curText.length() == 0){
			throw new RuntimeException("����Ĳ������󣬲��ܲ���Ԫ�أ�");
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
		throw new NoSuchElementException("�� " + text + " ֮ǰ���ı� " + curText + " ʧ��");
	}
	
	/**
	 * ͨ��������ռλ�����������
	 * @param placeholder
	 * @return
	 */
	@MethodMapping("ͨ��ռλ�����������")
	public WebElement findInputByPlaceHolder(@ParameterName("ռλ���ı�")String placeholder){
		placeholder = placeholder.trim();
		if(placeholder.length() == 0){
			throw new RuntimeException("û��������������ܲ�ѯԪ�أ�");
		}
		List<WebElement> inputs =  driver.findElements(By.xpath("//input[@placeholder='"+placeholder+"']"));
		for(WebElement input : inputs){
			if(input.isDisplayed()){
				return input;
			}
		}
		throw new NoSuchElementException("û���ҵ�ռλ��Ϊ " + placeholder + " �������");
	}
	
	@FollowAction(value="��", index=0)
	public WebElement noFollow(){
		return null;
	}
	
	/**
	 * ��ȡԪ�صĸ�Ԫ��
	 * @param elem
	 * @return
	 */
	@FollowAction("��ȡ��Ԫ��")
	public WebElement getParent(){
		return element.findElement(By.xpath(".."));
	}
	
	/**
	 * ��ȡԪ�ص���һ���ֵܽڵ�
	 * @param elem
	 * @return
	 */
	@FollowAction("��ȡ��һ���ֵܽڵ�")
	public WebElement getBeforeElement(){
		return element.findElement(By.xpath("./preceding-sibling::*[1]"));
	}
	
	/**
	 * ��ȡԪ�ص���һ���ֵܽڵ�
	 * @param elem
	 * @return
	 */
	@FollowAction("��ȡ��һ���ֵܽڵ�")
	public WebElement getAfterElement(){
		return element.findElement(By.xpath("./following-sibling::*[1]"));
	}
	
	@FollowAction("Ԫ�غ�����������")
	public WebElement getFollowInput(){
		List<WebElement> inputs = element.findElements(By.xpath("./following::input"));
		for(WebElement input : inputs){
			if(input.isDisplayed()){
				return input;
			}
		}
		throw new NoSuchElementException("Ԫ�غ�û���ҵ������");
	}
	
	/**
	 * ��ȡalert���������ݣ����ر�alert����
	 * @return
	 */
	@Operation("���Ե������ݲ��رյ���")
	public void closeAlertAndGetText(@ParameterName("��������")String text){
		Alert alert = driver.switchTo().alert();
		String result = alert.getText();
		alert.accept();
		if(!text.equals(result)){
			throw new NoSuchElementException("��ʾ�ﲻ��");
		}
	}
	
	@Operation("���")
	public void click(){
		element.click();
	}
	
	@Operation("����ֵ")
	public void sendKeys(@ParameterName("ֵ")String text){
		element.sendKeys(text);
	}
	
	@Operation("�������")
	public void clear(){
		element.clear();
	}
	
	@Operation("��ȡ�ı�ֵ")
	public String getText(){
		return element.getText();
	}
	
	@Operation("��ȡ����ֵ")
	public String getAttribute(@ParameterName("������")String attr){
		return element.getAttribute(attr);
	}
	
	@Operation("����ҳ")
	public void openPage(@ParameterName("��ַ")String pageAddr){
		pageAddr = pageAddr.trim();
		driver.get(pageAddr);
	}
	
}
