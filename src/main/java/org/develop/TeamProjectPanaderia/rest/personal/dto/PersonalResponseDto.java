package org.develop.TeamProjectPanaderia.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;

import java.util.UUID;

@Builder
public record PersonalResponseDto(
        @Schema(description = "Identificador unico del trabajador", example = "1a70f426-d51f-4a13-ba39-89203f94ed74")
        UUID id,
        @Schema(description = "DNI del trabajador", example = "03480731B")
        String dni,
        @Schema(description = "Nombre del trabajador", example = "Joselyn Obando")
        String nombre,
        @Schema(description = "Seccion del trabajador", example = "Panaderia")
        String seccion,
        @Schema(description = "Fecha de alta del trabajador", example = "2023-12-02")
        String fechaAlta,
        @Schema(description = "Si esta activo o no el trabajador",example = "true")
        Boolean isActive
        Long user
) {
}
