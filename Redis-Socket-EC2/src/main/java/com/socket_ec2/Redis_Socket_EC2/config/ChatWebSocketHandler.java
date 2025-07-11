package com.socket_ec2.Redis_Socket_EC2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
  private final RedisTemplate<String, String> redisTemplate;
  private final SessionManager sessionManager;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    String room = getRoom(session);
    sessionManager.add(room, session);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    String room = getRoom(session);
    redisTemplate.convertAndSend(room, message.getPayload());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessionManager.remove(session);
  }

  private String getRoom(WebSocketSession session) {
    String uri = session.getUri().getQuery(); // e.g., room=abc
    return uri != null && uri.startsWith("room=") ? uri.substring(5) : "default";
  }
}
