package org.develop.TeamProjectPanaderia.cliente.controllers;



import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.controllers.ClienteRestController;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
public class ClienteControllerTest {
    @Mock
    ClienteService clienteService;
    @Mock
    ClienteMapper clienteMapper;
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
    private final Direccion direccion = new Direccion("Calle", "Numero", "Ciudad", "Provincia", "Pais", "12345");
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
    @InjectMocks
    private ClienteRestController clienteController;

    @Test
    void getAllCliente() {
        List <Cliente> listCliente = List.of(cliente1, cliente2);
        List <ClienteResponseDto> expectedClients = List.of(clienteResponse1, clienteResponse2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(listCliente);
        Page<ClienteResponseDto> responseDto = new PageImpl<>(expectedClients);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(),  pageable)).thenReturn(page);
        when(clienteMapper.toPageClienteResponse(page)).thenReturn(responseDto);

        ResponseEntity<PageResponse<ClienteResponseDto>> responseEntity = clienteController.getAllCliente(Optional.empty(), Optional.empty(), 0, 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), Optional.empty(), pageable);
    }

}
