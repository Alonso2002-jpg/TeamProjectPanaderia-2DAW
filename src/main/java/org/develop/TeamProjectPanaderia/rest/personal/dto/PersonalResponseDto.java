package org.develop.TeamProjectPanaderia.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.develop.TeamProjectPanaderia.rest.users.dto.UserInfoResponseDto;

import java.util.UUID;

/**
 * Clase de transferencia de datos (DTO) utilizada para representar la respuesta de un trabajador (Personal).
 *
 * @param id          Identificador único del trabajador.
 * @param dni         DNI del trabajador.
 * @param nombre      Nombre del trabajador.
 * @param seccion     Sección a la que pertenece el trabajador.
 * @param fechaAlta   Fecha de alta del trabajador.
 * @param isActive    Indica si el trabajador está activo o no.
 * @param user        Identificador único del usuario del personal.
 * @Builder           Anotación de Lombok que facilita la creación de constructores y builders.
 *                     Se utiliza para mejorar la legibilidad y concisión del código.
 */
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
        Boolean isActive,
        @Schema(description = "Identificador unico del usuario del personal", example = "1")
        Long user
) {
}
