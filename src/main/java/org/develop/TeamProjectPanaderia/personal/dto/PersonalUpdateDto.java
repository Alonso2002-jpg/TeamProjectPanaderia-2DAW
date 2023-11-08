package org.develop.TeamProjectPanaderia.personal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;

import java.time.LocalDate;
@Builder
@AllArgsConstructor
@Data

public record PersonalUpdateDto(
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @NotBlank(message = "Tine que tener una seccion no puede estar vacio")
        String section,
        LocalDate fechaBaja,
        Boolean isActive
) {

}
