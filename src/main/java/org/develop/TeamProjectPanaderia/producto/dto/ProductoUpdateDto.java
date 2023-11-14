package org.develop.TeamProjectPanaderia.producto.dto;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ProductoUpdateDto (
     @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
     String nombre,
     @Min(value = 0, message = "El stock no puede ser negativo")
     Integer stock,
     String imagen,
     @Min(value = 0, message = "El precio no puede ser negativo")
     Double precio,
     Boolean isActivo,
     String categoria,
     String proveedor){
}
















