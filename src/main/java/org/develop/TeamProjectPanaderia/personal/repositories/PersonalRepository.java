package org.develop.TeamProjectPanaderia.personal.repositories;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {

    Optional<Personal> findByUUID(UUID aLong);
    List<Personal> findByIsActive(boolean isActive);
    @Query("SELECT p FROM Personal p WHERE LOWER(p.seccion.nameCategory) LIKE LOWER(:categoria)")
    List<Personal> findAllByCategoriaContainsIgnoreCase(String categoria);
    @Query("SELECT p FROM Personal p WHERE LOWER(p.isActive) LIKE LOWER(:isActive)")
    List<Personal> findAllByIsActiveIgnereCase(boolean isActive);
    @Query("SELECT p FROM Personal p WHERE LOWER(p.seccion.nameCategory) LIKE LOWER(:categoria) AND LOWER(P.isActive) LIKE LOWER(:isActive) ")
    List<Personal> findAllByIsActiveIgnorecaseCategoryIgnoreCase(String categoria,boolean isActive);
}
