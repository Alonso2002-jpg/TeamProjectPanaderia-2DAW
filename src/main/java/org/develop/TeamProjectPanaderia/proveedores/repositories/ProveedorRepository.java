package org.develop.TeamProjectPanaderia.proveedores.repositories;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Proveedor save(Proveedor proveedor);

    Optional<Proveedor> findById(Long id);

    void deleteById(Long id);

    Optional<Proveedor> findByNif(String nif);

    Page<Proveedor> findAll(Specification<Proveedor> criterio, Pageable pageable);
}