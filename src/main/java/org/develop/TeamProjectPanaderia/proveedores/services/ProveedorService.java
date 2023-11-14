package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    Proveedor saveProveedores(Proveedor proveedores);
    Optional<Proveedor> getProveedoresById(Long id);
    void deleteProveedoresById(Long id);
    List<Proveedor> getAllProveedores();
    Proveedor findProveedoresByNIF(String nif);
    List<Proveedor> findProveedoresByNombre(String nombre);

    Page<Proveedor> findAll(Optional<String> nif, Optional<String> name, Pageable pageable);
}
