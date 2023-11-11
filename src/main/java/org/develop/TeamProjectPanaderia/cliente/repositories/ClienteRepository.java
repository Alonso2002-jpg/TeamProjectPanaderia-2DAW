package org.develop.TeamProjectPanaderia.cliente.repositories;

import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.categoria.nameCategory) LIKE LOWER(:categoria)")
    List<Cliente> findAllByCategoriaContainsIgnoreCase(String categoria);

    Optional<Cliente> findClienteByDniEqualsIgnoreCase(String dni);

    List<Cliente> findAllByIsActivoFalse();

    List<Cliente> findAllByIsActivoTrue();

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.categoria.nameCategory) LIKE LOWER(:categoria) AND LOWER(c.producto.nombre) LIKE LOWER(:producto)")
    List<Cliente> findAllByCategoriaContainsIgnoreCaseAndProductoContainsIgnoreCase(String categoria, String producto);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.producto.nombre) LIKE LOWER(:producto)")
    List<Cliente> findAllByProductoContainsIgnoreCase(String producto);

    Optional<Cliente> findByNombreEqualsIgnoreCase(String nombre);

    @Override
    Optional<Cliente> findById(Long id);
    @Override
    void deleteById(Long id);
}