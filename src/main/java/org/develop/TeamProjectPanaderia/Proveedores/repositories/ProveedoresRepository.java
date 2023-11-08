package org.develop.TeamProjectPanaderia.Proveedores.repositories;


import org.develop.TeamProjectPanaderia.Proveedores.models.Proveedores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedoresRepository extends JpaRepository<Proveedores, Long> {
    Proveedores save(Proveedores proveedores);

    Optional<Proveedores> findById(Long id);

    void deleteById(Long id);

    List<Proveedores> findAll();
    Proveedores findByNIF(String nif);
    List<Proveedores> findByNombre(String nombre);
}
