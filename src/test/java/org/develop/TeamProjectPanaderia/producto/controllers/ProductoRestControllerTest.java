package org.develop.TeamProjectPanaderia.producto.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.services.ProductoService;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class ProductoRestControllerTest {
    private final String myEndpoint = "/v1/producto";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductoService productoService;
    @Autowired
    private JacksonTester<ProductoCreateDto> jsonProductoCreateDto;
    @Autowired
    private JacksonTester <ProductoUpdateDto> jsonProductoUpdateDto;
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

    @Autowired
    public ProductoRestControllerTest(ProductoService productoService){
        this.productoService = productoService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllProducts() throws Exception {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByNombre() throws Exception {
        List<Producto> expectedProducts = List.of(producto1);
        String myLocalEndPoint = myEndpoint + "?nombre=TEST-1";
        Optional<String> nombre = Optional.of("TEST-1");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByStockMin() throws Exception {
        List<Producto> expectedProducts = List.of(producto2);
        String myLocalEndPoint = myEndpoint + "?stockMin=45";
        Optional<Integer> stockMin = Optional.of(45);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByPrecioMax() throws Exception {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        String myLocalEndPoint = myEndpoint + "?precioMax=50";
        Optional<Double> precioMax = Optional.of(45.0);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByIsActivo() throws Exception {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        String myLocalEndPoint = myEndpoint + "?isActivo=true";
        Optional<Boolean> isActivo = Optional.of(true);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByCategoria() throws Exception {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        String myLocalEndPoint = myEndpoint + "?categoria=PRODUCTO_TEST";
        Optional<String> categoria = Optional.of("PRODUCTO_TEST");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable);
    }

    @Test
    void getAllProducts_ByProveedor() throws Exception {
        List<Producto> expectedProducts = List.of(producto1, producto2);
        String myLocalEndPoint = myEndpoint + "?proveedor=Y7821803T";
        Optional<String> proveedor = Optional.of("Y7821803T");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Producto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable);
    }



    @Test
    void getProductById() throws Exception {
        // Arrange
        String uuid = producto2.getId().toString();
        String myLocalEndPoint = myEndpoint + "/" + uuid;

        when(productoService.findById(uuid)).thenReturn(producto2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto result = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, result)
        );

        // verify
        verify(productoService, times(1)).findById(uuid);
    }

    @Test
    void getProductById_idNotExists() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String uuidFalso = uuid.toString();
        String myLocalEndPoint = myEndpoint + "/" + uuidFalso;

        when(productoService.findById(uuidFalso)).thenThrow(new ProductoNotFound(uuid));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // verify
        verify(productoService, times(1)).findById(uuidFalso);
    }

    @Test
    void getProductById_InvalidadUuid() throws Exception {
        // Arrange
        String uuidInvalido = "uuid_invalido";
        String myLocalEndPoint = myEndpoint + "/" + uuidInvalido;

        when(productoService.findById(uuidInvalido)).thenThrow(new ProductoBadUuid(uuidInvalido));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // verify
        verify(productoService, times(1)).findById(uuidInvalido);
    }


    @Test
    void createProduct() throws Exception{
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

        when(productoService.save(productoCreateDto)).thenReturn(expectedProduct);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto result = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(expectedProduct, result)
        );

        // Verify
        verify(productoService, times(1)).save(productoCreateDto);
    }

    @Test
    void createProduct_BadRequest_Nombre() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto(null,33,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre no puede estar vacio"))
        );
    }


    @Test
    void createProduct_BadRequest_Stock() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto =  new ProductoCreateDto("nuevo_producto",-20,25.99, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo"))
        );
    }


    @Test
    void createProduct_BadRequest_Precio() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,-20.0, "test3.png" ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo"))
        );
    }


    @Test
    void createProduct_BadRequest_Categoria() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,20.0, "test3.png" ,  true, null, proveedor.getNif());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La categoria no puede estar vacia"))
        );
    }

    @Test
    void createProduct_BadRequest_Proveedor() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,20.0, "test3.png" ,  true, categoriaProducto.getNameCategory(), null);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El proveedor no puede estar vacia"))
        );
    }

    @Test
    void updateProduct() throws Exception {
        // Arrange
        String id = producto1.getId().toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto result = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, result)
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, "producto_actualizado.jpg", 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        // Arrange
        when(productoService.update(id, productoUpdateDto)).thenThrow(new ProductoNotFound(uuid));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void updateProduct_BadRequest_Stock() throws Exception {
        // Arrange
        UUID uuid = producto1.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("producto_actualizado", -100, "producto_actualizado.jpg", 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        // Arrange
        when(productoService.update(id, productoUpdateDto)).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo"))
        );
    }

    @Test
    void updateProduct_BadRequest_Precio() throws Exception {
        // Arrange
        UUID uuid = producto1.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("producto_actualizado", 100, "producto_actualizado.jpg", -80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        // Arrange
        when(productoService.update(id, productoUpdateDto)).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo"))
        );
    }

    @Test
    void updatePartialProduct() throws Exception {
        // Arrange
        String id = producto2.getId().toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, null, 80.99, null, null, null);

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto result = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto2, result)
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
    }

    @Test
    void deleteProducById() throws Exception {
        // Arrange
        UUID uuid = producto2.getId();
        String myLocalEndpoint = myEndpoint + "/" + uuid.toString();

        doNothing().when(productoService).deleteById(uuid.toString());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(() -> assertEquals(204, response.getStatus()));

        // Verify
        verify(productoService, times(1)).deleteById(uuid.toString());
    }

    @Test
    void deleteProductById_IdNotExist() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String myLocalEndpoint = myEndpoint + "/" + uuid.toString();

        doThrow(new ProductoNotFound(uuid)).when(productoService).deleteById(uuid.toString());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );

        // Verify
        verify(productoService, times(1)).deleteById(uuid.toString());
    }

    @Test
    void updateFunkoImage() throws Exception {
        UUID uuid = producto1.getId();
        var myLocalEndpoint = myEndpoint + "/imagen/" + uuid.toString();

        when(productoService.updateImg(anyString(), any(MultipartFile.class))).thenReturn(producto1);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        Producto result = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, result)
        );

        // Verify
        verify(productoService, times(1)).updateImg(anyString(), any(MultipartFile.class));
    }
}



