package org.develop.TeamProjectPanaderia.rest.categoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaResponseDto(
        @Schema(description = "Nombre de la categoria", example = "Reposteria")
        String nameCategory,
        @Schema(description = "Fecha de creacion del registro de la categoria", example = "2023-12-02")
        String createdAt,
        @Schema(description = "Fecha de actualizacion del registro de la categoria", example = "2023-12-02")
        String updatedAt,
        @Schema(description = "Si esta activa o no la categoria", example = "true")
        Boolean isActive
)  {
}