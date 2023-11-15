package org.develop.TeamProjectPanaderia.producto.mapper;



import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.models.Producto;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductoMapper {
    public Producto toProducto(UUID id, ProductoCreateDto dto, Categoria categoria, Proveedor proveedor) {
        return Producto.builder()
                .id(id)
                .nombre(dto.nombre())
                .stock(dto.stock() != null ? dto.stock() : 0)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .imagen(Producto.IMAGE_DEFAULT)
                .precio(dto.precio() != null ? dto.precio() : 0.0)
                .isActivo(dto.isActivo() != null ? dto.isActivo() : true)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
    }

    public Producto toProducto(ProductoUpdateDto dto, Producto producto, Categoria categoria, Proveedor proveedor) {
        return Producto.builder()
                .id(producto.getId())
                .nombre(dto.nombre() != null ? dto.nombre() : producto.getNombre())
                .stock(dto.stock() != null ? dto.stock() : producto.getStock())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .precio(dto.precio() != null ? dto.precio() : producto.getPrecio())
                .isActivo(dto.isActivo() != null ? dto.isActivo() : producto.getIsActivo())
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
    }

    public ProductoResponseDto toProductoResponseDto(Producto producto) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagen(),
                producto.getCategoria(),
                producto.getProveedor(),
                producto.getIsActivo()
        );
    }

    public Page<ProductoResponseDto> toPageResponse(Page<Producto> productPages){
        return productPages.map(this::toProductoResponseDto);
    }
}








