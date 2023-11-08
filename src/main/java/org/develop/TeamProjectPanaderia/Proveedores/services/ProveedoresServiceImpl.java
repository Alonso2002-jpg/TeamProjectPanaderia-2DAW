package org.develop.TeamProjectPanaderia.Proveedores.services;

import org.develop.TeamProjectPanaderia.Proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.Proveedores.repositories.ProveedoresRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProveedoresServiceImpl implements ProveedoresService {

    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresServiceImpl(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public Proveedores saveProveedores(Proveedores proveedores) {
        return proveedoresRepository.save(proveedores);
    }

    @Override
    public Optional<Proveedores> getProveedoresById(Long id) {
        return proveedoresRepository.findById(id);
    }

    @Override
    public void deleteProveedoresById(Long id) {
        proveedoresRepository.deleteById(id);
    }

    @Override
    public List<Proveedores> getAllProveedores() {
        return proveedoresRepository.findAll();
    }

    @Override
    public Proveedores findProveedoresByNIF(String nif) {
        return proveedoresRepository.findByNIF(nif);
    }

    @Override
    public List<Proveedores> findProveedoresByNombre(String nombre) {
        return proveedoresRepository.findByNombre(nombre);
    }
}
