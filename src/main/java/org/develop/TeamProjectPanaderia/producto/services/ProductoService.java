package org.develop.TeamProjectPanaderia.producto.services;

import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductoService {
    List<Producto> findAll(String categoria, String proveedor);
    Producto findById(UUID id);
    Producto findByName(String name);
    Producto save(ProductoCreateDto productoCreateDto);
    Producto update(UUID id, ProductoUpdateDto productoUpdateDto);
    Producto updateImg(UUID id, MultipartFile file);
    void deleteById(UUID id);
}


