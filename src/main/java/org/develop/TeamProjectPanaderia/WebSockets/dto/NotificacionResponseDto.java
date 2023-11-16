package org.develop.TeamProjectPanaderia.WebSockets.dto;

import lombok.Builder;

@Builder
public record NotificacionResponseDto(
    String entity,
    String data
) {
}
