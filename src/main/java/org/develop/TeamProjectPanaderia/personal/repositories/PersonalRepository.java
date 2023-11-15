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

    Optional<Personal> findById(UUID uudi);

    List<Personal> findByIsActive(Boolean isActive);
    Optional<Personal> findByDni(String dni);

    @Query("SELECT p FROM Personal p WHERE LOWER(p.seccion.nameCategory) LIKE LOWER(:categoria)")
    List<Personal> findAllByCategoriaContainsIgnoreCase(String categoria);

   // @Query("SELECT p FROM Personal p WHERE LOWER(p.isActive) LIKE LOWER(:isActive)")
 //   List<Personal> findAllByIsActiveIgnereCase(Boolean isActive);

    @Query("SELECT p FROM Personal p WHERE LOWER(p.seccion.nameCategory) LIKE LOWER(CONCAT('%',:categoria,'%')) AND p.isActive = :isActive")
    List<Personal> findByCategoriaContainingIgnoreCaseAndIsActive(String categoria, Boolean isActive);

}


