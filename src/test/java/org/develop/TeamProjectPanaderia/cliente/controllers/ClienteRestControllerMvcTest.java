package org.develop.TeamProjectPanaderia.cliente.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.cliente.services.ClienteService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
public class ClienteRestControllerMvcTest {
    private final String myEndpoint = "/v1/cliente";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ClienteService clienteService;
    @Autowired
    private JacksonTester<ClienteCreateDto> jsonClienteCreateDto;
    @Autowired
    private JacksonTester <ClienteUpdateDto> jsonClienteUpdateDto;
    Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1_LOLA")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .build();
    private final Cliente cliente2 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST2_LILA")
                    .correo("test2@gmail.com")
                    .dni("03480731B")
                    .telefono("602697971")
                    .imagen("test2.jpg")
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .build();

    @Autowired
    public ClienteRestControllerMvcTest(ClienteService clienteService){
        this.clienteService = clienteService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllClientes() throws Exception{
        List<Cliente> expecCliente = List.of(cliente1,cliente2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expecCliente);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(),pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Cliente> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), Optional.empty(),  pageable);
    }

    @Test
    void getAllClientes_ByNombreCompleto() throws Exception {
        List<Cliente> expecCliente = List.of(cliente1);
        String myLocalEndPoint = myEndpoint + "?nombreCompleto=TEST1_LOLA";
        Optional<String> nombreCompleto = Optional.of("TEST1_LOLA");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expecCliente);

        // Arrange
        when(clienteService.findAll(nombreCompleto, Optional.empty(),  pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Cliente> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        // Verify
        verify(clienteService, times(1)).findAll(nombreCompleto, Optional.empty(), pageable);
    }

    @Test
    void getAllCliente_ByCategoria() throws Exception {
        List<Cliente> expecCliente = List.of(cliente1, cliente2);
        String myLocalEndPoint = myEndpoint + "?categoria=CLIENTE_TEST";
        Optional<String> categoria = Optional.of("CLIENTE_TEST");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expecCliente);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Cliente> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), Optional.empty(), pageable);
    }

    @Test
    void getClientById() throws Exception {
        // Arrange
        Long id = cliente2.getId();
        String myLocalEndPoint = myEndpoint + "/" + id;

        when(clienteService.findById(id)).thenReturn(cliente2);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Cliente result = mapper.readValue(response.getContentAsString(), Cliente.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(cliente1, result)
        );

        // verify
        verify(clienteService, times(1)).findById(id);
    }

    @Test
    void getClienteById_NotExists() throws Exception {
        // Arrange
        Long id = 99L;
        String myLocalEndPoint = myEndpoint + "/" + id;

        when(clienteService.findById(id)).thenThrow(new ClienteNotFoundException(id));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // verify
        verify(clienteService, times(1)).findById(id);
    }

    @Test
    void createCliente() throws Exception{
        // Arrange
        Long id = 1L;
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("TEST3_MARIA","test3@gmail.com","03480731A", "602697979" ,  "test3.jpg",categoriaCliente.getNameCategory(),true);
        Cliente expecCliente = Cliente.builder()
                .id(id)
                .nombreCompleto("TEST3_MARIA")
                .correo("test3@gmail.com")
                .dni("03480731A")
                .telefono("602697979")
                .imagen("test3.jpg")
                .categoria(categoriaCliente)
                .isActive(true)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(clienteService.save(clienteCreateDto)).thenReturn(expecCliente);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Cliente result = mapper.readValue(response.getContentAsString(), Cliente.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(expecCliente, result)
        );

        // Verify
        verify(clienteService, times(1)).save(clienteCreateDto);
    }

    @Test
    void createCliente_BadRequest_NombreCompletoIsNull() throws Exception {
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto(null,"test3@gmail.com","03480731A", "602697979" ,  "test3.jpg",categoriaCliente.getNameCategory(),true);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre no puede estar vacio"))
        );
    }

    @Test
    void createCliente_BadRequest_NombreCompletoInvalid() throws Exception {
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("cl","test3@gmail.com","03480731A", "602697979" ,  "test3.jpg",categoriaCliente.getNameCategory(),true);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe tener al menos 8 caracteres"))
        );
    }

    @Test
    void createCliente_BadRequest_Categoria() throws Exception {
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("EvelynObando","test3@gmail.com","03480731A", "602697979" ,  "test3.jpg", null, true);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteCreateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La categoria no puede estar vacia"))
        );
    }

    @Test
    void updateCliente() throws Exception {
        // Arrange
        Long id = cliente1.getId();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ClienteUpdateDto clienteUpdateDto =  ClienteUpdateDto.builder()
                .nombreCompleto("TEST4_PEPITO")
                .correo("test4@gmail.com")
                .telefono("602692079")
                .imagen("test4.jpg")
                .build();

        when(clienteService.update(id, clienteUpdateDto)).thenReturn(cliente1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteUpdateDto.write(clienteUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Cliente result = mapper.readValue(response.getContentAsString(), Cliente.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(cliente1, result)
        );

        // Verify
        verify(clienteService, times(1)).update(id, clienteUpdateDto);
    }

    @Test
    void updateCliente_NotFound() throws Exception {
        // Arrange
        Long id = -99L;
        String myLocalEndpoint = myEndpoint + "/" + id;
        ClienteUpdateDto clienteUpdateDto = ClienteUpdateDto.builder()
                .nombreCompleto("TEST4_PEPITO")
                .correo("test4@gmail.com")
                .telefono("602692079")
                .imagen("test4.jpg")
                .build();

        // Arrange
        when(clienteService.update(id, clienteUpdateDto)).thenThrow(new ClienteNotFoundException(id));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteUpdateDto.write(clienteUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void deleteClienteById() throws Exception {
        // Arrange
        Long id = cliente2.getId();
        String myLocalEndpoint = myEndpoint + "/" + id;

        doNothing().when(clienteService).deleteById(id);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(() -> assertEquals(204, response.getStatus()));

        // Verify
        verify(clienteService, times(1)).deleteById(id);
    }

    @Test
    void deleteClienteById_IdNotExist() throws Exception {
        // Arrange
        Long id = 99L;
        String myLocalEndpoint = myEndpoint + "/" + id;

        doThrow(new ClienteNotFoundException(id)).when(clienteService).deleteById(id);

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
        verify(clienteService, times(1)).deleteById(id);
    }

    @Test
    void updateClienteImage() throws Exception {
        Long id = cliente1.getId();
        var myLocalEndpoint = myEndpoint + "/imagen/" + id.toString();

        when(clienteService.updateImg(id,any(MultipartFile.class))).thenReturn(cliente1);

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


        Cliente result = mapper.readValue(response.getContentAsString(), Cliente.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(cliente1, result)
        );

        // Verify
        verify(clienteService, times(1)).updateImg(id, any(MultipartFile.class));
    }
}
