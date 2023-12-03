package org.develop.TeamProjectPanaderia.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
@Builder
public record PersonalUpdateDto(
        @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
        @Schema(description = "Nombre del trabajador", example = "Joselyn Obando")
        String nombre,
        @Schema(description = "Seccion del trabajador", example = "Panaderia")
        String seccion,
        @Schema(description = "Si esta activo o no el trabajador", example = "true")
        Boolean isActive

) {

}
