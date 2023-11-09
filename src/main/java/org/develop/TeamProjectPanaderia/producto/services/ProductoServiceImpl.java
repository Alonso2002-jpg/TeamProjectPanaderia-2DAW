package org.develop.TeamProjectPanaderia.producto.services;

import lombok.extern.slf4j.Slf4j;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProveedoresService proveedoresService;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaService categoriaService, ProveedoresService proveedoresService, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.proveedoresService = proveedoresService;
        this.productoMapper = productoMapper;
    }

    @Override
    public List<Producto> findAll(String categoria, String proveedor) {
        if((categoria == null || categoria.isEmpty()) && (proveedor == null || proveedor.isEmpty())){
            log.info("Buscando todos los productos: ");
            return productoRepository.findAll();
        }
        if((categoria != null && !categoria.isEmpty()) && (proveedor == null || proveedor.isEmpty())) {
            log.info("Buscando todos los productos por categoria: " + categoria);
            return productoRepository.findAllByCategoriaContainsIgnoreCase(categoria);
        }
        if((categoria == null || categoria.isEmpty())){
            log.info("Buscando todos los productos por proveedor: " + proveedor);
            return productoRepository.findAllByProveedorContainsIgnoreCase(proveedor);
        }
        log.info("Buscando todos los productos por categoria: " + categoria + " y proveedor: " + proveedor);
        return productoRepository.findAllByCategoriaContainsIgnoreCaseAndProveedorContainsIgnoreCase(categoria, proveedor);
    }

    @Override
    public Producto findById(UUID id) {
        log.info("Buscando producto por id: " + id);
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFound(id));
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
        Proveedores proveedores = proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor());
        UUID id = UUID.randomUUID();
        return productoRepository.save(productoMapper.toProducto(id,productoCreateDto, categoria, proveedores));
    }

    @Override
    public Producto update(UUID id, ProductoUpdateDto productoUpdateDto) {
       log.info("Actualizando producto por id: " + id);
       Producto productoActual = this.findById(id);
       Categoria categoria = null;
       Proveedores proveedor = null;
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
    public void deleteById(UUID id) {
        log.debug("Borrando producto por id: " + id);
        this.findById(id);
        productoRepository.deleteById(id);
    }
}
