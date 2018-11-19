package com.zhixin.core.enums;

/***
 * 
* @ClassName: StatusEnum 
* @Description: 系统状态枚举
* @author zhangtiebin@bwcmall.com
* @date 2015年6月24日 上午10:41:00 
*
 */
public enum StatusEnum {
	deleted(0,"删除"),
	created(1,"创建"),
	online(2,"直播中"), 
	closed(3,"已关闭");

    private int value;
    private String name; 
    StatusEnum(int value, String name) {
    	this.value = value;
    	this.name = name;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
  
}
