package com.example.demo.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler{
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message){
        System.out.println(session.toString());;
        System.out.println(message.toString());;

    }
}
