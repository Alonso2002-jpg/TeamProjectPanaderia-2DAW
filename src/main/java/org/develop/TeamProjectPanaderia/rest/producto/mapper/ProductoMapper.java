package org.develop.TeamProjectPanaderia.rest.producto.mapper;



import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;

import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase encargada de mapear entre entidades y DTOs relacionados con productos.
 */
@Component
public class ProductoMapper {

    /**
     * Convierte un DTO de creación de producto a la entidad Producto.
     *
     * @param id        Identificador único del producto.
     * @param dto       DTO de creación de producto.
     * @param categoria Categoría del producto.
     * @param proveedor Proveedor del producto.
     * @return Instancia de Producto creada a partir del DTO.
     */
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

    /**
     * Convierte un DTO de actualización de producto a la entidad Producto.
     *
     * @param dto       DTO de actualización de producto.
     * @param producto  Producto existente.
     * @param categoria Nueva categoría del producto.
     * @param proveedor Nuevo proveedor del producto.
     * @return Instancia de Producto actualizada a partir del DTO.
     */
    public Producto toProducto(ProductoUpdateDto dto, Producto producto, Categoria categoria, Proveedor proveedor) {
        return Producto.builder()
                .id(producto.getId())
                .nombre(dto.nombre() != null ? dto.nombre() : producto.getNombre())
                .stock(dto.stock() != null ? dto.stock() : producto.getStock())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .imagen(producto.getImagen())
                .precio(dto.precio() != null ? dto.precio() : producto.getPrecio())
                .isActivo(dto.isActivo() != null ? dto.isActivo() : producto.getIsActivo())
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
    }

    /**
     * Convierte una entidad Producto a un DTO de respuesta de producto.
     *
     * @param producto Producto a convertir.
     * @return DTO de respuesta de producto.
     */
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

    /**
     * Convierte una página de entidades Producto a una página de DTOs de respuesta de producto.
     *
     * @param productPages Página de productos a convertir.
     * @return Página de DTOs de respuesta de producto.
     */
    public Page<ProductoResponseDto> toPageResponse(Page<Producto> productPages){
        return productPages.map(this::toProductoResponseDto);
    }
}








