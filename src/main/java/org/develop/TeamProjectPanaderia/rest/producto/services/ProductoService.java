package org.develop.TeamProjectPanaderia.rest.producto.services;

import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface ProductoService {
    Page<Producto> findAll(Optional<String> nombre, Optional <Integer> stockMin, Optional<Double> precioMax, Optional<Boolean> isActivo, Optional<String> categoria, Optional<String> proveedor, Pageable pageable);
    Producto findById(String id);
    Producto findByName(String name);
    Producto save(ProductoCreateDto productoCreateDto);
    Producto update(String id, ProductoUpdateDto productoUpdateDto);
    Producto updateImg(String id, MultipartFile file);
    void deleteById(String id);
}

