package org.develop.TeamProjectPanaderia.config.websockets;

import java.io.IOException;

/**
 * Interfaz para enviar mensajes y mensajes periodicos a traves de WebSocket.
 *  @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */

public interface WebSocketSender {
    /**
     * Envía un mensaje a traves de WebSocket.
     *
     * @param message El mensaje a enviar.
     * @throws IOException Si ocurre un error durante el envio del mensaje.
     */
    void sendMessage(String message) throws IOException;
    /**
     * Envía un mensaje periodico a traves de WebSocket.
     *
     * @param message El mensaje periodico a enviar.
     * @throws IOException Si ocurre un error durante el envio del mensaje periódico.
     */
    void sendPeriodicMessage(String message) throws IOException;
}
