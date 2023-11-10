package org.develop.TeamProjectPanaderia.personal.dto;

import java.time.LocalDate;

public record PersonalUpdateDto(
        String nombre,
        String section,
        LocalDate fechaBaja,
        Boolean isActive
) {

}
