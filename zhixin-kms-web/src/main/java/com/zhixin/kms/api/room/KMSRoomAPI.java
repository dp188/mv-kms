package com.zhixin.kms.api.room;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.core.utils.ConditionBuilder;
import com.zhixin.core.utils.PropertyConfigurer;
import com.zhixin.core.utils.Response;
import com.zhixin.core.utils.ResponseBuilder;
import com.zhixin.dto.kms.KMSRoomDTO;
import com.zhixin.kms.websocket.One2ManyCallHandler;
import com.zhixin.service.kms.IKMSRoomService;

/**
 * 
 * @author zhangtiebin@bwcmall.com
 * @description 视频API
 * @class VideoAPI
 * @package com.zhixin.kms.api.room
 * @Date 2015年12月19日 下午8:10:14
 */
@Controller
@RequestMapping("/live")
public class KMSRoomAPI {
	
	@Autowired
	private IKMSRoomService roomService;
	
	@Autowired
	private PropertyConfigurer propertyConfigurer;
	
	@RequestMapping(value = "/mirror.html", method = RequestMethod.GET)
	public String mirror(HttpServletRequest request, HttpServletResponse response,String liveId) {
		 //roomId
		request.setAttribute("roomId", liveId);
		return "kms/mirror/one2many";
	}
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Response get(HttpServletRequest request, HttpServletResponse response) {
		QueryCondition queryCondition = ConditionBuilder.buildCondition(request, KMSRoomDTO.class);
		Pager<KMSRoomDTO> pager = null;
		try {
			pager = roomService.findForPager(queryCondition);
			if(pager != null && !CollectionUtils.isEmpty(pager.getPageItems())){
				for(KMSRoomDTO room : pager.getPageItems()){
					room.setUserCount(One2ManyCallHandler.getCallHandler().countUsers(room.getId().toString()));
				}
			}
			
		} catch (ApiException e) {
			  return  ResponseBuilder.buildFaildResponse(e);
		} 
		return ResponseBuilder.buildPager(pager);
	}
	
	@RequestMapping(value = "/m_one2ManyPresent.html", method = RequestMethod.GET)
	public String gotoPresent_m(HttpServletRequest request, HttpServletResponse response,String liveId) {
		 //roomId
		request.setAttribute("roomId", liveId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("present.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("present.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("present.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("present.frameRate.min").toString());
		return "kms/room/m_one2ManyPresent";
	}
	
	@RequestMapping(value = "/m_one2ManyViewer.html", method = RequestMethod.GET)
	public String gotoViewer_m(HttpServletRequest request, HttpServletResponse response,String liveId ) {
		request.setAttribute("roomId", liveId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("present.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("present.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("present.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("present.frameRate.min").toString());
		return "kms/room/m_one2ManyViewer";
	}
	
	@RequestMapping(value = "/pc_one2ManyPresent.html", method = RequestMethod.GET)
	public String gotoPresent_pc(HttpServletRequest request, HttpServletResponse response,String liveId) {
		request.setAttribute("roomId", liveId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("present.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("present.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("present.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("present.frameRate.min").toString());
		return "kms/room/pc_one2ManyPresent";
	}
	
	@RequestMapping(value = "/pc_one2ManyViewer.html", method = RequestMethod.GET)
	public String gotoViewer_pc(HttpServletRequest request, HttpServletResponse response,String liveId ) {
		request.setAttribute("roomId", liveId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("present.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("present.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("present.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("present.frameRate.min").toString());
		return "kms/room/pc_one2ManyViewer";
	}
	//开始直播
	@RequestMapping(value = "/startLive", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  startLive(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("liveName")String liveName,
			@RequestParam("livePath")String livePath,
			@RequestParam("remark")String remark,
			@RequestParam("isDuplexing")boolean isDuplexing ) {
		KMSRoomDTO roomDto = new KMSRoomDTO();
		//将room数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		Long pk = null;
		try {
			//创建room
			roomDto.setName(liveName);
			if(StringUtils.isEmpty(livePath)){
				throw new ApiException(ErrorCodeEnum.ParamsError.getCode(),"LivePath为空!");
			}else{
				File file = new File(livePath);
				if(file.exists()){
					file.mkdirs();
				}
				file.setReadable(true, false);
				file.setWritable(true, false);
				livePath=file.getAbsolutePath()+File.separator;
				roomDto.setLivePath(livePath);
				roomDto.setIsDuplexing(String.valueOf(isDuplexing));
				roomDto.setRemark(remark);
				roomDto.setName(liveName);
				pk = roomService.create(roomDto);//创建room，返回主键ID
				//设置返回消息内容
				result.put("success", true);
				result.put("message", "操作成功");
				result.put("liveId", String.valueOf(pk));
			}
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			if(e instanceof ApiException){
				result.put("message",e.getMessage()); 
			}else{
				result.put("message", "操作失败!"); 
			}
			
		}  
		return result;
	}
	//停止直播
	@RequestMapping(value = "/stopLive", method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> stopLive (HttpServletRequest request, HttpServletResponse response,String liveId) {
		//将room数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//移除room，返回主键ID
			roomService.remove(Long.parseLong(liveId));
			//关闭房间并通知其他观看者
			One2ManyCallHandler.getCallHandler().closeRoom(liveId);
			//设置返回消息内容
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//开始语音
	@RequestMapping(value = "/startAudio", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> startAudio (HttpServletRequest request, HttpServletResponse response,
			@RequestParam("liveId")String liveId,
			@RequestParam("Str_talkbackPath")String Str_talkbackPath) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			// One2ManyCallHandler.getCallHandler().closeRoom(liveId);
			//设置返回消息内容
			result.put("talkbackId", liveId); 
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//停止语音
	@RequestMapping(value = "/stopAudio", method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> stopAudio (HttpServletRequest request, HttpServletResponse response
			,String liveId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			// One2ManyCallHandler.getCallHandler().closeRoom(liveId);
			//设置返回消息内容
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//开始观看
	@RequestMapping(value = "/startWatch", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> startWatch  (HttpServletRequest request, HttpServletResponse response
			,String liveId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			// One2ManyCallHandler.getCallHandler().closeRoom(liveId);
			//设置返回消息内容
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//停止观看
	@RequestMapping(value = "/stopWatch", method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> stopWatch   (HttpServletRequest request, HttpServletResponse response
			,String liveId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//
			// One2ManyCallHandler.getCallHandler().closeRoom(liveId);
			 
			//设置返回消息内容
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
}
