package org.develop.TeamProjectPanaderia.cliente.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteServiceImpl;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest{
    private final Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);

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
                    .build();
    private final Cliente cliente2 =
            Cliente.builder()
                    .id(2L)
                    .nombreCompleto("TEST2_LILA")
                    .correo("test2@gmail.com")
                    .dni("03480731B")
                    .telefono("602697971")
                    .imagen("test2.jpg")
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .build();

    WebSocketHandler webSocketHandlerMock = mock (WebSocketHandler.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriaService categoriaService;
    @Mock
    private ClienteMapper clienteMapper;
    @InjectMocks
    private ClienteServiceImpl clienteService;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private NotificacionMapper<Cliente> clienteNotificationMapper;

    @BeforeEach
    void setUp(){
        clienteService.setWebSocketService(webSocketHandlerMock);
    }


    @Test
    void findAll_NotParameters(){
        // Arrange
        List<Cliente> expectedCliente = List.of(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expectedCliente);

        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Cliente> actualPage = clienteService.findAll(Optional.empty(), Optional.empty(),  pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(clienteRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByNombreCompleto(){
        // Arrange
        Optional <String> nombreCompleto = Optional.of("TEST1_LOLA");
        List<Cliente> expectedCliente = List.of(cliente1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>( expectedCliente);

        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Cliente> actualPage = clienteService.findAll(nombreCompleto, Optional.empty(),  pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(clienteRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


}
