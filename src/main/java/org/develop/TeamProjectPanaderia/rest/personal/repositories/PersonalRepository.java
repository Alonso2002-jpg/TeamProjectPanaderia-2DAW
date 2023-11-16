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

@Repository
public interface PersonalRepository extends JpaRepository<Personal, UUID>, JpaSpecificationExecutor<Personal> {
    Optional<Personal> findById(UUID uuid);
    List<Personal> findByIsActive(boolean isActive);
    Optional<Personal> findByDniEqualsIgnoreCase(String dni);
}

