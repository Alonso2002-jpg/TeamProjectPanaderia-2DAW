package org.develop.TeamProjectPanaderia.rest.personal.dto;

import lombok.Builder;

@Builder
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
