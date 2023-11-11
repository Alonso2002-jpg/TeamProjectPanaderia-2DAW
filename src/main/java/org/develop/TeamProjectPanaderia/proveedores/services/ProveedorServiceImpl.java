package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedoresRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public Proveedor saveProveedores(Proveedor proveedores) {
        return proveedoresRepository.save(proveedores);
    }

    @Override
    public Optional<Proveedor> getProveedoresById(Long id) {
        return proveedoresRepository.findById(id);
    }

    @Override
    public void deleteProveedoresById(Long id) {
        proveedoresRepository.deleteById(id);
    }

    @Override
    public List<Proveedor> getAllProveedores() {
        return proveedoresRepository.findAll();
    }

    @Override
    public Proveedor findProveedoresByNIF(String nif) {
        return proveedoresRepository.findByNIF(nif);
    }

    @Override
    public List<Proveedor> findProveedoresByNombre(String nombre) {
        return proveedoresRepository.findByNombre(nombre);
    }

    public Proveedor updateProveedores(long l, Proveedor proveedor1) {
        return proveedor1;
    }
}
