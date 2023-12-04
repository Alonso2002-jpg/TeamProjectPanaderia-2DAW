package org.develop.TeamProjectPanaderia.rest.personal.repositories;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.personal.models.Personal;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz que proporciona métodos para acceder y gestionar entidades de Personal en la base de datos.
 *
 * @Repository Indica que esta interfaz es un componente de repositorio, permitiendo la detección automática y
 * gestión de excepciones específicas de Spring.
 */
@Repository
public interface PersonalRepository extends JpaRepository<Personal, UUID>, JpaSpecificationExecutor<Personal> {

    /**
     * Busca un trabajador por su identificador único.
     *
     * @param uuid Identificador único del trabajador.
     * @return Una instancia de {@code Optional<Personal>} que puede contener o no un trabajador con el identificador dado.
     */
    Optional<Personal> findById(UUID uuid);

    /**
     * Busca trabajadores por su estado de activación.
     *
     * @param isActive Estado de activación del trabajador (activo o no).
     * @return Una lista de trabajadores que cumplen con el estado de activación especificado.
     */
    List<Personal> findByIsActive(boolean isActive);

    /**
     * Busca un trabajador por su número de DNI (ignorando mayúsculas y minúsculas).
     *
     * @param dni Número de DNI del trabajador.
     * @return Una instancia de {@code Optional<Personal>} que puede contener o no un trabajador con el DNI dado.
     */
    Optional<Personal> findByDniEqualsIgnoreCase(String dni);
}

