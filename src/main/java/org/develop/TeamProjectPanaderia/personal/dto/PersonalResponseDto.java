package org.develop.TeamProjectPanaderia.personal.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PersonalResponseDto(
        UUID id,
        String dni,
        String nombre,
        String seccion,
        String fechaAlta,
        Boolean isActive
) {
}
