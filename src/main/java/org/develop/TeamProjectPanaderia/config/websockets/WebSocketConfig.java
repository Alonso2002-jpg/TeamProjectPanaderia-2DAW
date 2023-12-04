package org.develop.TeamProjectPanaderia.config.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
/**
 * Clase de configuracion para la habilitacion y configuracion de WebSocket.
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private String urlWebSocket;
    private String entity;
    /**
     * Establece la URL y la entidad para WebSocket.
     *
     * @param urlWebSocket La URL de WebSocket.
     * @param entity       La entidad relacionada con WebSocket.
     */
    public void setUrlAndEntity(String urlWebSocket, String entity){
        this.urlWebSocket = urlWebSocket;
        this.entity = entity;
    }

    /**
     * Registra manejadores de WebSocket en el registro.
     *
     * @param registry El registro de manejadores de WebSocket.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/" + urlWebSocket).setAllowedOrigins("*");

    }
    /**
     * Crea un bean para el manejador de WebSocket.
     *
     * @return El bean del manejador de WebSocket.
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(entity);
    }
}
