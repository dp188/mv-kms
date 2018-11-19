package com.zhixin.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;


/**
 * 
 * @ClassName: HttpUtil 
 * @Description: TODO 
 * @author 573196010@qq.com
 * @date 2015年8月19日 下午7:31:29 
 *
 */
public class HttpUtil {

	/**
	 * 获取真实的http请求方法
	 * @param request
	 * @return
	 */
	public static String getHttpMethod(HttpServletRequest request) {
		if (!request.getMethod().equalsIgnoreCase("POST")) {
			return request.getMethod();
		}else{
			String method = request.getHeader("X-HTTP-Method-Override");
			if(StringUtils.isEmpty(method)){
				return request.getMethod();
			}else{
				return method.toUpperCase();
			}
		}
	}
}
