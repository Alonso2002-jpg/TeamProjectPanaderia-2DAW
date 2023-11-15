package org.develop.TeamProjectPanaderia.producto.dto;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductoResponseDto(
        UUID id,
        String nombre,
        Double precio,
        Integer stock,
        String imagen,
        Categoria categoria,
        Proveedor proveedor,
        Boolean isActivo
)
{ }




