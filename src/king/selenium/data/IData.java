package king.selenium.data;

import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年9月3日 下午3:13:31
* @ClassName ...
* @Description 数据的接口，测试计划数据、测试模块数据、测试用例数据、测试步骤数据，都实现了该接口
*/
public interface IData {

	void setName(String name);
	String getName();
	void setStatus(boolean status);
	boolean getStatus();
	List<IData> getDatas();
	IData getData(int index);
	void addData(IData data);
	void addData(int index, IData data);
	int indexOf(IData data);
	void delData(int index);
	void delData(IData data);
	void setParent(IData data);
	IData getParent();
	boolean isLastData();
}
