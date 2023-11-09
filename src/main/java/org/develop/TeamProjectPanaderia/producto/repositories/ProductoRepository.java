package org.develop.TeamProjectPanaderia.producto.repositories;

import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nameCategory) LIKE LOWER(:categoria)")
    List<Producto> findAllByCategoriaContainsIgnoreCase(String categoria);
    List<Producto> findAllByPrecio(Double precio);
    List<Producto> findAllByNombreContainsIgnoreCase(String nombre);
    List<Producto> findAllByIsActivoFalse();
    List<Producto> findAllByIsActivoTrue();
    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nameCategory) LIKE LOWER(:categoria) AND LOWER(p.proveedor.NIF) LIKE LOWER(:proveedor)")
    List<Producto> findAllByCategoriaContainsIgnoreCaseAndProveedorContainsIgnoreCase(String categoria, String proveedor);
    @Query("SELECT p FROM Producto p WHERE LOWER(p.proveedor.NIF) LIKE LOWER(:proveedor)")
    List<Producto> findAllByProveedorContainsIgnoreCase(String proveedor);
    @Override
    Optional<Producto> findById(UUID uuid);
    Optional<Producto> findByNombreEqualsIgnoreCase(String nombre);
    @Override
    void deleteById(UUID uuid);
}
