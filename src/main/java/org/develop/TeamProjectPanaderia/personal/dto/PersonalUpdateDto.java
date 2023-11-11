package org.develop.TeamProjectPanaderia.personal.dto;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record PersonalUpdateDto(
        String nombre,
        String section,
        LocalDate fechaBaja,
        Boolean isActive
) {

}
