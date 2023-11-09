package org.develop.TeamProjectPanaderia.personal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
public record CreateResponseDto(
        String dni,
        String name,
        String seccion,
        String fechaAlta,
        String fechaBaja,
        String fechaCreacion,
        String fechaUpdate,
        Boolean isActive
) {
}
