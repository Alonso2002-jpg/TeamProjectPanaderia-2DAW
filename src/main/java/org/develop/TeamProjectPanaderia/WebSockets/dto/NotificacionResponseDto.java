package org.develop.TeamProjectPanaderia.WebSockets.dto;

import lombok.Builder;

/**
 * Clase inmutable que representa un objeto de respuesta para notificaciones.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Builder
public record NotificacionResponseDto(
    String entity,
    String data
) {
}
