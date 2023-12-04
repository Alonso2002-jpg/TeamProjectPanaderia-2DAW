package org.develop.TeamProjectPanaderia.rest.categoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Data Transfer Object (DTO) utilizado para representar la informacion actualizable de una categoria.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public record CategoriaUpdateDto(
        @Schema(description = "Nombre de la categoria", example = "Reposteria")
        String nameCategory,
        @Schema(description = "Si esta activa o no la categoria", example = "true")
        Boolean isActive
) {
}
