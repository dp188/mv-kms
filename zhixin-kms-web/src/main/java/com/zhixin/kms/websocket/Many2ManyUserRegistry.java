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

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

/**
 * Map of users registered in the system. This class has a concurrent hash map to store users, using
 * its name as key in the map.
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @authos Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class Many2ManyUserRegistry {

  private final ConcurrentHashMap<String, Many2ManyUserSession> usersByName = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Many2ManyUserSession> usersBySessionId = new ConcurrentHashMap<>();

  public void register(Many2ManyUserSession user) {
    usersByName.put(user.getName(), user);
    usersBySessionId.put(user.getSession().getId(), user);
  }

  public Many2ManyUserSession getByName(String name) {
    return usersByName.get(name);
  }

  public Many2ManyUserSession getBySession(WebSocketSession session) {
    return usersBySessionId.get(session.getId());
  }

  public boolean exists(String name) {
    return usersByName.keySet().contains(name);
  }

  public Many2ManyUserSession removeBySession(WebSocketSession session) {
    final Many2ManyUserSession user = getBySession(session);
    if(user!=null){
    	usersByName.remove(user.getName());
    }
    usersBySessionId.remove(session.getId());
    return user;
  }

}
