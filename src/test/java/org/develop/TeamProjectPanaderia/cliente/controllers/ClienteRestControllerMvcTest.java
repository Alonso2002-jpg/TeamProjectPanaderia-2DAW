package org.develop.TeamProjectPanaderia.cliente.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class ClienteRestControllerMvcTest {
    private final String myEndpoint = "/v1/cliente";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private final ClienteMapper clienteMapper;
    @MockBean
    private ClienteService clienteService;
    @Autowired
    private JacksonTester<ClienteCreateDto> jsonClienteCreateDto;
    @Autowired
    private JacksonTester<ClienteUpdateDto> jsonClienteUpdateDto;
    Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Direccion direccion = new Direccion("Calle", "Numero", "Ciudad", "Provincia", "Pais", "12345");
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1_LOLA")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .direccion(direccion.toString())
                    .build();
    private final Cliente cliente2 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST2_LILA")
                    .correo("test2@gmail.com")
                    .dni("03480731B")
                    .telefono("602697971")
                    .imagen("test2.jpg")
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .isActive(true)
                    .direccion(direccion.toString())
                    .categoria(categoriaCliente)
                    .build();
    private final ClienteResponseDto clienteResponse1 = ClienteResponseDto.builder()
            .id(1L)
            .nombreCompleto("TEST1_LOLA")
            .dni("03480731A")
            .telefono("602697979")
            .imagen("test1.png")
            .fechaCreacion(LocalDateTime.now())
            .fechaActualizacion(LocalDateTime.now())
            .categoria("CLIENTE_TEST")
            .isActive(true)
            .direccion(direccion)
            .build();
    private final ClienteResponseDto clienteResponse2 = ClienteResponseDto.builder()
            .id(1L)
            .nombreCompleto("TEST2_LILA")
            .dni("03480731B")
            .telefono("602697971")
            .imagen("test2.jpg")
            .fechaCreacion(LocalDateTime.now())
            .fechaActualizacion(LocalDateTime.now())
            .categoria("CLIENTE_TEST")
            .isActive(true)
            .direccion(direccion)
            .build();

    @Autowired
    public ClienteRestControllerMvcTest(ClienteMapper clienteMapper, ClienteService clienteService){
        this.clienteMapper = clienteMapper;
        this.clienteService = clienteService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllCliente() throws Exception {
        List <Cliente> listaCliente = List.of(cliente1, cliente2);
        List <ClienteResponseDto> expectedClients = List.of(clienteResponse1, clienteResponse2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(listaCliente);
        Page<ClienteResponseDto> responseDto = new PageImpl<>(expectedClients);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(),  pageable)).thenReturn(page);
        when(clienteMapper.toPageClienteResponse(page)).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ClienteResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
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
    void getAllCliente_ByNombreCompleto() throws Exception {
        Optional<String> nombreCompleto = Optional.of("TEST1_LOLA");
        String localEndPoint = myEndpoint + "?nombreCompleto=TEST1_LOLA";
        List<Cliente> listaCliente = List.of(cliente1);
        List<ClienteResponseDto> expectedClients = List.of(clienteResponse1);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(listaCliente);
        Page<ClienteResponseDto> pageResponse = new PageImpl<>(expectedClients);

        // Arrange
        when(clienteService.findAll(nombreCompleto, Optional.empty(), pageable)).thenReturn(page);
        when(clienteMapper.toPageClienteResponse(page)).thenReturn(pageResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ClienteResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
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
        List<Cliente> listaCliente = List.of(cliente1, cliente2);
        List<ClienteResponseDto> expectedClients = List.of(clienteResponse1, clienteResponse2);
        String localEndPoint = myEndpoint + "?categoria=CLIENTE_TEST";
        Optional<String> categoria = Optional.of("CLIENTE_TEST");
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(listaCliente);
        Page<ClienteResponseDto> pageResponse = new PageImpl<>(expectedClients);

        // Arrange
        when(clienteService.findAll(Optional.empty(), categoria,  pageable)).thenReturn(page);
        when(clienteMapper.toPageClienteResponse(page)).thenReturn(pageResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ClienteResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), categoria, pageable);
        verify(clienteMapper, times(1)).toPageClienteResponse(page);
    }


    @Test
    void getAllCliente_ByIsActivo() throws Exception {
        Optional<Boolean> isActivo = Optional.of(true);
        String localEndPoint = myEndpoint + "?isActivo=true";
        List<Cliente> listaCliente = List.of(cliente1, cliente2);
        List<ClienteResponseDto> expectedClient = List.of(clienteResponse1, clienteResponse2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(listaCliente);
        Page<ClienteResponseDto> responseDtos = new PageImpl<>(expectedClient);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(),  pageable)).thenReturn(page);
        when(clienteMapper.toPageClienteResponse(page)).thenReturn(responseDtos);

        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<ClienteResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });


        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), Optional.empty(),  pageable);
        verify(clienteMapper, times(1)).toPageClienteResponse(page);
    }


    @Test
    void getClienteById() throws Exception {
        // Arrange
        String localEndPoint = myEndpoint + "/1";

        when(clienteService.findById(1L)).thenReturn(cliente1);
        when(clienteMapper.toClienteResponseDto(cliente1)).thenReturn(clienteResponse1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ClienteResponseDto res = mapper.readValue(response.getContentAsString(), ClienteResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(clienteResponse1, res)
        );
    }

    @Test
    void getClienteById_idNotExists() throws Exception {
        // Arrange
        String localEndPoint = myEndpoint + "/1";

        when(clienteService.findById(1L)).thenThrow(new ClienteNotFoundException(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndPoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // verify
        verify(clienteService, times(1)).findById(1L);
    }

    @Test
    void createCliente() throws Exception{
        // Arrange
        ClienteCreateDto clienteDto = new ClienteCreateDto("TEST3_MARIA","test3@gmail.com","03480731A", "602697979" ,  "test3.jpg",direccion, categoriaCliente.getNameCategory(),true);

        when(clienteService.save(clienteDto)).thenReturn(cliente1);
        when(clienteMapper.toClienteResponseDto(cliente1)).thenReturn(clienteResponse1);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ClienteResponseDto res = mapper.readValue(response.getContentAsString(), ClienteResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(clienteResponse1, res)
        );

        // Verify
        verify(clienteService, times(1)).save(clienteDto);
        verify(clienteMapper, times(1)).toClienteResponseDto(cliente1);
    }

   @Test
    void createCliente_BadRequest_NombreCompleto() throws Exception {
        // Arrange
        ClienteCreateDto clienteDto = new ClienteCreateDto(null,"test3@gmail.com","03480731A", "602697979" ,  "test3.jpg",direccion, categoriaCliente.getNameCategory(),true);


        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteCreateDto.write(clienteDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre no puede estar vacio"))
        );
    }

  @Test
   void createCliente_BadRequest_Categoria() throws Exception {
       // Arrange
       ClienteCreateDto clienteCreateDto = new ClienteCreateDto("EvelynObando","test3@gmail.com","03480731A", "602697979" ,  "test3.jpg", direccion, null, true);

       MockHttpServletResponse response = mockMvc.perform(
                       post(myEndpoint)
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(jsonClienteCreateDto.write(clienteCreateDto).getJson())
                               .accept(MediaType.APPLICATION_JSON))
               .andReturn().getResponse();

       // Assert
       assertAll(
               () -> assertEquals(400, response.getStatus()),
               () -> assertTrue(response.getContentAsString().contains("La categoria no puede estar vacio"))
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
      when(clienteMapper.toClienteResponseDto(cliente1)).thenReturn(clienteResponse1);

      MockHttpServletResponse response = mockMvc.perform(
                      put(myLocalEndpoint)
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(jsonClienteUpdateDto.write(clienteUpdateDto).getJson())
                              .accept(MediaType.APPLICATION_JSON))
              .andReturn().getResponse();

      ClienteResponseDto result = mapper.readValue(response.getContentAsString(), ClienteResponseDto.class);

      // Assert
      assertAll(
              () -> assertEquals(200, response.getStatus()),
              () -> assertEquals(clienteResponse1, result)
      );

      // Verify
      verify(clienteService, times(1)).update(id, clienteUpdateDto);
      verify(clienteMapper, times(1)).toClienteResponseDto(cliente1);
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
    void updatePartialCliente() throws Exception {
        // Arrange
        Long id = cliente1.getId();
        String myLocalEndpoint = myEndpoint + "/" + id;
        ClienteUpdateDto clienteUpdateDto = ClienteUpdateDto.builder()
                .nombreCompleto("EvelynObando")
                .correo("evelynobando@gmail.com")
                .telefono("722663186")
                .build();

        when(clienteService.update(id, clienteUpdateDto)).thenReturn(cliente1);
        when(clienteMapper.toClienteResponseDto(cliente1)).thenReturn(clienteResponse1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonClienteUpdateDto.write(clienteUpdateDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ClienteResponseDto result = mapper.readValue(response.getContentAsString(), ClienteResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(clienteResponse1, result)
        );

        // Verify
        verify(clienteService, times(1)).update(id, clienteUpdateDto);
        verify(clienteMapper, times(1)).toClienteResponseDto(cliente1);
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
        var myLocalEndpoint = myEndpoint + "/image/" + id;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        when(clienteService.updateImg(id, file)).thenReturn(cliente1);
        when(clienteMapper.toClienteResponseDto(cliente1)).thenReturn(clienteResponse1);


        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        ClienteResponseDto result = mapper.readValue(response.getContentAsString(), ClienteResponseDto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(clienteResponse1, result)
        );

        // Verify
        verify(clienteService, times(1)).updateImg(id, file);
        verify(clienteMapper, times(1)).toClienteResponseDto(cliente1);
    }
}