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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.Continuation;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.MediaType;
import org.kurento.client.OnIceCandidateEvent;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;
import com.zhixin.dto.kms.KMSRoomDTO;

/**
 *
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class Many2ManyUserSession implements Closeable {

	private static final Logger log = LoggerFactory
			.getLogger(Many2ManyUserSession.class);

	private final String name;
	private final WebSocketSession session;

	private final MediaPipeline pipeline;

	private final String roomName;
	private final String roomId;
	private final String filename;
	private final int call_type;
	private final WebRtcEndpoint outgoingMedia;
	private final RecorderEndpoint recorderMedia;
	private final ConcurrentMap<String, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();
	
	public Many2ManyUserSession(final String name, String roomName,
			String roomId, String filename, int call_type,
			final WebSocketSession session, MediaPipeline pipeline,String VideoStyle) {

		this.pipeline = pipeline;
		this.name = name;
		this.roomId = roomId;
		this.filename = filename;
		this.session = session;
		this.roomName = roomName;
		this.call_type = call_type;
		this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
		
		
		if (call_type == 1) {// 如果是视频会话
			if (("WEBM").equals(VideoStyle)) {
				this.recorderMedia = new RecorderEndpoint.Builder(pipeline,
						filename).withMediaProfile(MediaProfileSpecType.WEBM)
						.build();
			}
			else
			{
				this.recorderMedia = new RecorderEndpoint.Builder(pipeline,
						filename).withMediaProfile(MediaProfileSpecType.MP4)
						.build();
			}
			outgoingMedia.connect(recorderMedia, MediaType.AUDIO);
			outgoingMedia.connect(recorderMedia, MediaType.VIDEO);
		} else {//只存音频
			
			if (("WEBM").equals(VideoStyle)) {
				this.recorderMedia = new RecorderEndpoint.Builder(pipeline,
						filename).withMediaProfile(MediaProfileSpecType.WEBM_AUDIO_ONLY)
						.build();
			}
			else
			{
				this.recorderMedia = new RecorderEndpoint.Builder(pipeline,
						filename).withMediaProfile(MediaProfileSpecType.MP4_VIDEO_ONLY)
						.build();
			}
			outgoingMedia.connect(recorderMedia, MediaType.AUDIO);
			//outgoingMedia.connect(recorderMedia, MediaType.VIDEO);
		}
		recorderMedia.record();
		this.outgoingMedia
				.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {

					@Override
					public void onEvent(OnIceCandidateEvent event) {
						JsonObject response = new JsonObject();
						response.addProperty("id", "iceCandidate");
						response.addProperty("name", name);
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
	}

	public WebRtcEndpoint getOutgoingWebRtcPeer() {
		return outgoingMedia;
	}

	public String getName() {
		return name;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public String getfilepath() {
		return filename;
	}
	public Integer getCall_type()
	{
		return call_type;
	}
	/**
	 * The room to which the user is currently attending.
	 *
	 * @return The room
	 */
	public String getRoomName() {
		return this.roomName;
	}

	public String getRoomId() {
		return this.roomId;
	}

	/*
	 * 接收视频流
	 */
	public void receiveVideoFrom(Many2ManyUserSession sender, String sdpOffer)
			throws IOException {
		if (sender == null) {
			log.error("出现错误,无法找到对应的发送者会话信息");
			return;
		}
		log.info("USER {}: connecting with {} in room {}", this.name,
				sender.getName(), this.roomName);

		log.trace("USER {}: SdpOffer for {} is {}", this.name,
				sender.getName(), sdpOffer);

		final String ipSdpAnswer = this.getEndpointForUser(sender)
				.processOffer(sdpOffer);
		final JsonObject scParams = new JsonObject();
		scParams.addProperty("id", "receiveVideoAnswer");
		scParams.addProperty("name", sender.getName());
		scParams.addProperty("sdpAnswer", ipSdpAnswer);

		log.trace("USER {}: SdpAnswer for {} is {}", this.name,
				sender.getName(), ipSdpAnswer);
		this.sendMessage(scParams);
		log.debug("gather candidates");
		this.getEndpointForUser(sender).gatherCandidates();
	}

	/*
	 * 通过会话找到其webrtcendpoint,如果会话不在列表中则新建webrtcendpoint并加入列表
	 */
	private WebRtcEndpoint getEndpointForUser(final Many2ManyUserSession sender) {
		if (sender.getName().equals(name)) {
			log.debug("PARTICIPANT {}: configuring loopback", this.name);
			return outgoingMedia;
		}

		log.debug("PARTICIPANT {}: receiving video from {}", this.name,
				sender.getName());

		WebRtcEndpoint incoming = incomingMedia.get(sender.getName());
		if (incoming == null) {
			log.debug("PARTICIPANT {}: creating new endpoint for {}",
					this.name, sender.getName());
			incoming = new WebRtcEndpoint.Builder(pipeline).build();

			incoming.addOnIceCandidateListener(new EventListener<OnIceCandidateEvent>() {

				@Override
				public void onEvent(OnIceCandidateEvent event) {
					JsonObject response = new JsonObject();
					response.addProperty("id", "iceCandidate");
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

			incomingMedia.put(sender.getName(), incoming);
		}

		log.debug("PARTICIPANT {}: obtained endpoint for {}", this.name,
				sender.getName());
		//根据不同的方式选择是否传递视频流
		//sender.getOutgoingWebRtcPeer().connect(incoming, MediaType.AUDIO);
		if (call_type==1) {
			sender.getOutgoingWebRtcPeer().connect(incoming);
		}
		else
		{
			sender.getOutgoingWebRtcPeer().connect(incoming, MediaType.AUDIO);
		}
		return incoming;
	}

	/*
	 * 将某个参与者从本列表中移除（会话）
	 */
	public void cancelVideoFrom(final Many2ManyUserSession sender) {
		this.cancelVideoFrom(sender.getName());
	}

	/*
	 * 将某个参与者从本列表中移除（名字）
	 */
	public void cancelVideoFrom(final String senderName) {
		log.debug("PARTICIPANT {}: canceling video reception from {}",
				this.name, senderName);
		final WebRtcEndpoint incoming = incomingMedia.remove(senderName);

		log.debug("PARTICIPANT {}: removing endpoint for {}", this.name,
				senderName);
		incoming.release(new Continuation<Void>() {
			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace(
						"PARTICIPANT {}: Released successfully incoming EP for {}",
						Many2ManyUserSession.this.name, senderName);
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn(
						"PARTICIPANT {}: Could not release incoming EP for {}",
						Many2ManyUserSession.this.name, senderName);
			}
		});
	}

	/*
	 * 关闭并释放资源
	 */
	@Override
	public void close() throws IOException {
		log.debug("PARTICIPANT {}: Releasing resources", this.name);
		for (final String remoteParticipantName : incomingMedia.keySet()) {

			log.trace("PARTICIPANT {}: Released incoming EP for {}", this.name,
					remoteParticipantName);

			final WebRtcEndpoint ep = this.incomingMedia
					.get(remoteParticipantName);

			ep.release(new Continuation<Void>() {

				@Override
				public void onSuccess(Void result) throws Exception {
					log.trace(
							"PARTICIPANT {}: Released successfully incoming EP for {}",
							Many2ManyUserSession.this.name,
							remoteParticipantName);
				}

				@Override
				public void onError(Throwable cause) throws Exception {
					log.warn(
							"PARTICIPANT {}: Could not release incoming EP for {}",
							Many2ManyUserSession.this.name,
							remoteParticipantName);
				}
			});
		}

		outgoingMedia.release(new Continuation<Void>() {

			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace("PARTICIPANT {}: Released outgoing EP",
						Many2ManyUserSession.this.name);
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn("USER {}: Could not release outgoing EP",
						Many2ManyUserSession.this.name);
			}
		});
	}
	/*
	 * 发送消息
	 */
	//
	public void sendMessage(JsonObject message) throws IOException {
		log.debug("USER {}: Sending message {}", name, message);
		synchronized (session) {
			try {
				session.sendMessage(new TextMessage(message.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * 添加candidate
	 */
	public void addCandidate(IceCandidate candidate, String name) {
		if (this.name.compareTo(name) == 0) {
			outgoingMedia.addIceCandidate(candidate);
		} else {
			WebRtcEndpoint webRtcendpoint = incomingMedia.get(name);
			if (webRtcendpoint != null) {
				webRtcendpoint.addIceCandidate(candidate);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Many2ManyUserSession)) {
			return false;
		}
		Many2ManyUserSession other = (Many2ManyUserSession) obj;
		boolean eq = name.equals(other.name);
		eq &= roomName.equals(other.roomName);
		return eq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + name.hashCode();
		result = 31 * result + roomName.hashCode();
		return result;
	}
}
