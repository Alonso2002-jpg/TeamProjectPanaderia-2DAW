package org.develop.TeamProjectPanaderia.producto.dto;

import jakarta.validation.constraints.Min;

public record ProductoUpdateDto (
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
















