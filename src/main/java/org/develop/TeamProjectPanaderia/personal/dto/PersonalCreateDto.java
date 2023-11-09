package org.develop.TeamProjectPanaderia.personal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public record PersonalCreateDto(
        String dni,
        String name,
        String seccion,
        String fechaAlta,
        String fechaBaja,
        boolean isActive
) {
}

