package org.develop.TeamProjectPanaderia.producto.repositories;

import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    List<Producto> findAllByCategoriaEqualsIgnoreCase(String categoria);
    List<Producto> findAllByPrecio(Double precio);
    List<Producto> findAllByNombreEqualsIgnoreCase(String nombre);
    List<Producto> findAllByIsActivoFalse();
    List<Producto> findAllByIsActivoTrue();

}
