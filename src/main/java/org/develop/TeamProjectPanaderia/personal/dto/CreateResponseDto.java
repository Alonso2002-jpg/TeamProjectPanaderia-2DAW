package org.develop.TeamProjectPanaderia.personal.dto;

import lombok.Builder;

@Builder
public record CreateResponseDto(
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
