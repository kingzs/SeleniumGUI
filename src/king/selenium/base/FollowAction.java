package king.selenium.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019年8月28日 下午4:05:37
* @ClassName ...
* @Description ...
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FollowAction {
	String value();
	int index() default -1;
}
