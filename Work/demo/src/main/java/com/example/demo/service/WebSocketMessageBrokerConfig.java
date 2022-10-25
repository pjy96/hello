package com.example.demo.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Websocket message handling을 허용
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer{
    
    // messagebroker는 송신자에게 수신자의 이전 메세지 프로토콜을 변환해주는 모듈 중 하나
    // 요청이 오면 그에 해당하는 통신 채널로 전송, 응답 또한 같은 경로로 가서 응답한다.
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 내장브로커 / prefix가 붙은 메세지를 발행 시 브로커가 처리
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 메세지 송신 시 붙여줄 prefix, 메세지 처리가 필요할 때 핸들러로 라우팅 되는 prefix
    }
 
    public void registerStompEndpoints(StompEndpointRegistry registry) { 
        registry.addEndpoint("/sendmessage"); // 최초 소켓 연결 시 endpoint
    }
}
