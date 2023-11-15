package org.develop.TeamProjectPanaderia.producto.mappers;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoMapperTest {
    private final ProductoMapper productoMapper = new ProductoMapper();
    private final Categoria categoriaProducto = new Categoria(1L, "PRODUCTO_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Categoria categoriaProveedor = new Categoria(2L, "PROVEEDOR_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Proveedor proveedor = new Proveedor(1L, "Y7821803T", categoriaProveedor, "722663185", "Test S.L.", true, LocalDate.now(), LocalDate.now());
    private final Producto producto1 =
            Producto.builder()
                    .id(UUID.randomUUID())
                    .nombre("TEST-1")
                    .stock(10)
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .imagen("test1.png")
                    .precio(49.99)
                    .isActivo(true)
                    .categoria(categoriaProducto)
                    .proveedor(proveedor)
                    .build();

    @Test
    void testToProduct_create() {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());
        UUID id = UUID.randomUUID();

        // Act
        Producto nuevoProducto = productoMapper.toProducto(id, productoCreateDto, categoriaProducto, proveedor);

        // Assert
        assertAll(
                () -> assertNotNull(nuevoProducto.getId()),
                () -> assertEquals(id, nuevoProducto.getId()),
                () -> assertEquals(productoCreateDto.nombre(), nuevoProducto.getNombre()),
                () -> assertEquals(productoCreateDto.proveedor(), nuevoProducto.getProveedor().getNif()),
                () -> assertEquals(productoCreateDto.precio(), nuevoProducto.getPrecio()),
                () -> assertEquals(productoCreateDto.stock(), nuevoProducto.getStock()),
                () -> assertEquals(productoCreateDto.categoria(), nuevoProducto.getCategoria().getNameCategory()),
                () -> assertNotNull(nuevoProducto.getFechaActualizacion()),
                () -> assertNotNull(nuevoProducto.getFechaCreacion()),
                () -> assertTrue(nuevoProducto.getIsActivo()),
                () -> assertNotNull(nuevoProducto.getImagen())
        );
    }

    @Test
    void testToProducto_update(){
        // Arrange
        Producto productoExistente = producto1;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        // Act
        Producto productoActualizado = productoMapper.toProducto(productoUpdateDto, producto1, categoriaProducto, proveedor);

        // Assert
        assertAll(
                () -> assertEquals(productoExistente.getId(), productoActualizado.getId()),
                () -> assertEquals(productoUpdateDto.nombre(), productoActualizado.getNombre()),
                () -> assertEquals(productoUpdateDto.proveedor(), productoActualizado.getProveedor().getNif()),
                () -> assertEquals(productoUpdateDto.precio(), productoActualizado.getPrecio()),
                () -> assertEquals(productoUpdateDto.stock(), productoActualizado.getStock()),
                () -> assertEquals(productoUpdateDto.categoria(), productoActualizado.getCategoria().getNameCategory()),
                () -> assertEquals(productoUpdateDto.isActivo(), productoActualizado.getIsActivo()),
                () -> assertEquals(productoExistente.getFechaCreacion(), productoActualizado.getFechaCreacion()),
                () -> assertNotEquals(productoExistente.getFechaActualizacion(), productoActualizado.getFechaActualizacion()),
                () -> assertNotNull(productoActualizado.getImagen())
        );
    }


    @Test
    void toResponseDtoTest() {
        // Arrange
        Producto producto = producto1;

        // Act
        ProductoResponseDto productoResponseDto = productoMapper.toProductoResponseDto(producto);

        // Assert
        assertAll(
                () -> assertEquals(producto.getNombre(), productoResponseDto.nombre()),
                () -> assertEquals(producto.getPrecio(), productoResponseDto.precio()),
                () -> assertEquals(producto.getStock(), productoResponseDto.stock()),
                () -> assertEquals(producto.getImagen(), productoResponseDto.imagen()),
                () -> assertEquals(producto.getCategoria(), productoResponseDto.categoria()),
                () -> assertEquals(producto.getProveedor(), productoResponseDto.proveedor()),
                () -> assertEquals(producto.getId(), productoResponseDto.id())
        );
    }
}
