package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotFoundedException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotSaveException;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public abstract class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedoresRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public Proveedor saveProveedores(Proveedor proveedores) {
        try {
            return proveedoresRepository.save(proveedores);
        } catch (Exception e) {
            throw new ProveedorNotSaveException("Error al guardar el proveedor");
        }
    }

    @Override
    public Optional<Proveedor> getProveedoresById(Long id) {
        return Optional.ofNullable(proveedoresRepository.findById(id).orElseThrow(() -> new ProveedorNotFoundException(id)));
    }

    @Override
    public void deleteProveedoresById(Long id) {
        proveedoresRepository.deleteById(id);
    }

    public Page<Proveedor> findAll(Optional<String> nif, Optional<String> nombre, Pageable pageable) {
        Specification<Proveedor> findIsActive = (root, query, criteriaBuilder) ->
                nif.map(n -> criteriaBuilder.equal(root.get("nif"), n))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> findName = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> criterio = Specification.where(findIsActive).and(findName);

        return proveedoresRepository.findAll(criterio, pageable);
    }


    @Override
    public Proveedor findProveedoresByNIF(String nif) {
        Proveedor proveedor = proveedoresRepository.findByNIF(nif);
        if (proveedor == null) {
            throw new ProveedorNotFoundedException("No se encontró proveedor con NIF: " + nif);
        }
        return proveedor;
    }

    @Override
    public List<Proveedor> findProveedoresByNombre(String nombre) {
        List<Proveedor> proveedores = proveedoresRepository.findByNombre(nombre);
        if (proveedores.isEmpty()) {
            throw new ProveedorNotFoundedException("No se encontraron proveedores con el nombre: " + nombre);
        }
        return proveedores;
    }

    public Proveedor updateProveedores(long l, Proveedor proveedor1) {
        return proveedor1;
    }
}
