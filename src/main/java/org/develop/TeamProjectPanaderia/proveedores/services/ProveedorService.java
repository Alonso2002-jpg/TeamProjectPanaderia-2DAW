package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    Proveedor saveProveedores(ProveedorCreateDto proveedores);
    Proveedor updateProveedor(ProveedorUpdateDto proveedorUpdateDto, Long id);
    Proveedor getProveedoresById(Long id);
    void deleteProveedoresById(Long id);
    Proveedor findProveedoresByNIF(String nif);

    Page<Proveedor> findAll(Optional<String> nif, Optional<String> name, Optional<Boolean> isActive,Optional<String> tipo, Pageable pageable);
}
