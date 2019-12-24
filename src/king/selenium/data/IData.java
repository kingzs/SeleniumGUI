package king.selenium.data;

import java.util.List;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��9��3�� ����3:13:31
* @ClassName ...
* @Description ���ݵĽӿڣ����Լƻ����ݡ�����ģ�����ݡ������������ݡ����Բ������ݣ���ʵ���˸ýӿ�
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
