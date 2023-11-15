package org.develop.TeamProjectPanaderia.rest.producto.dto;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;

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




