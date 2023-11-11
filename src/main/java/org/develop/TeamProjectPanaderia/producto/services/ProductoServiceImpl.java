package org.develop.TeamProjectPanaderia.producto.services;

import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedoresService;
    private final ProductoMapper productoMapper;
    private final StorageService storageService;

    @Autowired
    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaService categoriaService,
            ProveedorService proveedoresService,
            ProductoMapper productoMapper,
            StorageService storageService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.proveedoresService = proveedoresService;
        this.productoMapper = productoMapper;
        this.storageService = storageService;
    }

    @Override
    public Page<Producto> findAll(Optional<String> nombre, Optional<Integer> stockMin, Optional<Double> precioMax, Optional<Boolean> isActivo, Optional<String> categoria, Optional<String> proveedor, Pageable pageable) {
        // Criteerio de búsqueda por nombre
        Specification<Producto> specNombreProducto = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por stock
        Specification<Producto> specStockMinProducto = (root, query, criteriaBuilder) ->
                stockMin.map(c -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"),c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por precio
        Specification<Producto> specPrecioMaxProducto = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por isActivo
        Specification<Producto> specIsActivo = (root, query, criteriaBuilder) ->
                isActivo.map(a -> criteriaBuilder.equal(root.get("isActivo"), a))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por categoria
        Specification<Producto> specCategoriaProducto = (root, query, criteriaBuilder) ->
                categoria.map(c ->{
                    Join<Producto, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por proveedor
        Specification<Producto> specProveedorProducto = (root, query, criteriaBuilder) ->
                categoria.map(c ->{
                    Join<Producto, Proveedor> categoriaJoin = root.join("proveedor");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("NIF")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Producto> criterio = Specification.where(specNombreProducto)
                .and(specStockMinProducto)
                .and(specPrecioMaxProducto)
                .and(specIsActivo)
                .and(specCategoriaProducto)
                .and(specProveedorProducto);
        return productoRepository.findAll(criterio, pageable);
    }

    @Override
    public Producto findById(String id) {
        log.info("Buscando producto por id: " + id);
        try{
            var uuid = UUID.fromString(id);
            return productoRepository.findById(uuid).orElseThrow(() -> new ProductoNotFound(uuid));
        } catch (IllegalArgumentException e) {
                throw new ProductoBadUuid(id);
        }
    }

    @Override
    public Producto findByName(String name) {
        log.info("Buscando producto por nombre: " + name);
        return productoRepository.findByNombreEqualsIgnoreCase(name).orElseThrow(() -> new ProductoNotFound(name));
    }

    @Override
    public Producto save(ProductoCreateDto productoCreateDto) {
        log.info("Guardando producto: " + productoCreateDto);
        Categoria categoria = categoriaService.findByName(productoCreateDto.categoria());
        Proveedor proveedores = proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor());
        UUID id = UUID.randomUUID();
        return productoRepository.save(productoMapper.toProducto(id,productoCreateDto, categoria, proveedores));
    }

    @Override
    public Producto update(String id, ProductoUpdateDto productoUpdateDto) {
       log.info("Actualizando producto por id: " + id);
       Producto productoActual = this.findById(id);
       Categoria categoria = null;
       Proveedor proveedor = null;
       if(productoUpdateDto.categoria() != null && !productoUpdateDto.categoria().isEmpty()){
           categoria = categoriaService.findByName(productoUpdateDto.categoria());
       } else {
           categoria = productoActual.getCategoria();
       }
       if(productoUpdateDto.proveedor() != null && !productoUpdateDto.proveedor().isEmpty()){
           proveedor = proveedoresService.findProveedoresByNIF(productoUpdateDto.proveedor());
       } else {
           proveedor = productoActual.getProveedor();
       }
       return productoRepository.save(productoMapper.toProducto(productoUpdateDto, productoActual, categoria, proveedor));
    }

    @Override
    public Producto updateImg(String id, MultipartFile file){
        log.info("Actualizando imagen de producto por id: " + id);
        Producto productoActual = this.findById(id);
        String img = storageService.store(file);
        String urlImg = storageService.getUrl(img).replace(" ", "");
        if (!productoActual.getImagen().equals(Producto.IMAGE_DEFAULT)){
            storageService.delete(productoActual.getImagen());
        }
        productoActual.setImagen(urlImg);
        return productoRepository.save(productoActual);
    }
    @Override
    public void deleteById(String id) {
        log.debug("Borrando producto por id: " + id);
        Producto productoActual = this.findById(id);
        productoRepository.deleteById(productoActual.getId());
    }
}
