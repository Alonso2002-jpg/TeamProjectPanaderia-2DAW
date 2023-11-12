package org.develop.TeamProjectPanaderia.producto.services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorService;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
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
    WebSocketHandler webSocketHandlerMock = mock (WebSocketHandler.class);
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
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private NotificacionMapper<Producto> funkoNotificationMapper;

    @BeforeEach
    void setUp(){
        productoService.setWebSocketService(webSocketHandlerMock);
    }

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
    void findByName(){
        // Arrange
        String nombre = producto1.getNombre();
        Producto expectedProduct = producto1;
        when(productoRepository.findByNombreEqualsIgnoreCase(nombre)).thenReturn(Optional.of(expectedProduct));

        // Act
        Producto actualProduct = productoService.findByName(nombre);

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(productoRepository, times(1)).findByNombreEqualsIgnoreCase(nombre);
    }

    @Test
    void findByName_NameNotExist(){
        // Arrange
        String nombreNoExiste = "nombre_falso";

        when(productoRepository.findByNombreEqualsIgnoreCase(nombreNoExiste)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.findByName(nombreNoExiste));
        assertEquals("Producto con nombre " + nombreNoExiste + " no encontrado", res.getMessage());

        // Verify
        verify(productoRepository, times(1)).findByNombreEqualsIgnoreCase(nombreNoExiste);
    }

    @Test
    void save_() throws IOException {
        // Arrange
        UUID uuid = UUID.randomUUID();
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());
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

        when(proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor())).thenReturn(proveedor);
        when(categoriaService.findByName(productoCreateDto.categoria())).thenReturn(categoriaProducto);
        when(productoMapper.toProducto(any(UUID.class), eq(productoCreateDto), eq(categoriaProducto), eq(proveedor))).thenReturn(expectedProduct);
        when(productoRepository.save(expectedProduct)).thenReturn(expectedProduct);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        Producto actualProduct = productoService.save(productoCreateDto);

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(proveedoresService, times(1)).findProveedoresByNIF(productoCreateDto.proveedor());
        verify(categoriaService, times(1)).findByName(productoCreateDto.categoria());
        verify(productoRepository, times(1)).save(expectedProduct);
        verify(productoMapper, times(1)).toProducto(any(UUID.class), eq(productoCreateDto), eq(categoriaProducto), eq(proveedor));
    }

    @Test
    void save_categoryNotExist(){
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(categoriaService.findByName(productoCreateDto.categoria())).thenThrow(new CategoriaNotFoundException(productoCreateDto.categoria()));

        // Act
        var res = assertThrows(CategoriaNotFoundException.class, () -> productoService.save(productoCreateDto));
        assertEquals("Categoria not found with " + productoCreateDto.categoria(), res.getMessage());

        // Verift
        verify(categoriaService, times(1)).findByName(productoCreateDto.categoria());
    }
