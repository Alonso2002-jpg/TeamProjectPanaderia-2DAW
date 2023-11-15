package org.develop.TeamProjectPanaderia.rest.producto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ProductoCreateDto (
        @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,
        @Min(value = 0, message = "El precio no puede ser negativo")
        Double precio,
        Boolean isActivo,
        @NotBlank(message = "La categoria no puede estar vacia")
        String categoria,
        @NotBlank(message = "El proveedor no puede estar vacia")
        String proveedor
        )
{ }


