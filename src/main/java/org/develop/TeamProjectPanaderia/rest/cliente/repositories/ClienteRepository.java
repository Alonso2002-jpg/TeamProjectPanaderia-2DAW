package org.develop.TeamProjectPanaderia.rest.cliente.repositories;

import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Interfaz de repositorio para la entidad Cliente. Extiende JpaRepository y JpaSpecificationExecutor
 * para proporcionar operaciones CRUD básicas y soporte para consultas basadas en especificaciones.
 *
 * @Repository Anotación que indica que esta interfaz es un componente de repositorio.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> , JpaSpecificationExecutor<Cliente> {
    /**
     * Busca un cliente por su identificador único (ID).
     *
     * @param id El ID del cliente a buscar.
     * @return Un Optional que puede contener un objeto Cliente si se encuentra, o estar vacío si no se encuentra.
     */
    @Override
    Optional<Cliente> findById(Long id);
    /**
     * Busca un cliente por su número de identificación (DNI) sin distinguir entre mayúsculas y minúsculas.
     *
     * @param dni El número de identificación (DNI) del cliente a buscar.
     * @return Un Optional que puede contener un objeto Cliente si se encuentra, o estar vacío si no se encuentra.
     */
    Optional<Cliente> findClienteByDniEqualsIgnoreCase(String dni);
    /**
     * Elimina un cliente por su identificador único (ID).
     *
     * @param id El ID del cliente a eliminar.
     */
    @Override
    void deleteById(Long id);
    /**
     * Busca clientes por su estado de activación.
     *
     * @param isActive El estado de activación que se utilizará como criterio de búsqueda.
     * @return Una lista de clientes que cumplen con el criterio de activación especificado.
     */
    List<Cliente> findByIsActive(boolean isActive);
}