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
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.zhixin.core.enums.StatusEnum;
import com.zhixin.dto.kms.KMSMany2ManyDTO;
import com.zhixin.service.kms.IKMSMany2ManyRoomService;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class Many2ManyRoomManager {

	private final Logger log = LoggerFactory.getLogger(Many2ManyRoomManager.class);

	@Autowired
	private KurentoClient kurento;

	@Autowired
	private IKMSMany2ManyRoomService kmsMany2ManyRoomService;// KMS多对多房间服务

	private final ConcurrentMap<String, Many2ManyRoom> rooms = new ConcurrentHashMap<>();

	/**
	 * Looks for a room in the active room list.
	 *
	 */
	public KMSMany2ManyDTO getRoomDto(String roomId) {
		log.debug("Searching for room by ID {}", roomId);
		KMSMany2ManyDTO dto = kmsMany2ManyRoomService.find(Long.parseLong(roomId));
		if (dto == null) {
			return null;
		} else {
			return dto;
		}
	}

	public void addRoom(String roomId, Many2ManyRoom room) {
		rooms.put(roomId, room);
	}

	/**
	 * Looks for a room in the active room list.
	 *
	 */
	public Many2ManyRoom getRoom(String roomId) {
		log.debug("Searching for room by ID {}", roomId);
		Many2ManyRoom room = rooms.get(roomId);
		if (room == null) {
			return null;
		} else {
			return room;
		}
	}

	/**
	 *
	 * 创建房间
	 */
	public Many2ManyRoom CreateRoom(int call_type, String liveName, String livePath, String remark) {
		Many2ManyRoom room = null;
		try {
			log.debug("create Room now!");
			KMSMany2ManyDTO many2ManyDTO = new KMSMany2ManyDTO();// 定义一个房间
			many2ManyDTO.setCall_type(call_type);
			many2ManyDTO.setLivePath(livePath);
			many2ManyDTO.setName(liveName);
			many2ManyDTO.setRemark(remark);
			Long id = kmsMany2ManyRoomService.create(many2ManyDTO);// 创建房间（数据库中）
			room = new Many2ManyRoom(many2ManyDTO, kurento.createMediaPipeline());// 创建房间
			room.setRoomID(id);
			rooms.put(id.toString(), room);
			return room;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建房间失败");
			return null;
			// TODO: handle exception
		}
	}

	/**
	 * Removes a room from the list of available rooms.
	 *
	 * @param room
	 *            the room to be removed
	 */
	public void closeRoom(Many2ManyRoom room) {
		if (room != null) {
			this.closeRoom(room.getRoomID().toString());
		}
	}

	/**
	 * 
	 * @param roomId
	 */
	public void closeRoom(String roomId) {
		Many2ManyRoom room = this.rooms.remove(roomId);
		if (room != null) {
			Collection<Many2ManyUserSession> participants = room.getParticipants();
			if (participants != null) {
				final JsonObject participantLeftJson = new JsonObject();
			    participantLeftJson.addProperty("id", "closeRoom");
			    participantLeftJson.addProperty("name", "closeRoom");
				for (Many2ManyUserSession session : participants) {
					try {
						session.sendMessage(participantLeftJson);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
			room.close();//告知每个人房间将关闭
			kmsMany2ManyRoomService.updateStatus(Long.parseLong(roomId), StatusEnum.closed);
			log.info("Room {} removed and closed", room.getName());
		}


	}

	public Integer countUsers(String roomId) {
		Many2ManyRoom room = this.rooms.get(roomId);
		if(room != null){
			if(room.getParticipants() != null){
				return room.getParticipants().size();
			}
		}
		return 0;
	}
}
