package org.develop.TeamProjectPanaderia.cliente.dto;


import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;

@Data
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
    private Boolean isActive;
}
