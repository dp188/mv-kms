package com.zhixin.kms.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhixin.dto.user.UserDTO;

@Controller
@RequestMapping("/")
public class AuthController {
	/**
	 * 首页
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String goIndex(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
		
		return "kms/common/index";
	}
	/**
	 * 登录页
	 */
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String goLogin(HttpServletRequest request, HttpServletResponse response, UserDTO user) {
		return "kms/common/login";
	}
}
