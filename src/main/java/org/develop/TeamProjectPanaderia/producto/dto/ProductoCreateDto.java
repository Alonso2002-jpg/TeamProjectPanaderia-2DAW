package org.develop.TeamProjectPanaderia.producto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductoCreateDto (
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,
        @Min(value = 0, message = "El precio no puede ser negativo")
        Double precio,
        String imagen,
        Boolean isActivo,
        @NotBlank(message = "La categoria no puede estar vacia")
        String categoria,
        @NotBlank(message = "El proveedor no puede estar vacia")
        String proveedor
        )
{ }