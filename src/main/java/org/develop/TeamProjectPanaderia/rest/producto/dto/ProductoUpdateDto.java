package org.develop.TeamProjectPanaderia.rest.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ProductoUpdateDto (
     @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
     @Schema(description = "Nombre del producto", example = "Pan de Barra")
     String nombre,
     @Min(value = 0, message = "El stock no puede ser negativo")
     @Schema(description = "Cantidad del producto", example = "50")
     Integer stock,
     @Min(value = 0, message = "El precio no puede ser negativo")
     @Schema(description = "Precio del producto", example = "1.99")
     Double precio,
     @Schema(description = "Si esta activo o no el producto", example = "true")
     Boolean isActivo,
     @Schema(description = "Categoria del producto", example = "Pan")
     String categoria,
     @Schema(description = "Nif del proveedor del producto", example = "12345678Z")
     String proveedor){
}
















