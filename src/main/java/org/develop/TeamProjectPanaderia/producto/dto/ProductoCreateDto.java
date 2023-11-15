package org.develop.TeamProjectPanaderia.producto.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

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


