package com.zhixin.kms.api.room;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.core.utils.ConditionBuilder;
import com.zhixin.core.utils.PropertyConfigurer;
import com.zhixin.core.utils.Response;
import com.zhixin.core.utils.ResponseBuilder;
import com.zhixin.dto.kms.KMSMany2ManyDTO;
import com.zhixin.kms.websocket.Many2ManyRoom;
import com.zhixin.kms.websocket.Many2ManyRoomManager;
import com.zhixin.service.kms.IKMSMany2ManyRoomService;


@Controller
@RequestMapping("/moveCMD ")
public class KMSManyToManyApI {
	@Autowired
	private Many2ManyRoomManager manytomanyroomManager;

	@Autowired
	private IKMSMany2ManyRoomService many2ManyRoomService;
	
	@Autowired
	private PropertyConfigurer propertyConfigurer;
	
	@RequestMapping(value = "/mirror.html", method = RequestMethod.GET)
	public String mirror(HttpServletRequest request, HttpServletResponse response,String liveId) {
		 //roomId
		request.setAttribute("roomId", liveId);
		return "kms/mirror/many2many";
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
		QueryCondition queryCondition = ConditionBuilder.buildCondition(request, KMSMany2ManyDTO.class);
		Pager<KMSMany2ManyDTO> pager = null;
		try {
			pager = many2ManyRoomService.findForPager(queryCondition);
			if(pager != null && !CollectionUtils.isEmpty(pager.getPageItems())){
				for(KMSMany2ManyDTO room : pager.getPageItems()){
					room.setUserCount(manytomanyroomManager.countUsers(room.getId().toString()));
				}
			}
		} catch (ApiException e) {
			  return  ResponseBuilder.buildFaildResponse(e);
		} 
		return ResponseBuilder.buildPager(pager);
	}
	
	
	//视频通话界面(pc)
	@RequestMapping(value = "/pc_manytomany_participater.html", method = RequestMethod.GET)
	public String participate_pc(HttpServletRequest request, HttpServletResponse response,String CallId) {
		request.setAttribute("CallId", CallId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("groupChat.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("groupChat.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("groupChat.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("groupChat.frameRate.min").toString());
		return "kms/manytomany/pc_manytomany_participater";
	}
	//视频通话界面(android)
	@RequestMapping(value = "/m_manytomany_participater.html", method = RequestMethod.GET)
	public String participate_m(HttpServletRequest request, HttpServletResponse response,String CallId) {
		request.setAttribute("CallId", CallId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("groupChat.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("groupChat.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("groupChat.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("groupChat.frameRate.min").toString());
		return "kms/manytomany/m_manytomany_participater";
	}
	//音频通话界面(pc)
	@RequestMapping(value = "/pc_manytomany_audio.html", method = RequestMethod.GET)
	public String participate_audio_pc(HttpServletRequest request, HttpServletResponse response, String CallId) {
		request.setAttribute("CallId", CallId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("groupChat.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("groupChat.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("groupChat.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("groupChat.frameRate.min").toString());
		return "kms/manytomany/pc_manytomany_audio";
	}
	//音频通话界面(android)
	@RequestMapping(value = "/m_manytomany_audio.html", method = RequestMethod.GET)
	public String participate_audio_m(HttpServletRequest request, HttpServletResponse response, String CallId) {
		request.setAttribute("CallId", CallId);
		request.setAttribute("maxWidth",propertyConfigurer.getProperty("groupChat.width.max").toString());
		request.setAttribute("minWidth",propertyConfigurer.getProperty("groupChat.width.min").toString());
		request.setAttribute("maxframeRate",propertyConfigurer.getProperty("groupChat.frameRate.max").toString());
		request.setAttribute("minframeRate",propertyConfigurer.getProperty("groupChat.frameRate.min").toString());
		return "kms/manytomany/m_manytomany_audio";
	}
	
	//开始
	@RequestMapping(value = "/startCall", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> startCall (HttpServletRequest request, HttpServletResponse response
			,@RequestParam("call_type")int call_type,//呼叫类型（音频、视频）
			@RequestParam("liveName")String liveName,//呼叫名字
			@RequestParam("livePath")String livePath,//呼叫数据保存地址Path
			@RequestParam("remark")String remark) //显示在视频上的其它备注（视频呼叫有效）
	{
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		Long liveId=null;
		try {
			if(StringUtils.isEmpty(livePath))
			{
				throw new ApiException(ErrorCodeEnum.ParamsError.getCode(),"LivePath为空");
			}else
			{
				File file=new File(livePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				file.setReadable(true, false);
				file.setWritable(true, false);
				livePath=file.getAbsolutePath()+File.separator;
				Many2ManyRoom many2ManyRoom=manytomanyroomManager.CreateRoom(call_type,liveName,livePath,remark);//创建一个房间
				if (many2ManyRoom!=null) {
					liveId=many2ManyRoom.getRoomID();
				}
				else
				{
					result.put("success", false);
					result.put("message", "操作失败!");
					return result;
				}
			}		
			//设置返回消息内容
			result.put("CallId", String.valueOf(liveId)); //本次呼叫ID
			result.put("success", true);
			result.put("message", "操作成功"); 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//接收音视频通话
	@RequestMapping(value = "/receiveCall", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> receiveCall (HttpServletRequest request, HttpServletResponse response
			,String CallId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//判断CallId房间是否还存在
			if (manytomanyroomManager.getRoom(CallId)!=null) {
				//设置返回消息内容
				result.put("success", true);
				result.put("message", "操作成功"); 
			}
			else {
				result.put("success", false);
				result.put("message", "操作失败!"); 
			}
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//强插
	@RequestMapping(value = "/compelIn", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> compelIn (HttpServletRequest request, HttpServletResponse response
			,String CallId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			if (manytomanyroomManager.getRoom(CallId)!=null) {
				//设置返回消息内容
				result.put("success", true);
				result.put("message", "操作成功"); 
			} 
			else {
				result.put("success", false);
				result.put("message", "操作失败!"); 
			}
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//强拆
	@RequestMapping(value = "/compelOut", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> compelOut (HttpServletRequest request, HttpServletResponse response
			,String CallId) {
		//数据返回
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//
			if (manytomanyroomManager.getRoom(CallId)!=null) {
				//设置返回消息内容
				
				result.put("success", true);
				result.put("message", "操作成功"); 
			} 
			else {
				result.put("success", false);
				result.put("message", "操作失败!"); 
			}
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
	//结束
	@RequestMapping(value = "/hangUp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object>  hangUp  (HttpServletRequest request, HttpServletResponse response
			,String CallId) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//
			Many2ManyRoom  room=null;
			room=manytomanyroomManager.getRoom(CallId);
			if (room!=null) {
				manytomanyroomManager.closeRoom(CallId);
				result.put("success", true);
				result.put("message", "操作成功"); 
			} 
			else {
				result.put("success", false);
				result.put("message", "操作失败!"); 
			} 
		} catch (ApiException e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "操作失败!"); 
		}  
		return result;
	}
}
