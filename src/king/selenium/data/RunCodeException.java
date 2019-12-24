package king.selenium.data;
/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月3日 下午4:05:59
* @ClassName ...
* @Description 用户定义的方法里面抛出的异常，会使用这个进行封装，后期可以拓展：脚本运行出现错误时，是否继续运行脚本
*/
public class RunCodeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RunCodeException(){}
	
	public RunCodeException(String message){
		super(message);
	}

}
