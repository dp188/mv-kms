package com.zhixin.core.enums;

/**
 * 
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class TaskStatusEnum
 * @package com.zhixin.core.enums
 * @Date 2016年1月16日 下午9:06:30
 */
public enum TaskStatusEnum {
	
	nostart(0, "未开始"), inprogress(1, "执行中"), complete(2, "执行完成"), faild(3, "执行失败");

	private int value;
	private String name;

	TaskStatusEnum(int value, String name) {
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
