package org.develop.TeamProjectPanaderia.producto.repositories;

import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID>, JpaSpecificationExecutor<Producto> {
    @Override
    Optional<Producto> findById(UUID uuid);
    Optional<Producto> findByNombreEqualsIgnoreCase(String nombre);
    @Override
    void deleteById(UUID uuid);
}
