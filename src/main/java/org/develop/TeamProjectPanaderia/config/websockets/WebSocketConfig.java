package org.develop.TeamProjectPanaderia.config.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private String urlWebSocket;
    private String entity;
    public void setUrlAndEntity(String urlWebSocket, String entity){
        this.urlWebSocket = urlWebSocket;
        this.entity = entity;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/" + urlWebSocket).setAllowedOrigins("*");

    }
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(entity);
    }
}
