package org.develop.TeamProjectPanaderia.producto.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductoResponseDto(
        UUID id,
        String nombre,
        Integer stock,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        String imagen,
        Double precio,
        Boolean isActivo,
        String categoria,
        String proveedor
)
{ }




