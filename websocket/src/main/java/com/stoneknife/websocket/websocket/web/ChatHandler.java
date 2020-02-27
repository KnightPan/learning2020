package com.stoneknife.websocket.websocket.web;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    public static ConcurrentHashMap<String, Set<WebSocketSession>> roomUserMap = new ConcurrentHashMap<>();

    //1、建立连接
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //id=85553f2c-1f99-3d6d-e8e1-5e2ce0b82379, uri=ws://127.0.0.1:8080/chat?userId=1782&roomId=1001
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String key = "room:" + roomId;
        //保存session
        roomUserMap.putIfAbsent(key, new HashSet<>());
        roomUserMap.get(key).add(session);
        System.out.println("新用户来了:" + session);
    }

    //2、数据交互
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        //收到消息要广播给房间的所有人
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String userId = uriComponents.getQueryParams().getFirst("userId");
        String key = "room:" + roomId;
        broadcastMessage(key, message.getPayload());

    }

    //3、退出房间
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");
        String key = "room:" + roomId;
        roomUserMap.get(key).remove(session);
        System.out.println("用户走了:" + session);
    }

    //4、推送给其他用户
    public void broadcastMessage(String key, String message) throws IOException {
        Set<WebSocketSession> webSocketSessions = roomUserMap.get(key);
        for (WebSocketSession webSocketSession : webSocketSessions){
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }
}
