package org.develop.TeamProjectPanaderia.rest.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;

import java.util.UUID;

public record ProductoResponseDto(
        @Schema(description = "Identificador unico del producto", example = "1a70f426-d51f-4a13-ba39-89203f94ed74")
        UUID id,
        @Schema(description = "Nombre del producto", example = "Pan de Barra")
        String nombre,
        @Schema(description = "Precio del producto", example = "1.99")
        Double precio,
        @Schema(description = "Cantidad del producto", example = "50")
        Integer stock,
        @Schema(description = "Imagen del producto", example = Producto.IMAGE_DEFAULT)
        String imagen,
        @Schema(description = "Categoria del producto", example = "Pan")
        Categoria categoria,
        @Schema(description = "Nif del proveedor del producto", example = "12345678Z")
        Proveedor proveedor,
        @Schema(description = "Si esta activo o no el producto", example = "true")
        Boolean isActivo
)
{ }




