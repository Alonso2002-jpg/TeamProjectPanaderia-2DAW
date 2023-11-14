package org.develop.TeamProjectPanaderia.cliente.dto;


import lombok.Builder;


import java.time.LocalDateTime;

@Builder
public class ClienteResponseDto {

    private Long id;
    private String nombreCompleto;
    private String correo;
    private String dni;
    private String telefono;
    private String imagen;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String categoria;
}
