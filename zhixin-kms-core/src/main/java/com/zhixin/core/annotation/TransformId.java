package com.zhixin.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
* @ClassName: TransformId
* @Description: 将产品ID、location地址ID等转换为相应的名称字段
*
 */

@Documented
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
public @interface TransformId {
	
	/**
	 * 转换后的目标字段名
	 * @return
	 */
	public String target(); 
	
	/**
	 * 转换所使用的类
	 * @return
	 */
	public String transformByService();
	
	/**
	 * 转换所使用的类
	 * @return
	 */
	public String transformByfield() default "name";
	
}
