package com.zhixin.core.entities.response;

/**
 * 
* @ClassName: ResourceError 
* @Description: 错误信息
* @author zhangtiebin@bwcmall.com
* @date 2015年7月2日 上午10:12:48 
*
 */
public class ResourceError {
    private int code;
    private String message;

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
 
}
