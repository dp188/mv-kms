package com.zhixin.core.enums;


/**
 * 
 * @ClassName: DataOperationEnum
 * @Description: 
 * @author lideqiang@bwcmall.com
 * @date 2015年7月31日 下午14:37:10
 * 
 */
public enum DataOperationEnum {
    OperationPatch("PATCH", "修改"),
    OperationDelete("DELETE", "删除"),
    OperationPost("POST", "新增"),
    ;
	String action;
	String desc;

	private DataOperationEnum(String action, String desc) {
		this.desc = desc;
		this.action =  action;
	}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
