package org.develop.TeamProjectPanaderia.producto.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.Proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.Proveedores.services.ProveedoresService;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            return productoRepository.findAllByCategoriaContainingIgnoreCase(categoria);
        }
        if((categoria == null || categoria.isEmpty())){
            log.info("Buscando todos los productos por proveedor: " + proveedor);
            return productoRepository.findAllByProveedorContainingIgnoreCase(proveedor);
        }
        log.info("Buscando todos los productos por categoria: " + categoria + " y proveedor: " + proveedor);
        return productoRepository.findAllByCategoriaContainingIgnoreCaseAndProveedorContainingIgnoreCase(categoria, proveedor);
    }

    @Override
    public Producto findById(UUID id) {
        log.info("Buscando producto por id: " + id);
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFound(id));
    }


    @Override
    public Producto findByName(String nombre) {
        log.info("Buscando producto por nombre: " + nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre).orElseThrow(() -> new ProductoNotFound(nombre));
    }


    @Override
    public Producto save(ProductoCreateDto productoCreateDto) {
        log.info("Guardando producto: " + productoCreateDto);
        Categoria categoria = categoriaService.f(productoCreateDto.categoria());
        Proveedores proveedores = proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor());
        return productoRepository.save(productoMapper.toProducto(UUID.randomUUID(),productoCreateDto, categoria, proveedores));
    }

    @Override
    public Producto update(UUID id, ProductoUpdateDto productoUpdateDto) {
       log.info("Actualizando producto por id: " + id);
       Producto productoActual = this.findById(id);
       Categoria categoria = null;
       Proveedores proveedor = null;
       if(productoUpdateDto.categoria() != null )
    }



    @Override
    public Funko update(Long id, FunkoUpdateDto funkoUpdateDto) {
        log.info("Actualizando funko por id: " + id);
        Funko funkoActual = this.findById(id);
        Categoria categoria = null;
        if(funkoUpdateDto.getCategoria() != null && !funkoUpdateDto.getCategoria().isEmpty()){
            categoria = categoriasService.findByNombre(funkoUpdateDto.getCategoria());
        } else {
            categoria = funkoActual.getCategoria();
        }
        var funkoUpdated = funkosRepository.save(funkoMapper.toFunko(funkoUpdateDto, funkoActual, categoria));

        onChange(Notificacion.Tipo.UPDATE, funkoUpdated);

        return funkoUpdated;
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Borrando producto por id: " + id);
        this.findById(id);
    }

    @Override
    public Producto updateImage(Long id, MultipartFile image) {
        return null;
    }
}




}