package org.develop.TeamProjectPanaderia.proveedores.repositories;


import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Proveedor save(Proveedor proveedores);

    Optional<Proveedor> findById(Long id);

    void deleteById(Long id);

    List<Proveedor> findAll();
    Proveedor findByNIF(String nif);
    List<Proveedor> findByNombre(String nombre);
}
