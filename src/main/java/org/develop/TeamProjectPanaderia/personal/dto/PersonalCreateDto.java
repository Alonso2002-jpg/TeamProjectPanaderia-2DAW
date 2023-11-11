package org.develop.TeamProjectPanaderia.personal.dto;

import lombok.Builder;

@Builder
public record PersonalCreateDto(
        String dni,
        String name,
        String seccion,
        String fechaAlta,
        String fechaBaja,
        boolean isActive
) {
}

