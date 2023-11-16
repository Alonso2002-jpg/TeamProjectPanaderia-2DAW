package org.develop.TeamProjectPanaderia.WebSockets.model;

import lombok.Builder;

@Builder
public record Notificacion<T>(
        String entity,
        Tipo tipo,
        T data,
        String createdAt
) {

    public enum Tipo{CREATE,UPDATE,DELETE}
}
