package org.develop.TeamProjectPanaderia.categoria.repositories;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
