package org.develop.TeamProjectPanaderia.rest.personal.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
@Builder
public record PersonalUpdateDto(
        @Length(min = 3, message = "El nombre debe contener al menos 3 letras")
        String nombre,
        String seccion,
        Boolean isActive

) {

}
