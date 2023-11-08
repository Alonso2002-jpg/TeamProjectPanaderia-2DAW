package org.develop.TeamProjectPanaderia.cliente.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class ClienteResponseDto {

    private Long id;
    private String nombreCompleto;
    private String correo;
    private String dni;
    private String telefono;
    private String producto;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String categoria;
}
