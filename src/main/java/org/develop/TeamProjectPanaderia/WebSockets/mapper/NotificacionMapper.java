package org.develop.TeamProjectPanaderia.WebSockets.mapper;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper <T>{


    public NotificacionResponseDto getNotificacionResponseDto(T t, String entity){
        return new NotificacionResponseDto(
                entity,
                t.toString()
        );
    }

}
