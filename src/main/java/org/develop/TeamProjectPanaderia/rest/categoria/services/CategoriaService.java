package org.develop.TeamProjectPanaderia.rest.categoria.services;

import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que define los servicios relacionados con la entidad Categoria.
 */
public interface CategoriaService {
    /**
     * Obtiene una página de categorías basada en los parámetros de búsqueda proporcionados.
     *
     * @param isActive Estado de activación de la categoría (opcional).
     * @param name     Nombre de la categoría (opcional).
     * @param pageable Información de paginación.
     * @return Página de categorías que cumplen con los criterios de búsqueda.
     */
    Page<Categoria> findAll(Optional<Boolean> isActive, Optional<String> name, Pageable pageable);
    /**
     * Obtiene una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return La categoría con el identificador proporcionado.
     */
    Categoria findById(Long id);
    /**
     * Guarda una nueva categoría.
     *
     * @param categoria DTO de creación de la categoría.
     * @return La categoría creada.
     */
    Categoria save(CategoriaCreateDto categoria);
    /**
     * Actualiza una categoría existente.
     *
     * @param id        Identificador de la categoría a actualizar.
     * @param categoria DTO de actualización de la categoría.
     * @return La categoría actualizada.
     */
    Categoria update(Long id, CategoriaUpdateDto categoria);
    /**
     * Busca una categoría por su nombre.
     *
     * @param name Nombre de la categoría a buscar.
     * @return La categoría con el nombre proporcionado.
     */
    Categoria findByName(String name);
    /**
     * Busca categorías por su estado de activación.
     *
     * @param isActive Estado de activación a buscar.
     * @return Lista de categorías que coinciden con el estado de activación proporcionado.
     */
    List<Categoria> findByActiveIs(boolean isActive);
    /**
     * Verifica si la categoría está asociada a algún otro objeto en el sistema.
     *
     * @param id Identificador de la categoría.
     */
    void categoryExistsSomewhere(Long id);
    /**
     * Elimina una categoría por su identificador.
     *
     * @param id Identificador de la categoría a eliminar.
     */
    void deleteById(Long id);

    /**
     * Elimina todas las categorías.
     */
    void deleteAll();
}