/*
    @Test
    void save_proveedorNotExist(){
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor())).thenThrow(new ProveedoresNotFoundException(productoCreateDto.proveedor()));

        // Act
        var res = assertThrows(CategoriaNotFoundException.class, () -> productoService.save(productoCreateDto));
        assertEquals("Proveedores con nombre: " + productoCreateDto.proveedor() + "No encontrado", res.getMessage());

        // Verify
        verify(proveedoresService, times(1)).findProveedoresByNIF(productoCreateDto.categoria());
    }
*/


    @Test
    void update() throws IOException {
        // Arrange
        UUID id = producto1.getId();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto1));
        when(productoRepository.save(producto1)).thenReturn(producto1);
        when(productoMapper.toProducto(productoUpdateDto, producto1, categoriaProducto, proveedor)).thenReturn(producto1);
        when(categoriaService.findByName(productoUpdateDto.categoria())).thenReturn(categoriaProducto);
        when(proveedoresService.findProveedoresByNIF(productoUpdateDto.proveedor())).thenReturn(proveedor);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        Producto productoActualizado = productoService.update(id.toString(), productoUpdateDto);

        // Assert
        assertAll(
                () -> assertNotNull(productoActualizado),
                () -> assertEquals(producto1, productoActualizado)
        );

        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(producto1);
        verify(categoriaService, times(1)).findByName(productoUpdateDto.categoria());
        verify(proveedoresService, times(1)).findProveedoresByNIF(productoUpdateDto.proveedor());
        verify(productoMapper, times(1)).toProducto(productoUpdateDto, producto1, categoriaProducto, proveedor);
    }

    @Test
    void update_WithNotCategoryAndNotProveedor() throws IOException {
        // Arrange
        UUID id = producto1.getId();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, "", "");

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto1));
        when(productoRepository.save(producto1)).thenReturn(producto1);
        when(productoMapper.toProducto(productoUpdateDto, producto1, categoriaProducto, proveedor)).thenReturn(producto1);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        Producto productoActualizado = productoService.update(id.toString(), productoUpdateDto);

        // Assert
        assertAll(
                () -> assertNotNull(productoActualizado),
                () -> assertEquals(producto1, productoActualizado)
        );

        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(producto1);
        verify(productoMapper, times(1)).toProducto(productoUpdateDto, producto1, categoriaProducto, proveedor);
    }

    @Test
    void update_IdNotExist() {
        // Arrange
        UUID id = producto1.getId();
        String uuid = id.toString();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.update(uuid,productoUpdateDto));
        assertEquals("Producto con id " + uuid + " no encontrado", res.getMessage());

        verify(productoRepository, times(1)).findById(id);
    }


    @Test
    void update_CategoryNotExist() {
        // Arrange
        String category = "Categoria_Falsa";
        UUID id = producto1.getId();
        String uuid = id.toString();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, "Categoria_Falsa", proveedor.getNif());

        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.of(producto1));
        when(categoriaService.findByName(category)).thenThrow(new CategoriaNotFoundException(category));

        // Act & Assert
        var res = assertThrows(CategoriaNotFoundException.class, () -> productoService.update(uuid, productoUpdateDto));
        assertEquals("Categoria not found with " + category, res.getMessage());

        // Verify
        verify(productoRepository, times(1)).findById(id);
        verify(categoriaService, times(1)).findByName(category);
    }

/*
    @Test
    void update_ProveedorNotExist() {
        // Arrange
        String proveedor = "Proveedor_Falso";
        UUID id = producto1.getId();
        String uuid = id.toString();
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, "Categoria_Falsa", proveedor.getNif());

        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.of(producto1));
        when(categoriaService.findByName(categoriaProducto.getNameCategory())).thenReturn(categoriaProducto);
        when(proveedoresService.findProveedoresByNIF(proveedor)).thenThrow(new ProveedoresNotFoundException(proveedor.getNif()));

        // Act & Assert
        var res = assertThrows(ProveedoresNotFoundException.class, () -> productoService.update(uuid, productoUpdateDto));
        assertEquals("Proveedores con nif: " + proveedor.getNif() + " No encontrado", res.getMessage());
    }
    */


    @Test
    void deleteById() throws IOException {
        // Arrange
        UUID id = producto2.getId();
        String uuid = id.toString();

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto2));
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        productoService.deleteById(uuid);

        verify(productoRepository, times(1)).deleteById(id);
    }


    @Test
    void deleteById_idNotExist(){
        // Arrange
        UUID id = producto2.getId();
        String uuid = id.toString();

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.deleteById(uuid));
        assertEquals("Producto con id " + id + " no encontrado", res.getMessage());

        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(0)).deleteById(id);
    }



    /*
    @Test
    void onChange() throws IOException {
        // Arrange
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));

        // Act
        productoService.onChange(Notificacion.Tipo.CREATE, any(Producto.class));
    }
    */

    @Test
    void updateImage() throws IOException {
        // Arrange
        String imageUrl = "test1.png";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(productoRepository.findById(producto1.getId())).thenReturn(Optional.of(producto1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when((storageService.getUrl(imageUrl))).thenReturn(imageUrl);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);
        doNothing().when(storageService).delete(imageUrl);
        doNothing().when(webSocketHandlerMock).sendMessage(anyString());


        // Act
        Producto updatedProducto = productoService.updateImg(producto1.getId().toString(), multipartFile);

        // Assert
        assertEquals(updatedProducto.getImagen(), imageUrl);
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(storageService, times(1)).delete(producto1.getImagen());
        verify(storageService, times(1)).store(multipartFile);
    }
}
