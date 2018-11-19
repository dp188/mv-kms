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

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaType;
import org.kurento.client.OnIceCandidateEvent;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

/**
 * User session.
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
public class UserSession {
	private static final Logger log = LoggerFactory.getLogger(UserSession.class);// 日志
	private String roomId;// 房间ID
	private String roomName;//
	private String name;//用户名字
	private final WebSocketSession session;// 会话
	private WebRtcEndpoint webRtcEndpoint;
	private RecorderEndpoint recorderEndpoint;
	private ConcurrentMap<String, WebRtcEndpoint> incomingEndpoint = new ConcurrentHashMap<>();
	
	
	////音频
	private MediaPipeline audioPipeline;	
	private WebRtcEndpoint webRtcEndpointAudio=null;
	private RecorderEndpoint recorderEndpointAudio=null;
	private ConcurrentMap<String, WebRtcEndpoint> incomingEndpointAudio = new ConcurrentHashMap<>();

	
	public UserSession(WebSocketSession session, String roomId,String name) {
		this.session = session;
		this.roomId = roomId;
		this.name=name;
		this.roomName="房间"+roomId;
	}

	//////////////////////音频

	public void addcandatelitenertoEndpoint_Video(){
		if (webRtcEndpointAudio!=null) {
			webRtcEndpointAudio.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {
				@Override
				public void onEvent(OnIceCandidateEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate_Audio");
					response.addProperty("name", name);
					response.add("candidate",JsonUtils.toJsonObject(event.getCandidate()));
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response
									.toString()));
						}
					} catch (IOException e) {
						log.debug(e.getMessage());
					}
				}
			});
		}
		
	}
	
	//////
	public void receiveVideoFrom(UserSession sender, String sdpOffer)
			throws IOException {
		if (sender == null) {
			log.error("出现错误,无法找到对应的发送者会话信息");
			return;
		}
		log.info("USER {}: connecting with {} in room {}", this.name,
				sender.getName(), this.roomName);
		log.trace("USER {}: SdpOffer for {} is {}", this.name,
				sender.getName(), sdpOffer);
		
		
		
		WebRtcEndpoint senderEndpoint = getEndpoint_AudioForUser(sender);//查找并创建
		if (senderEndpoint!=null) {
			final String ipSdpAnswer = senderEndpoint.processOffer(sdpOffer);
			final JsonObject scParams = new JsonObject();
			scParams.addProperty("id", "receiveVideoAnswer");
			scParams.addProperty("name", sender.getName());
			scParams.addProperty("sdpAnswer", ipSdpAnswer);
			log.trace("USER {}: SdpAnswer for {} is {}", this.name,
					sender.getName(), ipSdpAnswer);
			this.sendMessage(scParams);
			log.debug("gather candidates");
			senderEndpoint.gatherCandidates();
		}
	}
	
	
	private WebRtcEndpoint getEndpoint_AudioForUser(final UserSession sender) {
		if (sender.getName().equals(name)) {
			log.debug("PARTICIPANT {}: configuring loopback", this.name);
			return webRtcEndpointAudio;
		}

		log.debug("PARTICIPANT {}: receiving video from {}", this.name,
				sender.getName());

		WebRtcEndpoint incoming = incomingEndpointAudio.get(sender.getName());
		if (incoming == null) {
			log.debug("PARTICIPANT {}: creating new endpoint for {}",
					this.name, sender.getName());
			incoming = new WebRtcEndpoint.Builder(audioPipeline).build();

			incoming.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {

				@Override
				public void onEvent(OnIceCandidateEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate_Audio");
					response.addProperty("name", sender.getName());
					response.add("candidate",
							JsonUtils.toJsonObject(event.getCandidate()));
					try {
						synchronized (session) {
							session.sendMessage(new TextMessage(response
									.toString()));
						}
					} catch (IOException e) {
						log.debug(e.getMessage());
					}
				}
			});

			incomingEndpointAudio.put(sender.getName(), incoming);
		}

		log.debug("PARTICIPANT {}: obtained endpoint for {}", this.name,
				sender.getName());
		sender.getWebRtcEndpointAudio().connect(incoming, MediaType.AUDIO);
		sender.getWebRtcEndpointAudio().connect(incoming, MediaType.VIDEO);
		return incoming;
	}
	//////////////////////////////
	
	public ConcurrentMap<String, WebRtcEndpoint> getIncomingEndpoint() {
		return incomingEndpoint;
	}

	public void setIncomingEndpoint(
			ConcurrentMap<String, WebRtcEndpoint> incomingEndpoint) {
		this.incomingEndpoint = incomingEndpoint;
	}

	public WebRtcEndpoint getWebRtcEndpointAudio() {
		return webRtcEndpointAudio;
	}

	public void setWebRtcEndpointAudio(WebRtcEndpoint webRtcEndpointAudio) {
		this.webRtcEndpointAudio = webRtcEndpointAudio;
	}

	public RecorderEndpoint getRecorderEndpointAudio() {
		return recorderEndpointAudio;
	}

	public void setRecorderEndpointAudio(RecorderEndpoint recorderEndpointAudio) {
		this.recorderEndpointAudio = recorderEndpointAudio;
	}

	public ConcurrentMap<String, WebRtcEndpoint> getIncomingEndpointAudio() {
		return incomingEndpointAudio;
	}

	public void setIncomingEndpointAudio(
			ConcurrentMap<String, WebRtcEndpoint> incomingEndpointAudio) {
		this.incomingEndpointAudio = incomingEndpointAudio;
	}

	public String getName () {
		return name;
	}
	public WebSocketSession getSession() {
		return session;
	}

	public void sendMessage(JsonObject response) throws IOException {
		log.debug("Sending message from user with session Id '{}': {}", session.getId(), response);
		session.sendMessage(new TextMessage(response.toString()));
	}

	public WebRtcEndpoint getWebRtcEndpoint() {
		return webRtcEndpoint;
	}

	public void setWebRtcEndpoint(WebRtcEndpoint webRtcEndpoint) {
		this.webRtcEndpoint = webRtcEndpoint;
	}

	public RecorderEndpoint getRecorderEndpoint() {
		return recorderEndpoint;
	}


	
	public void setRecorderEndpoint(RecorderEndpoint recorderEndpoint) {
		this.recorderEndpoint = recorderEndpoint;
	}

	public void addCandidate(IceCandidate i) {
		if(webRtcEndpoint == null){
			log.error("输出webSorkt 为空"); 
		}
		webRtcEndpoint.addIceCandidate(i);
	}
	
	public void addCandidate_Audio(IceCandidate candidate, String name) {

		if (this.name.compareTo(name) == 0) {
			this.getWebRtcEndpointAudio().addIceCandidate(candidate);
		} else {
			WebRtcEndpoint webRtcendpoint = incomingEndpointAudio.get(name);
			if (webRtcendpoint != null) {
				webRtcendpoint.addIceCandidate(candidate);
			}
		}
		
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	

	
	public MediaPipeline getAudioPipeline() {
		return audioPipeline;
	}

	public void setAudioPipeline(MediaPipeline audioPipeline) {
		this.audioPipeline = audioPipeline;
	}

	public ConcurrentMap<String, WebRtcEndpoint> getIncomingMedia() {
		return incomingEndpoint;
	}

	public void setIncomingMedia(ConcurrentMap<String, WebRtcEndpoint> incomingMedia) {
		this.incomingEndpoint = incomingMedia;
	}
		
}
