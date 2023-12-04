package org.develop.TeamProjectPanaderia.rest.proveedores.repositories;

import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Guarda un proveedor en la base de datos.
     */
    Proveedor save(Proveedor proveedor);

    /**
     * Verifica si existen productos asociados a un proveedor mediante su identificador único.
     */
    Optional<Proveedor> findById(Long id);

    /**
     * Verifica si existen productos asociados a un proveedor mediante su identificador único.
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.proveedor.id = :id")
    Boolean existsProductById(Long id);


    /**
     * Elimina un proveedor por su identificador único.
     */
    void deleteById(Long id);

    /**
     * Busca un proveedor por su NIF.
     */
    Optional<Proveedor> findByNif(String nif);

    /**
     * Obtiene una página de proveedores que cumplen con un criterio de especificación.
     */
    Page<Proveedor> findAll(Specification<Proveedor> criterio, Pageable pageable);
}