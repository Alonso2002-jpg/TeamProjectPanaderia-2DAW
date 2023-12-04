package org.develop.TeamProjectPanaderia.WebSockets.model;

import lombok.Builder;

/**
 * Clase inmutable que representa una notificacion.
 * @param <T> El tipo de datos asociados a la notificacion.
 *
 *@author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Builder
public record Notificacion<T>(
        String entity,
        Tipo tipo,
        T data,
        String createdAt
) {
    /**
     * Enumeracion que define los tipos posibles de notificacion.
     */
    public enum Tipo{CREATE,UPDATE,DELETE}
}
