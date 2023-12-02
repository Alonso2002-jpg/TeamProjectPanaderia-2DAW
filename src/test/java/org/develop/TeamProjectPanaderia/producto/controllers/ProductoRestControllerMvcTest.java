package org.develop.TeamProjectPanaderia.producto.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class ProductoRestControllerMvcTest {
    private final String myEndpoint = "/v1/producto";
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private final ProductoMapper productoMapper;
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
    private final Proveedor proveedor = new Proveedor(1L, "Y7821803T", categoriaProveedor, "722663185", "Test S.L.", true, LocalDate.now(), LocalDate.now());
    private final Producto producto1 = new Producto(UUID.randomUUID(), "TEST-1", 10, LocalDateTime.now(), LocalDateTime.now(), "test1.png", 49.99, true, categoriaProducto, proveedor);
    private final Producto producto2 = new Producto(UUID.randomUUID(), "TEST-2", 50, LocalDateTime.now(), LocalDateTime.now(), "test2.png", 15.99, true, categoriaProducto, proveedor);
    private final ProductoResponseDto responseDtoProduct1 = new ProductoResponseDto(producto1.getId(), "TEST-1", 49.99, 10, "test1.png", categoriaProducto, proveedor, true);
    private final ProductoResponseDto responseDtoProduct2 = new ProductoResponseDto(producto2.getId(), "TEST-2", 15.99, 50, "test2.png", categoriaProducto, proveedor, true);
    @Autowired
    public ProductoRestControllerMvcTest(ProductoMapper productoMapper, ProductoService productoService){
        this.productoMapper = productoMapper;
        this.productoService = productoService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllProducts() throws Exception {
        List <Producto> productoList = List.of(producto1, producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct1, responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllProducts_ByNombre() throws Exception {
        String myLocalEndPoint = myEndpoint + "?nombre=TEST-1";
        Optional<String> nombre = Optional.of("TEST-1");
        List <Producto> productoList = List.of(producto1);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllProducts_ByStockMin() throws Exception {
        String myLocalEndPoint = myEndpoint + "?stockMin=45";
        Optional<Integer> stockMin = Optional.of(45);
        List <Producto> productoList = List.of(producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), stockMin, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllProducts_ByPrecioMax() throws Exception {
        String myLocalEndPoint = myEndpoint + "?precioMax=50.0";
        Optional<Double> precioMax = Optional.of(50.0);
        List <Producto> productoList = List.of(producto1, producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct2, responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), precioMax, Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllProducts_ByIsActivo() throws Exception {
        String myLocalEndPoint = myEndpoint + "?isActivo=true";
        Optional<Boolean> isActivo = Optional.of(true);
        List <Producto> productoList = List.of(producto1, producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct2, responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), isActivo, Optional.empty(), Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllProducts_ByCategoria() throws Exception {
        String myLocalEndPoint = myEndpoint + "?categoria=PRODUCTO_TEST";
        Optional<String> categoria = Optional.of("PRODUCTO_TEST");
        List <Producto> productoList = List.of(producto1, producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct2, responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), categoria, Optional.empty(), pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }


    @Test
    void getAllProducts_ByProveedor() throws Exception {
        String myLocalEndPoint = myEndpoint + "?proveedor=Y7821803T";
        Optional<String> proveedor = Optional.of("Y7821803T");
        List <Producto> productoList = List.of(producto1, producto2);
        List <ProductoResponseDto> expectedProducts = List.of(responseDtoProduct2, responseDtoProduct2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page <Producto> responsePage = new PageImpl<>(productoList);
        Page <ProductoResponseDto> responseDto = new PageImpl<>(expectedProducts);

        // Arrange
        when(productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable)).thenReturn(responsePage);
        when(productoMapper.toPageResponse(responsePage)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ProductoResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(productoService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), proveedor, pageable);
        verify(productoMapper, times(1)).toPageResponse(responsePage);
    }




    @Test
    void getProductById() throws Exception {
        // Arrange
        String uuid = producto2.getId().toString();
        String myLocalEndPoint = myEndpoint + "/" + uuid;

        when(productoService.findById(uuid)).thenReturn(producto2);
        when(productoMapper.toProductoResponseDto(producto2)).thenReturn(responseDtoProduct2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ProductoResponseDto result = mapper.readValue(response.getContentAsString(), ProductoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(responseDtoProduct2, result)
        );

        // verify
        verify(productoService, times(1)).findById(uuid);
        verify(productoMapper, times(1)).toProductoResponseDto(producto2);
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
    void getProductById_InvalidUuid() throws Exception {
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
        assertEquals(400, response.getStatus());

        // verify
        verify(productoService, times(1)).findById(uuidInvalido);
    }


    @Test
    void createProduct() throws Exception{
        // Arrange
        UUID uuid = UUID.randomUUID();
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,25.99 ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoService.save(productoCreateDto)).thenReturn(producto1);
        when(productoMapper.toProductoResponseDto(producto1)).thenReturn(responseDtoProduct1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ProductoResponseDto result = mapper.readValue(response.getContentAsString(), ProductoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(responseDtoProduct1, result)
        );

        // Verify
        verify(productoService, times(1)).save(productoCreateDto);
        verify(productoMapper, times(1)).toProductoResponseDto(producto1);
    }


    @Test
    void createProduct_BadRequest_NombreIsNull() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto(null,33,25.99,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
    void createProduct_BadRequest_NombreInvalid() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("PR",33,25.99,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoCreateDto.write(productoCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe contener al menos 3 letras"))
        );
    }


    @Test
    void createProduct_BadRequest_Stock() throws Exception {
        // Arrange
        ProductoCreateDto productoCreateDto =  new ProductoCreateDto("nuevo_producto",-20,25.99 ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,-20.0 ,  true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,20.0 ,  true, null, proveedor.getNif());

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
        ProductoCreateDto productoCreateDto = new ProductoCreateDto("nuevo_producto",33,20.0 ,  true, categoriaProducto.getNameCategory(), null);

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
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto1);
        when(productoMapper.toProductoResponseDto(producto1)).thenReturn(responseDtoProduct1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ProductoResponseDto result = mapper.readValue(response.getContentAsString(), ProductoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(responseDtoProduct1, result)
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
        verify(productoMapper, times(1)).toProductoResponseDto(producto1);
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
    void updateProduct_BadRequest_Nombre() throws Exception {
        // Arrange
        UUID uuid = producto2.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("pr", 100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
                () -> assertTrue(response.getContentAsString().contains("El nombre debe contener al menos 3 letras"))
        );
    }

    @Test
    void updateProduct_BadRequest_Stock() throws Exception {
        // Arrange
        UUID uuid = producto1.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("producto_actualizado", -100, 80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
        UUID uuid = producto2.getId();
        String id = uuid.toString();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("producto_actualizado", 100, -80.99, true, categoriaProducto.getNameCategory(), proveedor.getNif());

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
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("ProductoActualizado", 100, 80.99, null, null, null);

        when(productoService.update(id, productoUpdateDto)).thenReturn(producto2);
        when(productoMapper.toProductoResponseDto(producto2)).thenReturn(responseDtoProduct2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonProductoUpdateDto.write(productoUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ProductoResponseDto result = mapper.readValue(response.getContentAsString(), ProductoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(responseDtoProduct2, result)
        );

        // Verify
        verify(productoService, times(1)).update(id, productoUpdateDto);
    }

    @Test
    void deleteProducById() throws Exception {
        // Arrange
        UUID uuid = producto1.getId();
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
    void updateProductImage() throws Exception {
        String id = producto2.getId().toString();
        var myLocalEndpoint = myEndpoint + "/image/" + id;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        when(productoService.updateImg(id, file)).thenReturn(producto2);
        when(productoMapper.toProductoResponseDto(producto2)).thenReturn(responseDtoProduct2);

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        ProductoResponseDto result = mapper.readValue(response.getContentAsString(), ProductoResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(responseDtoProduct2, result)
        );

        // Verify
        verify(productoService, times(1)).updateImg(id, file);
    }
}

