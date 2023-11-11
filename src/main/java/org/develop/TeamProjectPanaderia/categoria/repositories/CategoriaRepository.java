package org.develop.TeamProjectPanaderia.categoria.repositories;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    Optional<Categoria> findByNameCategoryIgnoreCase(String categoryName);
    List<Categoria> findByIsActive(boolean isActive);
}
