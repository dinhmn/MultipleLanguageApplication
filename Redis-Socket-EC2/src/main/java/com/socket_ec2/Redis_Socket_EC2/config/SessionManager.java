package com.socket_ec2.Redis_Socket_EC2.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

  private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

  public void add(String room, WebSocketSession session) {
    roomSessions.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
  }

  public void remove(WebSocketSession session) {
    roomSessions.values().forEach(set -> set.remove(session));
  }

  public Set<WebSocketSession> getSessions(String room) {
    return roomSessions.getOrDefault(room, Collections.emptySet());
  }
}
