package king.selenium.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��10��11�� ����4:51:00
* @ClassName ...
* @Description ...
*/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ParameterName {
	String value();
}
