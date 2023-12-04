package org.develop.TeamProjectPanaderia.rest.categoria.repositories;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que define las operaciones de acceso a datos para la entidad Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {/**
 * Busca una categoría por su nombre, sin importar mayúsculas o minúsculas.
 *
 * @param categoryName Nombre de la categoría a buscar.
 * @return Una categoría opcional que coincide con el nombre proporcionado.
 */

    Optional<Categoria> findByNameCategoryIgnoreCase(String categoryName);
    /**
     * Busca categorías por su estado de activación.
     *
     * @param isActive Estado de activación a buscar.
     * @return Lista de categorías que coinciden con el estado de activación proporcionado.
     */
    List<Categoria> findByIsActive(boolean isActive);

    /**
     * Verifica si existen productos asociados a una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return Verdadero si existen productos asociados, falso de lo contrario.
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.categoria.id = :id")
    Boolean existsProductoById(Long id);
    /**
     * Verifica si existen clientes asociados a una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return Verdadero si existen clientes asociados, falso de lo contrario.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.categoria.id = :id")
    Boolean existsClienteById(Long id);
    /**
     * Verifica si existen personal asociado a una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return Verdadero si existen personal asociado, falso de lo contrario.
     */
    @Query("SELECT CASE WHEN COUNT(pe) > 0 THEN true ELSE false END FROM Personal pe WHERE pe.seccion.id = :id")
    Boolean existsPersonalById(Long id);
    /**
     * Verifica si existen proveedores asociados a una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return Verdadero si existen proveedores asociados, falso de lo contrario.
     */
    @Query("SELECT CASE WHEN COUNT(pr) > 0 THEN true ELSE false END FROM Proveedor pr WHERE pr.tipo.id = :id")
    Boolean existsProveedorByID(Long id);
}
