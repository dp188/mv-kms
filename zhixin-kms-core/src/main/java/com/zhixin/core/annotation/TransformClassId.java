package com.zhixin.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
* @ClassName: TransformClassId
* @Description: 为了降低在abstracService（90行）中循环遍历field查询注解带来的性能消耗，引入类注解，必须同时包含该类注解+字段注解才会进行ID->name的转换
*
 */

@Documented
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface TransformClassId {

}
