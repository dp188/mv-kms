/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.zhixin.kms.websocket;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kurento.client.Continuation;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.MediaType;
import org.kurento.client.OnIceCandidateEvent;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.zhixin.core.enums.StatusEnum;
import com.zhixin.dto.kms.KMSCaptionDTO;
import com.zhixin.dto.kms.KMSRoomDTO;
import com.zhixin.service.kms.IKMSCaptionService;
import com.zhixin.service.kms.IKMSRoomService;


/**
 * Protocol handler for 1 to N video call communication.
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
public class One2ManyCallHandler extends TextWebSocketHandler {
	private static final One2ManyCallHandler handler = new One2ManyCallHandler();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(One2ManyCallHandler.class);// 输出以CallHander.class为源的日志
//	private static final Gson gson = new GsonBuilder().create();// Gson是实现Java对象与Json之间的转换

	private static final SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd_HH-mm-ss-S");
	
	@Value("${VideoStyle}")
	String VideoStyle;
  
	@Autowired
	private KurentoClient client;// KMS客户端
	@Autowired
	private IKMSRoomService roomDBService;// KMS房间服务
	@Autowired
	private UserRegistry userRegistry;

	@Autowired
	private IKMSCaptionService kmsCaptionService;
	
	
	public static One2ManyCallHandler getCallHandler() {
		return handler;
	}

	/***
	 * 直播视频管道
	 */
	private Map<String, MediaPipeline> liveBroadcastPipelineCache = new ConcurrentHashMap<String, MediaPipeline>();// MediaPipeline鍒楄〃

	/****
	 * 语音通信管道
	 */
	private Map<String, MediaPipeline> audioPipelineCache = new ConcurrentHashMap<String, MediaPipeline>();// MediaPipeline鍒楄〃

	/****
	 * 直播者信息
	 */
	private Map<String, UserSession> presenterUserSessionCache = new ConcurrentHashMap<String, UserSession>();// 鐩存挱鑰呬細璇濆垪琛�
	/********
	 * 语音会话信息
	 */
	private Map<String, UserSession> audioStarterUserSessionCache = new ConcurrentHashMap<String, UserSession>();// 寮�鍚闊充細璇濆垪琛�

	/***
	 * 观看者
	 */
	private final ConcurrentHashMap<String, UserSession> viewersUserSessionCache = new ConcurrentHashMap<String, UserSession>();// 瑙傜湅鑰呭垪琛�

	public enum ActionConstant {
		presenter, viewer, caption, onIceCandidate, stop, startAudio, receiveVideoFrom, stopAudio, onIceCandidate_Audio;
	}

	public void handleTextMessage(WebSocketSession session,
			TextMessage textMessage) throws Exception {
		try {
			JSONObject msg = JSON.parseObject(textMessage.getPayload());
			ActionConstant action = ActionConstant.valueOf(msg.getString("id"));
			switch (action) {
			case presenter:
				doProcPresenterInit(msg, session);
				break;

			case viewer:
				doProcViewerInit(msg, session);
				break;

			case caption:
				doProcCaption(msg, session);
				break;

			case onIceCandidate:
				doProcVideoIceCandidate(msg, session);
				break;

			case stop:
				doProcStopLiveBroadcast(msg, session);
				break;

			case startAudio:
				doProcStartAudio(msg, session);
				break;

			case receiveVideoFrom:
				doProcReceiveAudioRequest(msg, session);
				break;

			case stopAudio:
				doProcStopAudio(msg, session);
				break;

			case onIceCandidate_Audio:
				doProcAudioIceCandidate(msg, session);
				break;

			default:
				LOGGER.error("处理出错");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("一对多直播处理前端请求出错", e);
		}
	}

	private void doProcAudioIceCandidate(JSONObject msg,WebSocketSession session) {
		JSONObject candidate = msg.getJSONObject("candidate");
		
		UserSession cur = presenterUserSessionCache.get(session.getId());
		// 是否是观看者
		if (cur == null) {
			cur = viewersUserSessionCache.get(session.getId());
		}

		if (cur != null) {
			IceCandidate cand = new IceCandidate(candidate.getString("candidate"),candidate.getString("sdpMid"), candidate.getInteger("sdpMLineIndex"));
			cur.addCandidate_Audio(cand, msg.getString("name"));
		}
	}

	private void doProcStopAudio(JSONObject msg, WebSocketSession session) {
		UserSession user = viewersUserSessionCache.get(session.getId());
		if (user != null) {
			if (audioStarterUserSessionCache.isEmpty()) {
				final JsonObject message = new JsonObject();
				message.addProperty("id", "rejectStartAudio");
				message.addProperty("message", "当前无人员开启语音");
				try {
					user.sendMessage(message);
				} catch (Exception e) {
					LOGGER.error("发送消息出错",e);
				}
				return;
			}
			
			if (audioStarterUserSessionCache.get(user.getSession().getId())==null) {
				final JsonObject message = new JsonObject();
				message.addProperty("id", "rejectStartAudio");
				message.addProperty("message", "此次语音不是您开启的，您无权关闭");
				try {
					user.sendMessage(message);
				} catch (Exception e) {
					LOGGER.error("发送消息出错",e);
				}
				return;
			}
			final JsonObject participantLeftJson = new JsonObject();
			participantLeftJson.addProperty("id", "participantLeft");
			participantLeftJson.addProperty("name", user.getName());

			// 告知观看者音频通道关闭
			Map<String, UserSession> viewers = getLiveViewers(user.getRoomId());
			if (viewers != null) {
				for (String key : viewers.keySet()) {
					if (!viewers.get(key).getName().equals(user.getName())) {
						// 发送消息
						try {
							viewers.get(key).sendMessage(participantLeftJson);
						} catch (final Exception e) {
							LOGGER.debug("sendmessage is failure");
						}
						// 释放对应的endpoint资源
						WebRtcEndpoint endpoint=null;
						endpoint=viewers.get(key).getIncomingEndpointAudio().get(user.getName());
						if (endpoint!=null) {
							endpoint.release(new Continuation<Void>() {
								@Override
								public void onSuccess(Void result) throws Exception {
									LOGGER.trace(" Released successfully incoming EP ");
								}

								@Override
								public void onError(Throwable cause) throws Exception {
									LOGGER.warn("Could not release incoming EP");
								}
							});
						}
						
						
						if (viewers.get(key).getWebRtcEndpointAudio()!=null) {
							viewers.get(key).getWebRtcEndpointAudio().release();
							viewers.get(key).setWebRtcEndpointAudio(null);;
						};
						if (viewers.get(key).getRecorderEndpointAudio() !=null) {
							viewers.get(key).getRecorderEndpointAudio().release();
							viewers.get(key).setRecorderEndpointAudio(null);;
						}

					}
				}
			}
			
			//释放pipeline资源并从列表中移除
			if (audioPipelineCache.get(user.getRoomId())!=null) {
				audioPipelineCache.get(user.getRoomId()).release();
				audioPipelineCache.remove(user.getRoomId());
			}
			if (audioStarterUserSessionCache.get(user.getSession().getId())!=null) {
				audioStarterUserSessionCache.remove(user.getSession().getId());
			}

			// 告诉直播者语音通道关闭
			UserSession Presenter = getCurrentPresenter(user.getRoomId());
			if (Presenter != null) {

				try {
					Presenter.sendMessage(participantLeftJson);
				} catch (final Exception e) {
					LOGGER.debug("sendmessage is failure");
				}
				// 释放对应的endpoint资源
				WebRtcEndpoint endpoint=null;
				endpoint=Presenter.getIncomingEndpointAudio().get(user.getName());
				if (endpoint!=null) {
					endpoint.release(new Continuation<Void>() {
						@Override
						public void onSuccess(Void result) throws Exception {
							LOGGER.trace(" Released successfully incoming EP ");
						}

						@Override
						public void onError(Throwable cause) throws Exception {
							LOGGER.warn("Could not release incoming EP");
						}
					});
				}

				if (Presenter.getWebRtcEndpointAudio()!=null) {
					Presenter.getWebRtcEndpointAudio().release();
					Presenter.setWebRtcEndpointAudio(null);;
				};
				if (Presenter.getRecorderEndpointAudio() !=null) {
					Presenter.getRecorderEndpointAudio().release();
					Presenter.setRecorderEndpointAudio(null);;
				}
			}			
		}
	}

	/***
	 * 接收音频处理
	 * @param msg
	 * @param session
	 * @throws IOException 
	 */
	private void doProcReceiveAudioRequest(JSONObject msg,final WebSocketSession session) throws IOException {
		final String senderName =  msg.getString("sender");
		final String recivename=  msg.getString("name");
		final String sdpOffer = msg.getString("sdpOffer");
		final UserSession voiceSender=userRegistry.getByName(senderName);
		UserSession voiceReciver=userRegistry.getByName(recivename);
		if(voiceSender != null && voiceSender.equals(voiceReciver)){//直播者进入的时候
			
			MediaPipeline audioPipeline=client.createMediaPipeline();
			audioPipelineCache.put(voiceReciver.getRoomId(), audioPipeline);// 缓存直播者信息
			audioStarterUserSessionCache.put(session.getId(), voiceSender);
			
			// 创建申请者的endpoint
			voiceSender.setAudioPipeline(audioPipeline);
			WebRtcEndpoint audioEndpoint= new WebRtcEndpoint.Builder(audioPipeline).build();
			voiceSender.setWebRtcEndpointAudio(audioEndpoint);
			
			// 存储音频

			KMSRoomDTO dto=roomDBService.find(Long.parseLong(voiceSender.getRoomId()));
			
			String roomBaseDir=dto.getLivePath()+voiceSender.getRoomId();
			File file = new File(roomBaseDir);
			if(!file.exists()){
				file.mkdirs();
			}
			file.setReadable(true, false);
			file.setWritable(true, false);
			roomBaseDir=file.getAbsolutePath()+File.separator;
			LOGGER.info("直播文件存放：----------"+ roomBaseDir); 
			if ("WEBM".equals(VideoStyle)) {
				voiceSender.setRecorderEndpointAudio(new RecorderEndpoint.Builder(audioPipeline,"file:///"+roomBaseDir +File.separator +voiceSender.getRoomId()
				+"_Audio_"+ voiceSender.getSession().getId()+".webm").withMediaProfile(MediaProfileSpecType.WEBM_AUDIO_ONLY).build());
			}
			else {
				voiceSender.setRecorderEndpointAudio(new RecorderEndpoint.Builder(audioPipeline,"file:///"+roomBaseDir +File.separator + voiceSender.getRoomId()
				+"_Audio_"+ voiceSender.getSession().getId()+".mp4").withMediaProfile(MediaProfileSpecType.MP4_AUDIO_ONLY).build());
			}
			
			voiceSender.getWebRtcEndpointAudio().connect(voiceSender.getRecorderEndpointAudio(),MediaType.AUDIO);
			voiceSender.getRecorderEndpointAudio().record();
			
			final JsonArray participantsArray = new JsonArray();

			final JsonObject newParticipantMsg = new JsonObject();
			newParticipantMsg.addProperty("id", "newParticipantArrived");
			newParticipantMsg.addProperty("name", msg.getString("name"));
			
			doNoticeBroadcast(audioPipeline, participantsArray, newParticipantMsg,voiceSender.getRoomId(), voiceReciver.getName());
			
			
			WebRtcEndpoint presenterWebRtc = voiceSender.getWebRtcEndpointAudio();//音频服务
			// ICEcanditate
			presenterWebRtc.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {
						@Override
						public void onEvent(OnIceCandidateEvent event) {
							JsonObject response = new JsonObject();
							response.addProperty("id", "iceCandidate_Audio");
							response.addProperty("name", senderName); 
							response.add("candidate", JsonUtils
									.toJsonObject(event.getCandidate()));
							response.addProperty("roomId",voiceSender.getRoomId());
							try {
								synchronized (session) {
									session.sendMessage(new TextMessage(
											response.toString()));
								}
							} catch (IOException e) {
								LOGGER.debug(e.getMessage());
							}
						}
			});

			// SDP			
			String sdpAnswer = presenterWebRtc.processOffer(sdpOffer);
			
			JsonObject response = new JsonObject();
			response.addProperty("id", "receiveVideoAnswer");
			response.addProperty("response", "accepted");
			response.addProperty("name", senderName);
			response.addProperty("sdpAnswer", sdpAnswer);
			response.addProperty("roomId", String.valueOf(voiceSender.getRoomId()));
			
			synchronized (session) {
				voiceSender.sendMessage(response);
			}			
			presenterWebRtc.gatherCandidates();
			audioStarterUserSessionCache.put(voiceSender.getRoomId(), voiceSender);
		}else{
			if (voiceSender!=null&&voiceReciver!=null) {			

				WebRtcEndpoint nextWebRtc = new WebRtcEndpoint.Builder(voiceSender.getAudioPipeline()).build();

				nextWebRtc.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {
					@Override
					public void onEvent(OnIceCandidateEvent event) {
						JsonObject response = new JsonObject();
						response.addProperty("id", "iceCandidate_Audio");
						response.addProperty("name", senderName);
						response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
						try {
							synchronized (session) {
								session.sendMessage(new TextMessage(response.toString()));
							}
						} catch (IOException e) {
							LOGGER.debug(e.getMessage());
						}
					}
				});

				voiceReciver.setWebRtcEndpointAudio(nextWebRtc);
				voiceSender.getWebRtcEndpointAudio().connect(nextWebRtc,MediaType.AUDIO);
				String sdpAnswer = nextWebRtc.processOffer(sdpOffer);
				voiceReciver.getIncomingEndpointAudio().put(senderName, nextWebRtc);

				JsonObject response = new JsonObject();
				response.addProperty("id", "receiveVideoAnswer");
				response.addProperty("response", "accepted");
				response.addProperty("name", senderName);
				response.addProperty("sdpAnswer", sdpAnswer);

				synchronized (session) {
					voiceReciver.sendMessage(response);
				}
				nextWebRtc.gatherCandidates();
			}
		
		}
		
	}

	
	/****
	 * 进行音频发言
	 * @param msg
	 * @param session
	 * @throws IOException 
	 */ 
	private void doProcStartAudio(JSONObject msg, WebSocketSession session) throws IOException {
		UserSession requester = viewersUserSessionCache.get(session.getId());
		
		if (requester==null) {
			final JsonObject message = new JsonObject();
			message.addProperty("id", "rejectStartAudio");
			message.addProperty("message", "您不是观看者或者未注册");
//			requester.sendMessage(message);
			LOGGER.error("您不是观看者或者未注册");
			return;
		}
		
		//鍒涘缓pipeline		
		if (audioPipelineCache.get(msg.getString("roomId"))!=null) {
			final JsonObject message = new JsonObject();
			message.addProperty("id", "rejectStartAudio");
			message.addProperty("message", "已经有人开启了语音，待其关闭了才能申请");
			requester.sendMessage(message);
			return;
		}
		
		final JsonArray participantsArray = new JsonArray();

		final JsonObject newParticipantMsg = new JsonObject();
		newParticipantMsg.addProperty("id", "newParticipantArrived");
		newParticipantMsg.addProperty("name", msg.getString("name"));				
		if (requester != null) {			
			final JsonObject existingParticipantsMsg = new JsonObject();
			existingParticipantsMsg.addProperty("id", "existingParticipants");
			existingParticipantsMsg.add("data", participantsArray);
			LOGGER.debug("PARTICIPANT {}: sending a list of {} participants", requester.getName(),
					participantsArray.size());
			try {
				requester.sendMessage(existingParticipantsMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void doNoticeBroadcast(MediaPipeline audioPipeline,
			final JsonArray participantsArray,
			final JsonObject newParticipantMsg, String roomId, String name) {
		// 给房间中的所有观看者发送消息
		Map<String, UserSession> viewers  = getLiveViewers(roomId);
		if (viewers != null) {
			for (String key : viewers.keySet()) {
				if (!viewers.get(key).getName().equals(name)) {
					try {
						viewers.get(key).sendMessage(newParticipantMsg);
					} catch (Exception e) {
						LOGGER.debug("sendmessage is failure");
					}
					// 将观看者加入已存在列表
					final JsonElement participantName = new JsonPrimitive(viewers.get(key).getName());
					participantsArray.add(participantName);
					//设置pipeline_Audio
					viewers.get(key).setAudioPipeline(audioPipeline);
				}
			}
		}

		// 告诉直播者有新语音
		UserSession presenter = getCurrentPresenter(roomId);
		if (presenter != null) {
			try {
				presenter.sendMessage(newParticipantMsg);
			} catch (Exception e) {
				LOGGER.debug("sendmessage is failure");
			}
			//将直播者加入已存在列表
			final JsonElement participantName = new JsonPrimitive(presenter.getName());
			participantsArray.add(participantName);
			//设置pipeline_Audio
			presenter.setAudioPipeline(audioPipeline);
		}
	}

	/****
	 * 停止直播
	 * @param msg
	 * @param session
	 * @throws Exception
	 */
	private void doProcStopLiveBroadcast(JSONObject msg,	WebSocketSession session) throws Exception {
		doStopLive(session, true);
	}

	/***
	 * 处理视频
	 * 
	 * @param msg
	 * @param session
	 */
	private void doProcVideoIceCandidate(JSONObject msg,WebSocketSession session) {
		JSONObject candidate = msg.getJSONObject("candidate");
		String roomId = msg.getString("roomId");
		UserSession presenter = this.getCurrentPresenter(roomId);
		UserSession user = null;
		if (presenter != null && session == presenter.getSession()) {
			user = presenter;
		} else {
			user = viewersUserSessionCache.get(session.getId());
		}
		if (user != null) {
			IceCandidate cand = new IceCandidate(candidate.getString("candidate"),candidate.getString("sdpMid"), candidate.getInteger("sdpMLineIndex"));
			user.addCandidate(cand);
		}
	}

	/**
	 * 处理字幕数据
	 * 
	 * @param msg
	 * @param session
	 */
	private void doProcCaption(JSONObject msg, WebSocketSession session) {
		String sessionId = session.getId();
		UserSession presenterUserSession = presenterUserSessionCache.get(sessionId);// 查找此会话是否在cachedPresenterUserSession中
		if (presenterUserSession != null) {
			//给直播者发送字幕
			final JsonObject captionData = new JsonObject();
			captionData.addProperty("id", "updateCaption");
			captionData.addProperty("text", msg.getString("text"));
			captionData.addProperty("append", msg.getBoolean("append"));
			try {
				presenterUserSession.sendMessage(captionData);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			Map<String, UserSession> viewers = getLiveViewers(presenterUserSession.getRoomId());
			if (viewers != null) {// 给房间中的所有观看者发送字幕
				for (String key : viewers.keySet()) {
					
					//打时间戳记录在数据库中
					persistentCaption(presenterUserSession.getRoomId(),msg.getString("text"),msg.getBoolean("append"));
					try {
						viewers.get(key).sendMessage(captionData);
					} catch (final IOException e) {
						LOGGER.debug("sendmessage is failure");
					}
				}
			}
		}
	}

	
	/***
	 * 持久化字幕到数据库
	 * @param roomId
	 * @param text
	 * @param applend
	 */
	private void persistentCaption(String roomId, String text,Boolean applend) {
		try {
			KMSCaptionDTO caption = new KMSCaptionDTO();
			caption.setKey(roomId);
			caption.setCaption(text);
			caption.setCapTime(new Date(System.currentTimeMillis()));
			caption.setCapTimeShow(new Date(System.currentTimeMillis()));		
			kmsCaptionService.create(caption);
			//TODO (等待实现)
		} catch (Exception e) {
			LOGGER.error("插入字幕文件出错",e); 
		}
	}

	/***
	 * 获取某个房间的直播观看人员
	 * @param roomId
	 * @return
	 */
	private Map<String, UserSession> getLiveViewers(String roomId) {
		Map<String, UserSession> viewers = new HashMap<String, UserSession>();
		if (viewersUserSessionCache != null) {
			Set<String> keyset = viewersUserSessionCache.keySet();
			UserSession temp = null;
			for (String key : keyset) {
				temp = viewersUserSessionCache.get(key);
				if (temp.getRoomId().equals(roomId)) {
					viewers.put(temp.getSession().getId(), temp);
				}
			}
		}
		return viewers;
	}

	/**
	 * 处理观看者初始化
	 * 
	 * @param msg
	 * @param session
	 * @throws IOException 
	 */
	private void doProcViewerInit(JSONObject msg, final WebSocketSession session) throws IOException {
		String roomId = null;
		try {
			roomId = msg.getString("roomId");
		} catch (Exception e) {
			LOGGER.error("No roomId found!");
		}
		UserSession presenter = getCurrentPresenter(roomId);
		MediaPipeline pipeline = liveBroadcastPipelineCache.get(roomId);
		if (presenter == null || presenter.getWebRtcEndpoint() == null) {
			JsonObject response = new JsonObject();
			response.addProperty("id", "viewerResponse");
			response.addProperty("response", "rejected");
			response.addProperty("message", "当前没有直播者,请稍后刷新页面");
			session.sendMessage(new TextMessage(response.toString()));
		} else {
			if (viewersUserSessionCache.containsKey(session.getId())) {
				JsonObject response = new JsonObject();
				response.addProperty("id", "viewerResponse");// 宸茬粡寤虹珛session,杩涜瑙傜湅
				response.addProperty("response", "rejected");
				response.addProperty("message",
						"您已经在其他浏览器进行观看,请先关闭之前的浏览器.");
				session.sendMessage(new TextMessage(response.toString()));
				return;
			}
			UserSession currentViewer = new UserSession(session, roomId, msg.getString("name"));// 首次进行观看，创建session
			viewersUserSessionCache.put(session.getId(), currentViewer);
			userRegistry.register(currentViewer);//注册

			String sdpOffer = msg.getString("sdpOffer");

			WebRtcEndpoint nextWebRtc = new WebRtcEndpoint.Builder(pipeline).build();

			nextWebRtc.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {
				@Override
				public void onEvent(OnIceCandidateEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate");
					response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response.toString()));
						}
					} catch (IOException e) {
						LOGGER.debug(e.getMessage());
					}
				}
			});

			currentViewer.setWebRtcEndpoint(nextWebRtc);
			presenter.getWebRtcEndpoint().connect(nextWebRtc);
			String sdpAnswer = nextWebRtc.processOffer(sdpOffer);

			JsonObject response = new JsonObject();
			response.addProperty("id", "viewerResponse");
			response.addProperty("response", "accepted");
			response.addProperty("sdpAnswer", sdpAnswer);

			synchronized (session) {
				currentViewer.sendMessage(response);
			}
			nextWebRtc.gatherCandidates();
			
			UserSession audioStarter = audioStarterUserSessionCache.get(roomId);
			if(audioStarter != null){//当前内容.
				final JsonObject newParticipantMsg = new JsonObject();
				newParticipantMsg.addProperty("id", "newParticipantArrived");
				newParticipantMsg.addProperty("name", audioStarter.getName());
				try {
					currentViewer.sendMessage(newParticipantMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		
		}
	}

	/*********
	 * 处理直播员初始化
	 * 
	 * @param msg
	 * @param session
	 * @throws IOException 
	 */
	private void doProcPresenterInit(JSONObject msg, final WebSocketSession session) throws IOException {
		LOGGER.info("---------------------------------有人进入直播-----------------------------------"); 
		String sessionId = session.getId();// 得到会话id
		MediaPipeline pipeline; // 流媒体
		UserSession presenter = presenterUserSessionCache.get(sessionId);

		if (presenter == null) {// 如果没有
			final String roomId = msg.getString("roomId");
			
			final Date starttime= new Date(msg.getLongValue("starttime"));
			
			UserSession old = getCurrentPresenter(roomId);// 如果以前的有，则删除以前的
			if (old != null) {
				closeRoom(roomId);
				try {
					//通知原本的直播者自己已经被踢出
					JsonObject response = new JsonObject();
					response.addProperty("id", "stopCommunication");
					response.addProperty("msg", "您的直播已经被别人占用");
					old.sendMessage(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			presenter = new UserSession(session,roomId, msg.getString("name"));// 用户会话
			userRegistry.register(presenter);// 注册
			presenterUserSessionCache.put(sessionId, presenter);// 将直播人员的会话加入到map中
			pipeline = client.createMediaPipeline();// 创建流媒体
			
			liveBroadcastPipelineCache.put(roomId, pipeline);// 流媒体map
			
			presenter.setWebRtcEndpoint(new WebRtcEndpoint.Builder(pipeline).build());
			
			KMSRoomDTO dto = roomDBService.find(Long.parseLong(roomId));
			if (dto == null) {
				throw new IOException("房间不存在！");
			}
			if (dto.getStatus() != null	&& dto.getStatus().intValue() == StatusEnum.closed.getValue()) {
				throw new IOException("房间已经关闭！");
			}
			
			//存储开始时间
			dto.setVideoStartTime(starttime);
			roomDBService.update(dto);
			
			String roomBaseDir=dto.getLivePath()+roomId+File.separator; //房间文件夹
			File file = new File(roomBaseDir);
			if(!file.exists()){
				file.mkdirs();		
			}
			file.setReadable(true, false);
			file.setWritable(true, false);
			roomBaseDir=file.getAbsolutePath()+File.separator;
			if (("WEBM").equals(VideoStyle)) {
				presenter.setRecorderEndpoint(new RecorderEndpoint.Builder(pipeline,
						"file:///"+roomBaseDir +File.separator+ roomId+"_"+df.format(new Date(System.currentTimeMillis()))+".webm").withMediaProfile(MediaProfileSpecType.WEBM).build());
				LOGGER.info("file:///"+roomBaseDir +File.separator+ roomId+".webm");
			}
			else {
				RecorderEndpoint recorderEndpoint =	new RecorderEndpoint.Builder(pipeline,
						"file:///"+roomBaseDir  +File.separator+ roomId+"_"+df.format(new Date(System.currentTimeMillis()))+".mp4")
						.withMediaProfile(MediaProfileSpecType.MP4).build();
				recorderEndpoint.setOutputBitrate(700,new Continuation<Void>(){
					@Override
					public void onSuccess(Void result) throws Exception {
						
					}
					@Override
					public void onError(Throwable cause) throws Exception {
						
					}
				});
				presenter.setRecorderEndpoint(recorderEndpoint);
				LOGGER.info("file:///"+roomBaseDir + roomId+".mp4");
			}
			presenter.getWebRtcEndpoint().connect(presenter.getRecorderEndpoint(),MediaType.AUDIO);
			presenter.getWebRtcEndpoint().connect(presenter.getRecorderEndpoint(),MediaType.VIDEO);
			presenter.getRecorderEndpoint().record();

			//
			WebRtcEndpoint presenterWebRtc = presenter.getWebRtcEndpoint();
			// ICEcanditate
			presenterWebRtc.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {
						@Override
						public void onEvent(OnIceCandidateEvent event) {
							JsonObject response = new JsonObject();
							response.addProperty("id", "iceCandidate");
							response.add("candidate", JsonUtils
									.toJsonObject(event.getCandidate()));
							response.addProperty("roomId",roomId);

							try {
								synchronized (session) {
									session.sendMessage(new TextMessage(
											response.toString()));
								}
							} catch (IOException e) {
								LOGGER.debug(e.getMessage());
							}
						}
			});

			// SDP
			String sdpOffer = msg.getString("sdpOffer");
			String sdpAnswer = presenterWebRtc.processOffer(sdpOffer);
			roomDBService.updateStatus(Long.parseLong(presenter.getRoomId()),StatusEnum.online);
			JsonObject response = new JsonObject();
			response.addProperty("id", "presenterResponse");
			response.addProperty("response", "accepted");
			response.addProperty("sdpAnswer", sdpAnswer);
			response.addProperty("roomId", String.valueOf(roomId));

			synchronized (session) {
				presenter.sendMessage(response);
			}
			presenterWebRtc.gatherCandidates();
		} else {
			JsonObject response = new JsonObject();
			response.addProperty("id", "presenterResponse");
			response.addProperty("response", "rejected");
			response.addProperty("message",	"Another user is currently acting as sender. Try again later ...");
			session.sendMessage(new TextMessage(response.toString()));
		}
	}

	private UserSession getCurrentPresenter(String roomId) {
		UserSession preseter = null;
		if (presenterUserSessionCache != null) {
			Set<String> keyset = presenterUserSessionCache.keySet();
			UserSession temp = null;
			for (String key : keyset) {
				temp = presenterUserSessionCache.get(key);
				if (temp.getRoomId().equals(roomId)) {
					preseter = temp;
				}
			}
		}
		return preseter;
	}

	/**
	 * 关闭一个房间
	 * 
	 * @param liveId
	 */
	public void closeRoom(String liveId) {
		UserSession session = getCurrentPresenter(liveId);
		if (session != null) {
			try {
				doStopLive(session.getSession(), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 停止直播
	 * 
	 * @param session
	 * @param b
	 * @throws IOException
	 */
	private void doStopLive(WebSocketSession session, boolean removeRoom)
			throws IOException {
		String sessionId = session.getId();
		UserSession presenter = presenterUserSessionCache.get(sessionId);
		if (presenter != null
				&& presenter.getSession().getId().equals(sessionId)) {
			MediaPipeline pipeline = liveBroadcastPipelineCache.get(presenter
					.getRoomId());
			for (UserSession viewer : viewersUserSessionCache.values()) {
				JsonObject response = new JsonObject();
				response.addProperty("id", "stopCommunication");
				viewer.sendMessage(response);
			}
			LOGGER.info("Releasing media pipeline");
			if (pipeline != null) {
				pipeline.release();
			}
			if (removeRoom) {
				roomDBService.updateStatus(
						Long.parseLong(presenter.getRoomId()),
						StatusEnum.closed);
			}
			presenterUserSessionCache.remove(sessionId);
			pipeline = null;
			presenter = null;

		} else if (viewersUserSessionCache.containsKey(sessionId)) {
			if (viewersUserSessionCache.get(sessionId).getWebRtcEndpoint() != null) {
				viewersUserSessionCache.get(sessionId).getWebRtcEndpoint()
						.release();
			}
			viewersUserSessionCache.remove(sessionId);
		}
	}	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			org.springframework.web.socket.CloseStatus status) throws Exception {
		try {
			doProcStopAudio(new JSONObject(), session);			
		} catch (Exception e) {
			LOGGER.error("停止语音",e);
		}
		try{
			doStopLive(session, false);
		}catch (Exception e){
			LOGGER.error("停止视频",e);
		}
		
		///停止直播
	}

	public int countUsers(String roomId) {
		int count = 0;
		if (this.getCurrentPresenter(roomId) != null) {
			count++;
		}
		for (UserSession viewer : viewersUserSessionCache.values()) {
			if (viewer.getRoomId().equals(roomId)) {
				count++;
			}
		}
		return count;
	}
}
