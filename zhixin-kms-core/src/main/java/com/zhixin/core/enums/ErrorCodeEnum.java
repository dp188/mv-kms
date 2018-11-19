package com.zhixin.core.enums;
/**
 * 
 * @ClassName: ErrorCodeEnum 
 * @Description: 错误编码定义
 * @author zhangtiebin@bwcmall.com
 * @date 2015年7月2日 下午2:19:38 
 *
 */
public enum ErrorCodeEnum {
	//系统默认异常
	SystemError(10000,"System Error","系统错误"),
	AuthFaild(10001,"Auth Faild","认证失败"),
	UnsupportMethod( 10002,"Unsupport Method ","错误的HTTP方法"),
	NoSuchAPI( 10003,"No Such API","没有这个API"),
	NoRoute( 10004,"No Permission","错误的URI,没有找到对应的路由"),
	NoPermission (10005,"No Permission","权限不足"),
	UnknownError( 10006,"Unknown Error","未知错误"),
	JsonError(10007,"Json ErrorJson","解析失败"),
	ParamsError( 10008,"Params Error","参数错误"),
	InvalidUser(10009,"Invalid User","无效的用户"),
	NotFound(10010,"NotFound","访问的资源不存在"),
	BadRequest(10011,"BadRequest","错误的请求"),
	NOTNULL(10012, "Should Not NULL", "内容不能为空"),
	NoDataPermission (10015,"No DATA Permission","数据权限不足"),
	PKNOTNULL(10013, "PK Should Not NULL", "主键不能为空"),
	PKError(10014,"PK Parse Error","主键获取失败"),
	
	//RBAC异常
	UserNotFound(102001,"User Not Found","用户不存在"),
	UserNameEmpty(102002,"User Name Is Empty","用户名为空"),
	UserPWDEmpty(102003,"User Password Is Empty","用户密码为空"),
	UserOrPWDWrong(102004,"User Or Password Is Wrong","用户名或者密码错误"),
	DuplicateRole(102005,"The Role Is Duplicate For User","用户已经拥有这个角色"),
	SESSIONISEXPIRED(403,"Sessiion Is Expired","Session已经过期"),
	SystemDefaultValue(102006,"System DefaultValue ","系统默认角色 "),
	DuplicateRoleName(102007,"The Role Name Is Duplicate","角色名称重复"),
	RoleNameNotNull(102008,"The RoleName Is Null","角色英文名称不能为空"),
	RoleDisplayNameNotNull(102009,"The Role DispalyName Is Null","角色显示名称不能为空"),
	RoleGroupIdNotNull(1020010,"The Role Group Is Null","角色所属分组不能为空"),
	RoleNameTooLong(1021012,"The Name Is Too Long","角色英文名称太长"),
	RoleDisplayNameTooLong(1021012,"The Name Is Too Long","角色显示名称太长"),
	
	NameNotNull(1021011,"The Name Is Null","名称不能为空"),
	NameTooLong(1021012,"The Name Is Too Long","名称太长"),
	DescTooLong(1021013,"The Descript Is Too Long","描述太长"),
	ChildrenRecordExits(1021013,"Children Record Exits,Cant't Deleted!","当前数据有子节点，不能删除！"),
	
	FileUploadError(1021014,"File upload error","文件上传失败"),
	
	TimeFormatWrong(101007,"Time format Wrong","日期格式错误"),
	;
	
	private int code;
	private String message;
	private String description;

	private ErrorCodeEnum(int code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



}
