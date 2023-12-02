package org.develop.TeamProjectPanaderia.rest.proveedores.services;

import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.exceptions.*;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.rest.proveedores.repositories.ProveedorRepository;
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
        try{
            return proveedoresRepository.save(proveedorMapper.toProveedor(
                    proveedor,
                    categoriaService.findByName(proveedor.getTipo())
            ));
        } catch (CategoriaNotFoundException e){
            throw new ProveedorBadRequest(proveedor.getTipo());
        }
    }

    @Override
    public Proveedor updateProveedor(ProveedorUpdateDto proveedorUpdateDto, Long id) {
        Proveedor proveedor = getProveedoresById(id);
        try{
            if(proveedorUpdateDto.getNif() != null && !proveedorUpdateDto.getNif().isEmpty()){
                Optional <Proveedor> proveedorSameNif = proveedoresRepository.findByNif(proveedorUpdateDto.getNif());
                if(proveedorSameNif.isPresent() && !proveedorSameNif.get().getId().equals(proveedor.getId())){
                    throw new ProveedorInvalidNif(proveedorUpdateDto.getNif());
                }
            }
            return proveedoresRepository.save(
                    proveedorMapper.toProveedor(
                            proveedorUpdateDto,
                            proveedor,
                            proveedorUpdateDto.getTipo() != null ? categoriaService.findByName(proveedorUpdateDto.getTipo()) : null
                    )
            );
        } catch (CategoriaNotFoundException e){
            throw new ProveedorBadRequest(proveedorUpdateDto.getTipo());
        }
    }

    @Override
    public Proveedor getProveedoresById(Long id) {
        return proveedoresRepository.findById(id).orElseThrow(() -> new ProveedorNotFoundException(""+id));
    }

    @Override
    @Transactional
    public void deleteProveedoresById(Long id) {
        Proveedor proveedor = getProveedoresById(id);
        if(proveedoresRepository.existsProductById(id)) {
            throw new ProveedorNotDeletedException(id);
        } else {
            proveedoresRepository.deleteById(id);
        }
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