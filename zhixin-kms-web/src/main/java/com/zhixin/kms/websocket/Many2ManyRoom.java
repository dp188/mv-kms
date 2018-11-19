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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PreDestroy;

import org.kurento.client.Continuation;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.RecorderEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.zhixin.dto.kms.KMSMany2ManyDTO;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class Many2ManyRoom implements Closeable {
	private final Logger log = LoggerFactory.getLogger(Many2ManyRoom.class);

	private final ConcurrentMap<String, Many2ManyUserSession> participants = new ConcurrentHashMap<>();
	private final MediaPipeline pipeline;
	private KMSMany2ManyDTO many2manydto;

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-S");
	
	
	public String getName() {
		return many2manydto.getName();
	}

	public KMSMany2ManyDTO getMany2ManyDTO() {
		return many2manydto;
	}

	public Long getRoomID() {
		return many2manydto.getId();
	}

	public void setRoomID(Long Id) {
		many2manydto.setId(Id);
	}

	public String getRoomName() {
		return many2manydto.getName();
	}

	public void setRoomName(String name) {
		many2manydto.setName(name);
	}

	public int getCall_Type() {
		return many2manydto.getCall_type();
	}

	public void setCall_Type(int call_type) {
		many2manydto.setCall_type(call_type);
	}

	public String getLivePath() {
		return many2manydto.getLivePath();
	}

	public void setLivePath(String path) {
		many2manydto.setLivePath(path);
	}

	public Many2ManyRoom(KMSMany2ManyDTO many2manydto, MediaPipeline pipeline) {
		this.many2manydto = many2manydto;
		this.pipeline = pipeline;
		log.info("ROOM {} has been created", many2manydto.getId());
	}

	@PreDestroy
	private void shutdown() {
		this.close();
	}

	/*
	 * 加入会话
	 */
	public Many2ManyUserSession join(String userName, WebSocketSession session,String VideoStyle) throws IOException {
		log.info("ROOM {}: adding participant {}", userName, userName);
		final Integer num = participants.values().size();
		String filename;
		String FilePath = getLivePath() + getRoomID();
		File file = new File(FilePath);
		if (file.exists()) {
			file.mkdirs();
		}
		file.setReadable(true, false);
		file.setWritable(true, false);
		
		FilePath = file.getAbsolutePath() + File.separator;
		if (VideoStyle == "WEBM") {
			filename="file:///"+FilePath+File.separator+getRoomID()+ df.format(new Date()) + "_" + num.toString()
			+".webm";
		}
		else{
			filename="file:///"+FilePath+File.separator+getRoomID()+ df.format(new Date()) + "_" + num.toString()
			+".mp4";
		}
		final Many2ManyUserSession participant = new Many2ManyUserSession(userName, getRoomName(),
				getRoomID().toString(), filename, getCall_Type(), session, this.pipeline,VideoStyle);
		broadcastnewcoming(participant);// 告知其他已经在房间的用户有新人
		participants.put(participant.getName(), participant);// 将用户会话加入列表
		sendParticipantNames(participant);// 将已经存在的参与者发送给新来的用户
		return participant;
	}

	/*
	 * 离开房间
	 */
	public void leave(Many2ManyUserSession user) throws IOException {
		log.debug("PARTICIPANT {}: Leaving room {}", user.getName(), this.many2manydto.getName());
		this.removeParticipant(user.getName());
		user.close();
	}

	/*
	 * 告知其他用户有人加入房间
	 */
	private Collection<String> broadcastnewcoming(Many2ManyUserSession newParticipant) throws IOException {
		final JsonObject newParticipantMsg = new JsonObject();
		newParticipantMsg.addProperty("id", "newParticipantArrived");
		newParticipantMsg.addProperty("name", newParticipant.getName());

		final List<String> participantsList = new ArrayList<>(participants.values().size());
		log.debug("ROOM {}: notifying other participants of new participant {}", this.many2manydto.getName(),
				newParticipant.getName());

		for (final Many2ManyUserSession participant : participants.values()) {
			try {
				participant.sendMessage(newParticipantMsg);
			} catch (final IOException e) {
				log.debug("ROOM {}: participant {} could not be notified", this.many2manydto.getName(),
						participant.getName(), e);
			}
			participantsList.add(participant.getName());
		}

		return participantsList;
	}

	/*
	 * 将某个参与者移除并告知其他参与者
	 */
	private void removeParticipant(String name) throws IOException {
		participants.remove(name);

		log.debug("ROOM {}: notifying all users that {} is leaving the room", this.many2manydto.getName(), name);

		final List<String> unnotifiedParticipants = new ArrayList<>();
		final JsonObject participantLeftJson = new JsonObject();
		participantLeftJson.addProperty("id", "participantLeft");
		participantLeftJson.addProperty("name", name);
		for (final Many2ManyUserSession participant : participants.values()) {
			try {
				participant.cancelVideoFrom(name);
				participant.sendMessage(participantLeftJson);
			} catch (final IOException e) {
				unnotifiedParticipants.add(participant.getName());
			}
		}

		if (!unnotifiedParticipants.isEmpty()) {
			log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.many2manydto.getName(),
					unnotifiedParticipants, name);
		}

	}

	/*
	 * 将已经存在的参与者发送给用户
	 */
	public void sendParticipantNames(Many2ManyUserSession user) throws IOException {

		final JsonArray participantsArray = new JsonArray();
		for (final Many2ManyUserSession participant : this.getParticipants()) {
			if (!participant.equals(user)) {
				final JsonElement participantName = new JsonPrimitive(participant.getName());
				participantsArray.add(participantName);
			}
		}

		final JsonObject existingParticipantsMsg = new JsonObject();
		existingParticipantsMsg.addProperty("id", "existingParticipants");
		existingParticipantsMsg.add("data", participantsArray);
		log.debug("PARTICIPANT {}: sending a list of {} participants", user.getName(), participantsArray.size());
		user.sendMessage(existingParticipantsMsg);
	}

	/**********
	 * 发送字幕数据
	 * 
	 * @param user
	 * @param text
	 */
	public void sendCaptionData(Many2ManyUserSession user, String text, boolean append) {
		final List<String> unnotifiedParticipants = new ArrayList<>();
		final JsonObject captionData = new JsonObject();
		captionData.addProperty("id", "updateCaption");
		captionData.addProperty("name", user.getName());
		captionData.addProperty("text", text);
		captionData.addProperty("append", append);
		for (final Many2ManyUserSession participant : participants.values()) {
			try {
				participant.sendMessage(captionData);
			} catch (final IOException e) {
				unnotifiedParticipants.add(participant.getName());
			}
		}

		if (!unnotifiedParticipants.isEmpty()) {
			log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.many2manydto.getName(),
					unnotifiedParticipants);
		}
	}

	/*
	 * 得到所有用户的Session
	 */
	public Collection<Many2ManyUserSession> getParticipants() {
		return participants.values();
	}

	/*
	 * 根据客户名字得到Session
	 */
	public Many2ManyUserSession getParticipant(String name) {
		return participants.get(name);
	}

	@Override
	public void close() {
		for (final Many2ManyUserSession user : participants.values()) {
			try {
				user.close();
			} catch (IOException e) {
				log.debug("ROOM {}: Could not invoke close on participant {}", this.many2manydto.getName(),
						user.getName(), e);
			}
		}
		// 清除房间参与者列表
		participants.clear();

		pipeline.release(new Continuation<Void>() {

			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace("ROOM {}: Released Pipeline", Many2ManyRoom.this.many2manydto.getName());
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn("PARTICIPANT {}: Could not release Pipeline", Many2ManyRoom.this.many2manydto.getName());
			}
		});

		log.debug("Room {} closed", this.many2manydto.getName());
	}

}
