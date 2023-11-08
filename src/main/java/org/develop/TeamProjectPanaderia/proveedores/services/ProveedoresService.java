package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;

import java.util.List;
import java.util.Optional;

public interface ProveedoresService {
    Proveedores saveProveedores(Proveedores proveedores);
    Optional<Proveedores> getProveedoresById(Long id);
    void deleteProveedoresById(Long id);
    List<Proveedores> getAllProveedores();
    Proveedores findProveedoresByNIF(String nif);
    List<Proveedores> findProveedoresByNombre(String nombre);
}
