package com.zhixin.mall.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
 
/**
 * 容许跨域请求
 * @author Administrator
 *
 */
public class CORSFilter implements Filter {
  
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	 
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	   HttpServletResponse res = (HttpServletResponse) response;
	   res.setHeader("Access-Control-Allow-Origin", "*");
	   res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH"); 
	   res.setHeader("Access-Control-Allow-Headers", "Authorization,Token,token,x-requested-with,X-HTTP-Method-Override,origin, content-type, accept, SourceUrl, src_url_base, X_Requested_With");
	   chain.doFilter(request, res);
	}

	@Override
	public void destroy() {
		
	}
	 
}
