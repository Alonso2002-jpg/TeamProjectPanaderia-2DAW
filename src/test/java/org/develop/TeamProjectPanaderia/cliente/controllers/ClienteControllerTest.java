package org.develop.TeamProjectPanaderia.cliente.controllers;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.controllers.ClienteRestController;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteResponseDto;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteService;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoResponseDto;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {
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

    @Mock
    private ClienteService clienteService;
    @InjectMocks
    private ClienteRestController clienteController;
    @Mock
    private ClienteMapper clienteMapper;

    @Test
    void getAllCliente() {
        List<Cliente> expecCliente = List.of(cliente1, cliente2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(expecCliente);

        // Arrange
        when(clienteService.findAll(Optional.empty(), Optional.empty(),  pageable)).thenReturn(page);


        ResponseEntity<PageResponse<Cliente>> responseEntity  =  clienteController.getAllCliente(Optional.empty(), Optional.empty(),  0 , 10, "id", "asc");

        // Assert
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().content().size());

        // Verify
        verify(clienteService, times(1)).findAll(Optional.empty(), Optional.empty(), pageable);
    }

}
