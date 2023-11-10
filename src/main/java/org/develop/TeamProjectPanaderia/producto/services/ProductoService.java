package org.develop.TeamProjectPanaderia.producto.services;

import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoService {
    Page<Producto> findAll(Optional<String> nombre, Optional <Integer> stockMin, Optional<Double> precioMax, Optional<Boolean> isActivo, Optional<String> categoria, Optional<String> proveedor, Pageable pageable);
    Producto findById(UUID id);
    Producto findByName(String name);
    Producto save(ProductoCreateDto productoCreateDto);
    Producto update(UUID id, ProductoUpdateDto productoUpdateDto);
    void deleteById(UUID id);
}

