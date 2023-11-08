package org.develop.TeamProjectPanaderia.producto.services;

import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductoService {
    List<Producto> findAll(String categoria);
    Producto findById(UUID id);
    Producto save(ProductoCreateDto productoCreateDto);
    Producto update(Long id, ProductoUpdateDto productoUpdateDto);
    void deleteById(Long id);
    Producto updateImage(Long id, MultipartFile image);
}


