package org.develop.TeamProjectPanaderia.personal.dto;

public record PersonalResponseDto(
        String dni,
        String name,
        String seccion,
        String fechaAlta,
        String fechaBaja,
        String fechaCreacion,
        String fechaUpdate,
        Boolean isActive
) {
}
