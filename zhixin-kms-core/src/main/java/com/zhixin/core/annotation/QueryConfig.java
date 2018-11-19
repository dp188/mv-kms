package com.zhixin.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.zhixin.core.enums.QueryOperatorEnum;
/**
 * 
* @ClassName: QueryConfig 
* @Description: 查询条件注解配置工具
* @author zhangtiebin@bwcmall.com
* @date 2015年7月1日 下午10:51:07 
*
 */
@Documented//元注解,注解表明这个注解应该被 javadoc工具记录
@Retention(RetentionPolicy.RUNTIME) //元注解（用来修饰注解的注解）RetentionPolicy.RUNTIME 运行时可读取，RetentionPolicy.CLASS 编译后能读取，RetentionPolicy.SOURCE 源文件能读取
@Target(ElementType.FIELD)//元注解，说明该注解应用到域上
public @interface QueryConfig {
	/**
	 * 支持的主键类型(默认支持所有类型)
	 * @return
	 */
	public QueryOperatorEnum[] supportOps() default {QueryOperatorEnum.all};
	
	/**
	 * 数据库字段名称（默认会使用当前field）
	 * @return
	 */
	public String colname(); 
	
	/**
	 * 是否支持排序
	 * @return
	 */
	public boolean supportOrder() default false;
	
	/**
	 * 是否支持分组
	 * @return
	 */
	public boolean supportGroupBy() default false;
	
}
