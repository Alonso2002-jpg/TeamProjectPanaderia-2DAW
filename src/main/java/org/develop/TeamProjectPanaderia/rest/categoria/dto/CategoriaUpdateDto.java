package org.develop.TeamProjectPanaderia.rest.categoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaUpdateDto(
        @Schema(description = "Nombre de la categoria", example = "Reposteria")
        String nameCategory,
        @Schema(description = "Si esta activa o no la categoria", example = "true")
        Boolean isActive
) {
}
