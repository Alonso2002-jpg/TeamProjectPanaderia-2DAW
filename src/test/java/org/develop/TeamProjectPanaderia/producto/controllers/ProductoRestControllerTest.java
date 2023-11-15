package org.develop.TeamProjectPanaderia.producto.controllers;

/*
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.producto.controllers.ProductoRestController;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.rest.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.producto.services.ProductoService;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoRestControllerTest {
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
    private ProductoMapper productoMapper;
    @Mock
    private ProductoService productoService;
    @InjectMocks
    private ProductoRestController productoController;

    @Test
    void getAllProducts() {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  =  productoController.getAllProductos(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByNombre() {
        Optional<String> nombre = Optional.of("TEST-2");
        List<Producto> expectedProducts = List.of(producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByStockMin() {
        Optional<Integer> stockMin = Optional.of(40);
        List<Producto> expectedProducts = List.of(producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }


    @Test
    void getAllProducts_ByPrecioMax() {
        Optional<Double> precioMax = Optional.of(60.00);
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByIsActivo() {
        Optional<Boolean> isActivo = Optional.of(true);
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByCategoria() {
        Optional<String> categoria = Optional.of("PRODUCTO_TEST");
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByProveedor() {
        Optional<String> proveedor = Optional.of("Y7821803T");
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable)).thenReturn(page);


        ResponseEntity<PageResponse<ProductoResponseDto>> responseEntity  = productoController.getAllProductos(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, 0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable);
    }


    @Test
    void getProductById(){
        // Arrange
        String uuid = producto2.getId().toString();

        when(productoService.findById(uuid)).thenReturn(producto2);

        ResponseEntity<ProductoResponseDto> responseEntity = productoController.getProductoById(uuid);

        // Assert
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCode().value()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(producto1, responseEntity.getBody())
        );

        // verify
        verify(productoService, times(1)).findById(uuid);
    }

    @Test
    void getProductById_idNotExists() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String uuidFalso = uuid.toString();

        when(productoService.findById(uuidFalso)).thenThrow(new ProductoNotFound(uuid));

        // Act & Assert
       var result = assertThrows(ProductoNotFound.class, () -> productoController.getProductoById(uuidFalso));
       assertEquals("Producto con id " + uuid + " no encontrado", result.getMessage());

        // Verify
        verify(productoService, times(1)).findById(uuidFalso);
    }

    @Test
    void getProductById_InvalidadUuid() {
        // Arrange
        String uuidInvalido = "uuid_invalido";

        when(productoService.findById(uuidInvalido)).thenThrow(new ProductoBadUuid(uuidInvalido));

        // Act & Assert
        var result = assertThrows(ProductoBadUuid.class, () -> productoController.getProductoById(uuidInvalido));
        assertEquals("UUID: " + uuidInvalido + " no válido o de formato incorrecto", result.getMessage());

        // verify
        verify(productoService, times(1)).findById(uuidInvalido);
    }

    @Test
    void createProduct(){
        // Arrange
        UUID uuid = UUID.randomUUID();
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99,  true, categoriaProducto.getNameCategory(), proveedor.getNif());
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

        when(productoService.save(productoCreateDto)).thenReturn(expectedProduct);

        // Act
        ResponseEntity<ProductoResponseDto> responseEntity = productoController.createProduct(productoCreateDto);

        // Assert
        assertAll(
                () -> assertEquals(201, responseEntity.getStatusCode().value()),
                () -> assertEquals(expectedProduct, responseEntity.getBody())
        );

        // Verify
        verify(productoService, times(1)).save(productoCreateDto);
    }

    @Test
    void updateProduct() {
        // Arrange
        String id = producto1.getId().toString();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto1);

        // Act
        ResponseEntity<ProductoResponseDto> responseEntity = productoController.updateProduct(id, productoUpdateDto);

        // Assert
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCode().value()),
                () -> assertEquals(producto1, responseEntity.getBody())
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
    }

    @Test
    void updateProduct_NotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        when(productoService.update(any(), any())).thenThrow(new ProductoNotFound(uuid));

        // Act & Assert
        var result = assertThrows(ProductoNotFound.class, () -> productoController.updateProduct(any(), any()));
        assertEquals("Producto con id " +  uuid + " no encontrado", result.getMessage());

        // Verify
        verify(productoService, times(1)).update(any(), any());
    }

    @Test
    void updatePartialProduct() {
        // Arrange
        String id = producto2.getId().toString();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, null, null, null);

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto2);

        // Act
        ResponseEntity<ProductoResponseDto> responseEntity = productoController.updatePartialProduct(id, productoUpdateDto);

        // Assert
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCode().value()),
                () -> assertEquals(producto2, responseEntity.getBody())
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
    }

    @Test
    void deleteProducById()  {
        // Arrange
        UUID uuid = producto2.getId();
        doNothing().when(productoService).deleteById(uuid.toString());

        // Act
        ResponseEntity<Void> responseEntity = productoController.deleteProduct(uuid.toString());

        // Assert
        assertAll(() -> assertEquals(204, responseEntity.getStatusCode().value()));

        // Verify
        verify(productoService, times(1)).deleteById(uuid.toString());
    }

    @Test
    void deleteProductById_IdNotExist() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        doThrow(new ProductoNotFound(uuid)).when(productoService).deleteById(uuid.toString());

        // Act & Assert
        var result = assertThrows(ProductoNotFound.class, () -> productoController.deleteProduct(uuid.toString()));
        assertEquals("Producto con id " + uuid + " no encontrado", result.getMessage());

        // Verify
        verify(productoService, times(1)).deleteById(uuid.toString());
    }

    @Test
    void updateImage() {
        // Arrange
        String id = producto2.getId().toString();

        MultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                "image/jpeg",
                "contenido del archivo".getBytes()
        );

        when(productoService.updateImg(id, file)).thenReturn(producto2);

        // Act
        ResponseEntity<ProductoResponseDto> responseEntity = productoController.updateImage(id, file);

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(producto2, responseEntity.getBody());

        // Verify
        verify(productoService, times(1)).updateImg(id, file);
    }

    @Test
    void updateImage_InvalidFile() {
        // Arrange
        String id = producto1.getId().toString();

        MultipartFile invalidFile = new MockMultipartFile(
                "file",
                "filename.jpg",
                "image/jpeg",
                new byte[0]
        );

        var exception = assertThrows(ResponseStatusException.class, () -> productoController.updateImage(id, invalidFile));

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("400 BAD_REQUEST \"La Imagen no puede estar vacia\"", exception.getMessage());
    }
}






*/





