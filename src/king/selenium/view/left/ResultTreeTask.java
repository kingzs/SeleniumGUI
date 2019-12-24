package king.selenium.view.left;

import java.util.LinkedList;

import king.selenium.view.Domain;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��12��19�� ����10:42:56
* @ClassName ...
* @Description �������ӽڵ�Ĵ������
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
