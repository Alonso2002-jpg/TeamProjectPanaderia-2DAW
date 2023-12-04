package org.develop.TeamProjectPanaderia.utils.pageresponse;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Clase inmutable que representa la respuesta paginada de una consulta.
 *
 * @param <T> El tipo de contenido de la pagina.
 *
 *@author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public record PageResponse<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int pageSize,
        int pageNumber,
        int totalPageElements,
        boolean empty,
        boolean first,
        boolean last,
        String sortBy,
        String direction
) {

    /**
     * Metodo estatico de fabrica para crear una instancia de PageResponse a partir de un objeto Page.
     *
     * @param <T>        El tipo de contenido de la pagina.
     * @param page       El objeto Page del cual obtener la informacion.
     * @param sortBy     El campo por el cual se ha ordenado la pagina.
     * @param direction  La direccion de la ordenacion (ASC o DESC).
     * @return Una instancia de PageResponse.
     */
    public static <T> PageResponse<T> of(Page<T> page, String sortBy, String direction) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.isEmpty(),
                page.isFirst(),
                page.isLast(),
                sortBy,
                direction
        );
    }
}