package org.develop.TeamProjectPanaderia.categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaCreateDto(
        @NotBlank(message = "El nombre de la categoria no puede estar vacio")
        String nameCategory,
        boolean isActive
) {
}
