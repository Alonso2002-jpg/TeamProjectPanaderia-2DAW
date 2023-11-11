package org.develop.TeamProjectPanaderia.cliente.dto;


import lombok.Builder;


import java.time.LocalDateTime;

@Builder
public class ClienteResponseDto {

    Long id;
    String nombreCompleto;
    String correo;
    String dni;
    String telefono;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
    String producto;
    String categoria;
}
