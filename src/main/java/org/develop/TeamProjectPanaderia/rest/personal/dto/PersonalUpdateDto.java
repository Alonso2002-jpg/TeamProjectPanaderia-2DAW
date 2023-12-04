package org.develop.TeamProjectPanaderia.rest.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * Clase de transferencia de datos (DTO) utilizada para representar la actualización parcial de un trabajador (Personal).
 *
 * @param nombre   Nuevo nombre del trabajador, debe contener al menos 3 letras.
 * @param seccion  Nueva sección a la que pertenece el trabajador.
 * @param isActive Nuevo estado de actividad del trabajador.
 * @Length         Anotación que valida la longitud mínima del nombre.
 * @Schema         Anotación que proporciona información sobre la descripción y el ejemplo de cada atributo.
 * @Builder        Anotación de Lombok que facilita la creación de constructores y builders.
 *                  Se utiliza para mejorar la legibilidad y concisión del código.
 */
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
