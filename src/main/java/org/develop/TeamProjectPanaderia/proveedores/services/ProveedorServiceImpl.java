package org.develop.TeamProjectPanaderia.proveedores.services;

import jakarta.persistence.criteria.Join;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.proveedores.exceptions.ProveedorNotSaveException;
import org.develop.TeamProjectPanaderia.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedoresRepository;
    private final CategoriaService categoriaService;
    private final ProveedorMapper proveedorMapper;

    public ProveedorServiceImpl(ProveedorRepository proveedoresRepository,
                                CategoriaService categoriaService,
                                ProveedorMapper proveedorMapper) {
        this.proveedoresRepository = proveedoresRepository;
        this.proveedorMapper = proveedorMapper;
        this.categoriaService = categoriaService;
    }

    @Override
    public Proveedor saveProveedores(ProveedorCreateDto proveedor) {
        if (proveedoresRepository.findByNif(proveedor.getNif()).isPresent()){
            throw new ProveedorNotSaveException("Proveedores with NIF " + proveedor.getNif() + " already exists");
        }
        return proveedoresRepository.save(proveedorMapper.toProveedor(
                proveedor,
                categoriaService.findByName(proveedor.getTipo())
        ));
    }

    @Override
    public Proveedor updateProveedor(ProveedorUpdateDto proveedorUpdateDto, Long id) {
        Proveedor proveedor = getProveedoresById(id);
        return proveedoresRepository.save(
                proveedorMapper.toProveedor(
                        proveedorUpdateDto,
                        proveedor,
                        proveedorUpdateDto.getTipo() != null ? categoriaService.findByName(proveedorUpdateDto.getTipo()) : null
                )
        );
    }

    @Override
    public Proveedor getProveedoresById(Long id) {
        return proveedoresRepository.findById(id).orElseThrow(() -> new ProveedorNotFoundException(""+id));
    }

    @Override
    public void deleteProveedoresById(Long id) {
        getProveedoresById(id);
        proveedoresRepository.deleteById(id);
    }

    @Override
    public Proveedor findProveedoresByNIF(String nif) {
        return proveedoresRepository.findByNif(nif).orElseThrow(() -> new ProveedorNotFoundException(nif));
    }

    @Override
    public Page<Proveedor> findAll(Optional<String> nif, Optional<String> name, Optional<Boolean> isActive, Optional<String> tipo, Pageable pageable) {
        Specification<Proveedor> findNif = (root, query, criteriaBuilder) ->
                nif.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nif")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> findIsActive = (root, query, criteriaBuilder) ->
                isActive.map(isAc -> criteriaBuilder.equal(root.get("isActive"),isAc))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> findTipo = (root, query, criteriaBuilder) ->
                tipo.map(ti -> {
                    Join<Proveedor, Categoria> categoriaJoin = root.join("tipo");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + ti.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> findName = (root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Proveedor> criterio = Specification.where(findNif)
                .and(findName)
                .and(findIsActive)
                .and(findTipo);

        return proveedoresRepository.findAll(criterio, pageable);
    }

}