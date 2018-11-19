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

import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Video call 1 to N demo (main).
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.0
 */
@Configuration
@EnableWebSocket
@Service
public class KMSWebSocketApp implements WebSocketConfigurer {
	@Value("${kurento.ws.url}")
	private String DEFAULT_KMS_WS_URI;
	
	@Bean
	public Many2ManyUserRegistry manytomanyregistry() {
	   return new Many2ManyUserRegistry();
	}

	@Bean
	public Many2ManyRoomManager manytomanyroomManager() {
	   return new Many2ManyRoomManager();
	}
	
	@Bean
	public Many2ManyCallHandler many2ManycallHandler() {
		return Many2ManyCallHandler.getCallHandler();
	}
	
	@Bean
	public One2ManyCallHandler one2ManycallHandler() {
		return One2ManyCallHandler.getCallHandler();
	}
	@Bean
	public UserRegistry userRegistry(){
		return new UserRegistry();
	}
	
	@Bean
	public KurentoClient kurentoClient() {
		return KurentoClient.create(System.getProperty("kms.ws.uri",DEFAULT_KMS_WS_URI));
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(one2ManycallHandler(), "/one2Many");
		registry.addHandler(many2ManycallHandler(), "/many2Many");
	}
  
}
