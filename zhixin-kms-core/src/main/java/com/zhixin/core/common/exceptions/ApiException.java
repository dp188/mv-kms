package com.zhixin.core.common.exceptions;

import com.zhixin.core.enums.ErrorCodeEnum;

/**
 * @Author by maitian on 14/12/18.
 */
public class ApiException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3542451561250518138L;
	private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
    public ApiException(ErrorCodeEnum error){
    	super(error.getDescription());
    	this.code =  error.getCode();
    }
    public ApiException (int code,String msg,Throwable cause) {
        super(msg,cause);
        this.code = code;
    }
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
    
}
