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

import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zhixin.core.enums.StatusEnum;
import com.zhixin.dto.kms.KMSMany2ManyDTO;
import com.zhixin.service.kms.IKMSMany2ManyRoomService;

/**
 * Protocol handler for 1 to N video call communication.
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
public class Many2ManyCallHandler extends TextWebSocketHandler {
	private static final Many2ManyCallHandler handler = new Many2ManyCallHandler();

	private static final Logger log = LoggerFactory
			.getLogger(Many2ManyCallHandler.class);
	private static final Gson gson = new GsonBuilder().create();

	@Autowired
	private Many2ManyRoomManager manytomanyroomManager;

	@Autowired
	private Many2ManyUserRegistry manytomanyregistry;
	
	@Autowired
	private KurentoClient kurento;

	@Autowired
  	private IKMSMany2ManyRoomService kmsMany2ManyRoomService;//KMS多对多房间服务

	@Value("${VideoStyle}")
	private String videoStyle;
	public static Many2ManyCallHandler getCallHandler() {
		return handler;
	}

	public Many2ManyRoomManager getmanytomanyroomManager() {
		return manytomanyroomManager;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {
		final JsonObject jsonMessage = gson.fromJson(message.getPayload(),JsonObject.class);

		final Many2ManyUserSession user = manytomanyregistry.getBySession(session);

		if (user != null) {
			log.debug("Incoming message from user '{}': {}", user.getName(),jsonMessage);
		} else {
			log.debug("Incoming message from new user: {}", jsonMessage);
		}

		switch (jsonMessage.get("id").getAsString()) {
		case "joinRoom":
			joinRoom(jsonMessage, session);
			break;
		case "receiveVideoFrom":
			final String senderName = jsonMessage.get("sender").getAsString();
			final Many2ManyUserSession sender = manytomanyregistry.getByName(senderName);
			final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
			user.receiveVideoFrom(sender, sdpOffer);
			break;
		case "leaveRoom":
			leaveRoom(user);
			break;
		case "onIceCandidate":
			JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();
			if (user != null) {
				IceCandidate cand = new IceCandidate(candidate.get("candidate")
						.getAsString(), candidate.get("sdpMid").getAsString(),
						candidate.get("sdpMLineIndex").getAsInt());
				user.addCandidate(cand, jsonMessage.get("name").getAsString());
			}
			break;
		default:
			session.sendMessage(message);
			break;
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		Many2ManyUserSession user = manytomanyregistry.removeBySession(session);
		if (user != null) {
			Many2ManyRoom room = manytomanyroomManager.getRoom(user.getRoomId());
			if (room != null) {
				leaveRoom(user);
				//room.leave(user);
			}
		}
	}

	/*************
	 * 加入房间
	 * @param params
	 * @param session
	 * @throws IOException
	 */
	private synchronized void joinRoom(JsonObject params, WebSocketSession session)
			throws IOException {
		final String roomName = params.get("roomName").getAsString();
		final String roomId = params.get("roomId").getAsString();
		final String name = params.get("name").getAsString();
		log.info("PARTICIPANT {}: trying to join room {}", name, roomName);

		Many2ManyRoom room = manytomanyroomManager.getRoom(roomId);
		if (room == null) {
			KMSMany2ManyDTO dto =	this.kmsMany2ManyRoomService.find(Long.parseLong(roomId));
			if(dto != null){
			    room =  new Many2ManyRoom(dto, kurento.createMediaPipeline());//创建房间
			    manytomanyroomManager.addRoom(roomId, room);
			}else{
				log.info("room {} is not exist", roomId);
				JsonObject message = new JsonObject();
				message.addProperty("id", "roomisnull");
				message.addProperty("message", "room Id is not exist");
				session.sendMessage(new TextMessage(message.toString()));
				return;
			} 
		}
		this.kmsMany2ManyRoomService.updateStatus(Long.parseLong(roomId), StatusEnum.online);
		final Many2ManyUserSession user = room.join(name, session,videoStyle);
		manytomanyregistry.register(user);
	}

	private synchronized void leaveRoom(Many2ManyUserSession user) throws IOException {
		final Many2ManyRoom room = manytomanyroomManager.getRoom(user.getRoomId());
		if(room != null){
			room.leave(user);
			if (room.getParticipants().isEmpty()) {
				manytomanyroomManager.closeRoom(room);
			}
		}		
	}
	
	/*****
	 * 更新字幕
	 * @param user
	 * @param text
	 */
	public void updateCaption(Many2ManyUserSession sender ,String text){
		if(sender!=null){
			Many2ManyRoom room = manytomanyroomManager.getRoom(sender.getRoomId());
			room.sendCaptionData(sender, text, false);
		}
	}
}
