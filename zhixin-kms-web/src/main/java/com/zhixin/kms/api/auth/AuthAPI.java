package com.zhixin.kms.api.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zhixin.dto.user.UserDTO;

@Controller
@RequestMapping("/api/auth")
public class AuthAPI {
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	
	PropertyPlaceholderConfigurer p = null;

	/**
	 * 登录认证
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response,@RequestBody UserDTO user) {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user.getUserName(), user.getPassword());
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return JSON.toJSONString(authentication);
	}
	/**
	 * 注销
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public void auth(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		if(authentication.isAuthenticated()){
			request.getSession().invalidate(); 
		}
	}
}
