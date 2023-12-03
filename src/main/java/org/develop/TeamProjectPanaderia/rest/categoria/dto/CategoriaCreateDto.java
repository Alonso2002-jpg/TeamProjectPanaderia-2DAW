package org.develop.TeamProjectPanaderia.rest.categoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoriaCreateDto(
        @NotBlank(message = "El nombre de la categoria no puede estar vacio")
        @Schema(description = "Nombre de la categoria", example = "Reposteria")
        String nameCategory,
        @Schema(description = "Si esta activa o no la categoria", example = "true")
        Boolean isActive
) {
}
