package org.develop.TeamProjectPanaderia.WebSockets.mapper;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de mapear objetos a DTOs de notificaciones.
 * @param <T> El tipo de objeto que se mapeara a un DTO de notificacion.
 *
 *@author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@Component
public class NotificacionMapper <T>{


    public NotificacionResponseDto getNotificacionResponseDto(T t, String entity){
        /**
         * Convierte un objeto y una entidad a un DTO de notificacion.
         *
         * @param t      El objeto a convertir a cadena y agregar al DTO.
         * @param entity La entidad relacionada con la notificacion.
         * @return Un DTO de notificacion que contiene la entidad y la representacion en cadena del objeto.
         */
        return new NotificacionResponseDto(
                entity,
                t.toString()
        );
    }

}
