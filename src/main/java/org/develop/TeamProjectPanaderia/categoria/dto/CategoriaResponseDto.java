package org.develop.TeamProjectPanaderia.categoria.dto;

public record CategoriaResponseDto(
        String nameCategory,
        String createdAt,
        String updatedAt,
        boolean isActive
) {
}
