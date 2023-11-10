package org.develop.TeamProjectPanaderia.personal.dto;


public record PersonalCreateDto(
        String dni,
        String name,
        String seccion,
        String fechaAlta,
        String fechaBaja,
        boolean isActive
) {
}

