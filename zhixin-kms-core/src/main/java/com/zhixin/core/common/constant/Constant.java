package com.zhixin.core.common.constant;

/**
 * 
* @ClassName: Constant 
* @Description: 系统常量
* @author zhangtiebin@bwcmall.com
* @date 2015年7月7日 下午2:18:45 
*
 */
public class Constant {
	
	public final static String OPERATE_SUCCESS =  "操作成功！";
	
	public final static String DOT = ".";
	 //用户权限缓存
    public static final String USER_PERMISSION_CACHE = "lup"+DOT; //超时时间为30分钟
    //权限缓存
    public static final String PERMISSION_CACHE = "lp"+DOT; //超时时间为7天
    //系统所有角色的权限缓存（角色英文名称为key，角色对应的权限LIST为value）
    public static final String ROLE_PERMISSION_CACHE = "lrp"+DOT; //超时时间为7天 
    //已经匹配过的权限缓存
    public static final String RES_MATCH_CACHE ="lrm"+DOT; //超时时间为30分钟
    //用户信息的cache
    public static final String USER_INFO_CACHE="su"+DOT; 
    //cookie与user的映射
    public static final String COOKIE_USER_CACHE="sct"+DOT;
    //token与真实use的映射
    public static final String TOEKN_USER_CACHE="stuc"+DOT;
    //token与真实use的映射
    public static final String USER_MENU_CACHE="um"+DOT;
    
    //SSO使用的cookie的name
    public final static String SSO_COOKIE_NAME= "uo.auth.sso.cookie";
    
	//一周
	public final static Integer SEC_WEEK = 7*24*60*60;//seconds
	//一天
	public final static Integer SEC_ONEDAY = 24*60*60;//seconds
	//一个小时
	public final static Integer SEC_ANHOUR = 60*60;//seconds
	//半个小时
	public final static Integer SEC_HALFHOUR = 30*60;//seconds
	//一分钟
	public final static Integer SEC_ONEMINUTE = 60;//seconds
	
	//重试次数
	public final static Integer RETRY_TIME = 5;
	//连接超时时间
	public final static Integer CONNECT_TIMEOUT = 2000;
	//访问超时时间
	public final static Integer READ_TIMEOUT = 3000;
	
	//redis nxxx的expire定义
	public final static String EX = "EX"; //seconds
	//redis nxxx的expire定义
	public final static String PX = "PX"; //milliseconds
	//详细地址字符个数上限 45个，对应cust_addr表的detail字段
	public final static Integer MAX_ADDR_DETAIL_LEN = 45;
	//访问uc结果错误码
	public final static Integer ERRNO_SUCCESS = 0;
	//访问uc结果错误信息
	public final static String ERRMSG_SUCCESS = "SUCCESS";
	//redis uc结果 '1'表示是注册用户 '0'表示未注册用户
	public final static String ERRMSG_TRUE = "1";
	public final static String ERRMSG_FALSE = "0";
	   
	public final static String DATA_ACCESS_RULE_CONFIG ="com.opensm.dataaccess._DATA_ACCESS_RULE_CONFIG"; 
	
	public final static String DATA_ACCESS_EXCEPTION ="com.opensm.dataaccess._DATA_ACCESS_EXCEPTION"; 
	
	public final static int HTTP_SUCCESS = 200;
	
	public final static int ZERO = 0;
	
	public final static String CUST_CELLPHONE_REPEAD_MESSAGE = "该手机号已被以下员工登记过</br>{0} {1} {2} {3}";
	
	//流程相关
	//默认的人员选择结果变量名映射
	
	public final static String DEFAULT_USER_VARIABLE_MAP_NAME="assignee";
	
}
