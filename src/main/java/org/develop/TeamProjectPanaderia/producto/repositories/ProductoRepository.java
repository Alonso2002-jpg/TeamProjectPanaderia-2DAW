package org.develop.TeamProjectPanaderia.producto.repositories;

import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    List<Producto> findAllByCategoriaContainingIgnoreCase(String categoria);
    List<Producto> findAllByPrecio(Double precio);
    List<Producto> findAllByNombreContainingIgnoreCase(String nombre);
    List<Producto> findAllByIsActivoFalse();
    List<Producto> findAllByIsActivoTrue();
    List<Producto> findAllByCategoriaContainingIgnoreCaseAndProveedorContainingIgnoreCase(String categoria, String proveedor);
    List<Producto> findAllByProveedorContainingIgnoreCase(String proveedor);
    @Override
    Optional<Producto> findById(UUID uuid);
    Optional<Producto> findByNombreContainingIgnoreCase(String nombre);
    @Override
    void deleteById(UUID uuid);
}
