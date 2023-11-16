package org.develop.TeamProjectPanaderia.rest.categoria.repositories;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    Optional<Categoria> findByNameCategoryIgnoreCase(String categoryName);
    List<Categoria> findByIsActive(boolean isActive);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.categoria.id = :id")
    Boolean existsProductoById(Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.categoria.id = :id")
    Boolean existsClienteById(Long id);

    @Query("SELECT CASE WHEN COUNT(pe) > 0 THEN true ELSE false END FROM Personal pe WHERE pe.seccion.id = :id")
    Boolean existsPersonalById(Long id);

    @Query("SELECT CASE WHEN COUNT(pr) > 0 THEN true ELSE false END FROM Proveedor pr WHERE pr.tipo.id = :id")
    Boolean existsProveedorByID(Long id);
}
