package com.zhixin.kms.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

 
public class UnauhtorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		//简单粗暴的直接跳转到login.jsp
		  response.sendRedirect(request.getContextPath()+"/login.html");
	}

}