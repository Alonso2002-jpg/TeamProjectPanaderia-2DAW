package org.develop.TeamProjectPanaderia.rest.producto.repositories;

import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
/**
 * La interfaz {@code ProductoRepository} proporciona operaciones de acceso a datos para la entidad {@code Producto}.
 * Utiliza Spring Data JPA para interactuar con la base de datos y ofrece métodos para recuperar, buscar y eliminar productos.
 *
 * @version 1.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID>, JpaSpecificationExecutor<Producto> {

    /**
     * Recupera un producto por su identificador único.
     *
     * @param uuid Identificador único del producto.
     * @return Un {@code Optional} que contiene el producto si se encuentra, de lo contrario, un {@code Optional} vacío.
     */
    @Override
    Optional<Producto> findById(UUID uuid);

    /**
     * Busca un producto por su nombre, sin distinguir mayúsculas y minúsculas.
     *
     * @param nombre Nombre del producto a buscar.
     * @return Un {@code Optional} que contiene el producto si se encuentra, de lo contrario, un {@code Optional} vacío.
     */
    Optional<Producto> findByNombreEqualsIgnoreCase(String nombre);

    /**
     * Elimina un producto por su identificador único.
     *
     * @param uuid Identificador único del producto a eliminar.
     */
    @Override
    void deleteById(UUID uuid);
}
