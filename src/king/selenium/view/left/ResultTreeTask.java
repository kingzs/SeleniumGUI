package king.selenium.view.left;

import java.util.LinkedList;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年12月19日 上午10:42:56
* @ClassName ...
* @Description 结果树添加节点的处理对象
*/
public class ResultTreeTask implements Runnable {

	@Override
	public void run() {
		LinkedList<ResultNodeHandler> list = Domain.getResultNodeHandlers();
		while(!list.isEmpty()){
			ResultNodeHandler resultNodeHandler = list.removeFirst();
			resultNodeHandler.parent.add(resultNodeHandler.son);
		}
		Domain.getResultTree().updateUI();
	}

}
