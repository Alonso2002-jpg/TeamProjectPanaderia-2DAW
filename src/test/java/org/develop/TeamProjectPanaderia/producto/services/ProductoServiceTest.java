package org.develop.TeamProjectPanaderia.producto.services;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    private final Categoria categoriaProducto = new Categoria(1L, "PRODUCTO_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Categoria categoriaProveedor = new Categoria(2L, "PROVEEDOR_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Proveedor proveedor = new Proveedor(1L, "Y7821803T", categoriaProveedor, "722663185", "Test S.L.", LocalDate.now(), LocalDate.now());
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
    private final Producto producto2 =
            Producto.builder()
                    .id(UUID.randomUUID())
                    .nombre("TEST-2")
                    .stock(50)
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .imagen("test2.png")
                    .precio(15.99)
                    .isActivo(true)
                    .categoria(categoriaProducto)
                    .proveedor(proveedor)
                    .build();
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriaService categoriaService;
    @Mock
    private ProveedorService proveedoresService;
    @Mock
    private ProductoMapper productoMapper;
    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void findAll_NotParameters(){
        // Arrange
        List<Producto> expectedProducts = List.of(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByNombre(){
        // Arrange
        Optional <String> nombre = Optional.of("TEST-1");
        List<Producto> expectedProducts = List.of(producto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByStockMin(){
        // Arrange
        Optional <Integer> stockMin = Optional.of(10);
        List<Producto> expectedProducts = List.of(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByPrecioMax(){
        // Arrange
        Optional <Double> precioMax = Optional.of(20.00);
        List<Producto> expectedProducts = List.of(producto1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByIsActivo(){
        // Arrange
        Optional <Boolean> isActivo = Optional.of(true);
        List<Producto> expectedProducts = List.of(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void findAll_ByCategoria(){
        // Arrange
        Optional <String> categoria = Optional.of("PRODUCTO_TEST");
        List<Producto> expectedProducts = List.of(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void findAll_ByProveedor(){
        // Arrange
        Optional <String> proveedor = Optional.of("Y7821803T");
        List<Producto> expectedProducts = List.of(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findById_IdExist() {
        // Arrange
        UUID uuid = producto1.getId();
        Producto expectedProduct = producto1;
        when(productoRepository.findById(uuid)).thenReturn(Optional.of(expectedProduct));

        // Act
        Producto actualProduct = productoService.findById(uuid.toString());

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(productoRepository, times(1)).findById(uuid);
    }

    @Test
    void findById_IdNotExist() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        when(productoRepository.findById(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.findById(uuid.toString()));
        assertEquals("Producto con id " + uuid + " no encontrado", res.getMessage());

        // Verify
        verify(productoRepository, times(1)).findById(uuid);
    }


    @Test
    void findById_InvalidUuid() {
        // Arrange
        String uuid = "uuid_falso";

        // Act & Assert
        var res = assertThrows(ProductoBadUuid.class, () -> productoService.findById(uuid));
        assertEquals("UUID: " + uuid + " no válido o de formato incorrecto", res.getMessage());

        // Verify
        verify(productoRepository, times(0)).findById(any(UUID.class));
    }


    @Test
    void save_() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNIF());
        Producto expectedProduct = Producto.builder()
                        .id(uuid)
                        .nombre("nuevo_producto")
                        .stock(33)
                        .fechaCreacion(LocalDateTime.now())
                        .fechaActualizacion(LocalDateTime.now())
                        .imagen("test3.png")
                        .precio(25.99)
                        .isActivo(true)
                        .categoria(categoriaProducto)
                        .proveedor(proveedor)
                        .build();

        when(categoriaService.findByName(productoCreateDto.categoria())).thenReturn(categoriaProducto);
        when(productoMapper.toProducto(uuid, productoCreateDto, categoriaProducto, proveedor)).thenReturn(expectedProduct);
        when(productoRepository.save(expectedProduct)).thenReturn(expectedProduct);

        // Act
        Producto actualProduct = productoService.save(productoCreateDto);

        // Assert
        assertEquals(expectedProduct, actualProduct);


        // Verify
        verify(categoriaService, times(1)).findByName(productoCreateDto.categoria());
        verify(productoRepository, times(1)).save(expectedProduct);
        verify(productoMapper, times(1)).toProducto(uuid, productoCreateDto, categoriaProducto, proveedor);
    }
}
