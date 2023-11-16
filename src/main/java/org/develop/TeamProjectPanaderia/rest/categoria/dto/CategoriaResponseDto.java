package org.develop.TeamProjectPanaderia.rest.categoria.dto;

public record CategoriaResponseDto(
        String nameCategory,
        String createdAt,
        String updatedAt,
        Boolean isActive
)  {
}