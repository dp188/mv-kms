package com.zhixin.core.utils;

import java.util.List;

import com.zhixin.core.common.constant.Constant;
import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.PagerResultInfo;
import com.zhixin.core.entities.response.BaseResponse;
import com.zhixin.core.entities.response.ResourceError;
import com.zhixin.core.enums.ErrorCodeEnum;

/**
 * 
* @ClassName: ResponseBuilder 
* @Description:  REST Ful请求响应构建工具类
* @author zhangtiebin@bwcmall.com
* @date 2015年6月26日 上午11:02:22 
*
 */
public class ResponseBuilder {
	
	/**
	 * 构建默认的成功响应信息
	 * @param obj
	 * @return
	 */
	public static  Response buildSuccessResponse(){
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(true);
		
		response.setMessage(Constant.OPERATE_SUCCESS);
		
		return Response.status(Response.Status.OK).entity(response).build();
		 
	}
	/**
	 * 构建默认的成功响应信息
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  Response buildSuccessResponse(Object obj){
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(true);
		response.setMessage(Constant.OPERATE_SUCCESS);
		
		if(obj != null){
			if(obj instanceof List ){
				//post
				List<Object> list  =	(List<Object>)obj;
				if(list.size() == 1){
					response.setResult(list.get(0));
				}
			}else if ( obj instanceof Object[]){
				//post
				Object[] array  =	(Object[])obj;
				if(array.length == 1){
					response.setResult(array[0]);
				}
			}else{
				//patch || put
				response.setResult(obj);
			}	
		}	
		
		return Response.status(Response.Status.OK).entity(response).build();
		 
	}
	/**
	 * 构建默认的成功响应信息
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static  Response buildSuccessResponseByList(List list){
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(true);
		response.setMessage(Constant.OPERATE_SUCCESS); 
		response.setResult(list);  
		return Response.status(Response.Status.OK).entity(response).build();
		 
	}
	/**
	 * 构建成功响应信息
	 * @param obj
	 * @param meessage
	 * @return
	 */
	public static Response buildSuccessResponse(Object obj,String meessage){
		
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(true);
		
		response.setResult(obj);
		
		response.setMessage(meessage);
		
		return Response.status(Response.Status.OK).entity(response).build();
	} 
	
	/**
	 * 构建成功响应信息
	 * @param obj
	 * @param meessage
	 * @return
	 */
	public static Response buildFaildResponse(int code,String meessage){
		ResourceError error = new ResourceError();
		
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(false);
		 
		response.setMessage(meessage);
		
		error.setCode(code);
		error.setMessage(meessage);
		
		response.setError(error);
		 
		if(code == ErrorCodeEnum.AuthFaild.getCode() || code == ErrorCodeEnum.InvalidUser.getCode() || code == ErrorCodeEnum.SESSIONISEXPIRED.getCode()){
			return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
		}
		if(code == ErrorCodeEnum.NoPermission.getCode() ){
			return Response.status(Response.Status.FORBIDDEN).entity(response).build();
		}
		return Response.status(Response.Status.OK).entity(response).build();
	} 
	/**
	 * 构建失败响应信息
	 * @param e 
	 * @return
	 */
	public static Response buildFaildResponse(ApiException e){
		 return buildFaildResponse(e.getCode(), e.getMessage());
	} 
	
	
	
	/**
	 * 构建错误的响应
	 * @param obj 
	 * @return
	 */
	public static Response buildFaildResponse(ErrorCodeEnum errorCode){
		return buildFaildResponse(errorCode.getCode(),errorCode.getDescription());
	} 
	/**
     * 构建分页响应信息
	 * @param pager
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Response buildPager(Pager pager){
		if(pager == null){
			pager = new Pager();
		}
		PagerResultInfo result_info = new PagerResultInfo();
		BaseResponse response = new BaseResponse();
		
		response.setSuccess(true);
		
		response.setResult(pager.getPageItems());
		response.setResult_info(result_info);
		
		result_info.setPage(pager.getCurrPage());
		result_info.setPer_page(pager.getPageSize());
		result_info.setTotal_count(pager.getRowsCount());
		
		//计算总页数
		int rowsCount = pager.getRowsCount();
		int pageSize = pager.getPageSize();
		int pageCount = 0;
		if(pageSize != 0){
			pageCount = (( rowsCount /  pageSize) > (rowsCount / pageSize) ? ( rowsCount/ pageSize) + 1:rowsCount / pageSize);
			if(rowsCount%pageSize>0){
				pageCount =  pageCount+1;
			}
		}
		
		result_info.setPage_count(pageCount); 
		 
		return Response.status(Response.Status.OK).entity(response).build();
	}
}
