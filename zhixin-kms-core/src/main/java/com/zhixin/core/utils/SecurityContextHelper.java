package com.zhixin.core.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;
/**
 * 
* @ClassName: SecurityContextHelper 
* @Description: SecurityContext工具类
* @author zhangtiebin@bwcmall.com
* @date 2015年6月23日 下午6:41:47 
*
 */
public class SecurityContextHelper {

	/**
	 * 用户是否登录
	 * @return
	 */
	public static boolean isLogin(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.isAuthenticated(); 
	}
 
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	public static User getCurrentUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null){
			User detail = null;
			try {
				detail = (User)authentication.getDetails();
			} catch (Exception e) {
				 throw new ApiException(ErrorCodeEnum.SESSIONISEXPIRED.getCode(), "Session已经过期，请重新登录！");
			}
			return detail;
		}
		return null;
	}
	/**
	 * 获取当前登录用户的所有角色
	 * @return
	 */
	public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities;
	}
	/**
	 * 注销
	 */
	public static void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	/**
	 * 构造用户信息map
	 * @param user
	 * @return
	 */
	public static Map<String,Object> buildUserInfoMap(){
		User user  = getCurrentUser();
		return buildUserInfoMap(user);
	}
	/**
	 * 构造用户信息map
	 * @param user
	 * @return
	 */
	public static Map<String,Object> buildUserInfoMap(User user){
		Map<String,Object> userInfo = new HashMap<String,Object>();
		
//		userInfo.put("user_id", user.getStaffId());
//		userInfo.put("user_name", user.getUsername());
//		userInfo.put("user_realname", user.getUserRealname());
//		userInfo.put("area_id", user.getAreaId());
//		userInfo.put("area_name", user.getAreaName());
//		
//		userInfo.put("province_id", user.getProvinceId());
//		userInfo.put("city_id", user.getCityId());
//		userInfo.put("county_id", user.getCountyId());
//		userInfo.put("location_id", user.getLocationId());
//		userInfo.put("province_name", user.getProvinceName());
//		userInfo.put("city_name", user.getCityName());
//		userInfo.put("county_name", user.getCountyName());
//		userInfo.put("location_name", user.getLocationName());
//		 
//		if(user.getOrg() != null){
//			OrgEntity org = user.getOrg();
//			userInfo.put("org_id", org.getOrgId());
//			userInfo.put("org_name", org.getOrgName());
//			userInfo.put("org_code", org.getOrgCode()); 
//		}
		return userInfo;
	}
}
