package org.develop.TeamProjectPanaderia.rest.personal.dto;

import lombok.Builder;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;

import java.util.UUID;

@Builder
public record PersonalResponseDto(
        UUID id,
        String dni,
        String nombre,
        String seccion,
        String fechaAlta,
        Boolean isActive,
        Long user
) {
}
