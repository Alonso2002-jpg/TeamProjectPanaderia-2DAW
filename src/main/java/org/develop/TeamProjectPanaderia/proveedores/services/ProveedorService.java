package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    Proveedor saveProveedores(Proveedor proveedores);
    Optional<Proveedor> getProveedoresById(Long id);
    void deleteProveedoresById(Long id);
    List<Proveedor> getAllProveedores();
    Proveedor findProveedoresByNIF(String nif);
    List<Proveedor> findProveedoresByNombre(String nombre);
}
