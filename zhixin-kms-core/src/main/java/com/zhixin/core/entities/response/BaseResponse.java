package com.zhixin.core.entities.response;

import java.io.Serializable;

import com.zhixin.core.entities.query.PagerResultInfo;

/**
 * 
 * @ClassName: BaseResponse
 * @Description: 基础响应
 * @author zhangtiebin@bwcmall.com
 * @date 2015年7月2日 上午10:11:22
 *
 */
//ESTFul基础响应信息"
public class BaseResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1518484851893182089L;
	//成功失败标志
	private Boolean success = true; 
	//错误信息
	private ResourceError error; 
	//成功提示信息
	private String message; 
	//响应数据实体
	private Object result; 
	//分页元数据信息
	private PagerResultInfo result_info;
	

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public ResourceError getError() {
		return error;
	}

	public void setError(ResourceError error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public PagerResultInfo getResult_info() {
		return result_info;
	}

	public void setResult_info(PagerResultInfo result_info) {
		this.result_info = result_info;
	}

}
