package org.develop.TeamProjectPanaderia.rest.personal.services;

import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalCreateDto;
import org.develop.TeamProjectPanaderia.rest.personal.dto.PersonalUpdateDto;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que declara métodos para gestionar entidades de Personal.
 */
public interface PersonalService {

    /**
     * Obtiene una página de trabajadores filtrados por nombre, DNI, sección, estado de activación y paginación.
     *
     * @param nombre   Nombre del trabajador (opcional).
     * @param dni      Número de DNI del trabajador (opcional).
     * @param seccion  Sección del trabajador (opcional).
     * @param isActivo Estado de activación del trabajador (opcional).
     * @param pageable Objeto que encapsula información de paginación.
     * @return Una página de trabajadores que cumplen con los criterios de búsqueda y paginación.
     */
    Page<Personal> findAll(Optional<String> nombre, Optional <String> dni, Optional<String> seccion, Optional<Boolean> isActivo, Pageable pageable);

    /**
     * Obtiene un trabajador por su identificador único.
     *
     * @param id Identificador único del trabajador.
     * @return Una instancia de {@code Personal} que representa al trabajador con el identificador dado.
     */
    Personal findById(String id);

    /**
     * Busca un trabajador por su número de DNI.
     *
     * @param dni Número de DNI del trabajador.
     * @return Una instancia de {@code Personal} que representa al trabajador con el DNI dado.
     */
    Personal findPersonalByDni(String dni);

    /**
     * Guarda un nuevo trabajador en la base de datos.
     *
     * @param personal Objeto de tipo {@code PersonalCreateDto} que contiene la información del nuevo trabajador.
     * @return Una instancia de {@code Personal} que representa al trabajador recién creado.
     */
    Personal save(PersonalCreateDto personal);

    /**
     * Actualiza la información de un trabajador existente en la base de datos.
     *
     * @param id       Identificador único del trabajador a actualizar.
     * @param personal Objeto de tipo {@code PersonalUpdateDto} que contiene la información actualizada.
     * @return Una instancia de {@code Personal} que representa al trabajador actualizado.
     */
    Personal update(String id, PersonalUpdateDto personal);

    /**
     * Elimina un trabajador por su identificador único.
     *
     * @param id Identificador único del trabajador a eliminar.
     */
    void deleteById(String id);

    /**
     * Obtiene una lista de trabajadores filtrados por estado de activación.
     *
     * @param isActive Estado de activación del trabajador (activo o no).
     * @return Una lista de trabajadores que cumplen con el estado de activación especificado.
     */
    List<Personal> findByActiveIs(Boolean isActive);

}
