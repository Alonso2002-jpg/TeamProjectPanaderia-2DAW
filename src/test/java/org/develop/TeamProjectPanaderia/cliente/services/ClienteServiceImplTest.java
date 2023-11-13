package org.develop.TeamProjectPanaderia.cliente.services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {
    private final Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(),true);
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .categoria(categoriaCliente)
                    .build();
    private final Cliente cliente2 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST2")
                    .correo("test2@gmail.com")
                    .dni("03480731B")
                    .telefono("602697971")
                    .imagen("test2.jpg")
                    .categoria(categoriaCliente)
                    .build();

    WebSocketHandler webSocketHandlerMock = mock (WebSocketHandler.class);
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriaService categoriaService;
    @Mock
    private ClienteMapper clienteMapper;
    @Mock
    private  ClienteServiceImpl clienteService;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private NotificacionMapper<Cliente> clienteNotificacionMapper;

    @BeforeEach
    void setUp(){clienteService.setWebSocketService(webSocketHandlerMock);}

    @Test
    void findAll_NotParameters(){
        // Arrange
        List<Cliente> expecCliente = List.of(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expecCliente);

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
        Optional <String> nombreCompleto = Optional.of("TEST-1");
        List<Cliente> expecCliente = List.of(cliente1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expecCliente);

        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Cliente> paginaActual = clienteService.findAll(nombreCompleto, Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(paginaActual),
                () -> assertFalse(paginaActual.isEmpty()),
                () -> assertEquals(expectedPage, paginaActual)
        );

        // Verify
        verify(clienteRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ByCategoria() {
        Optional<String> categoria = Optional.of("CLIENTE_TEST");
        List<Cliente> expectedProducts = List.of(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expectedProducts);

        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Cliente> actualPage = clienteService.findAll(Optional.empty(), Optional.empty(), pageable);

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
    void updateImage() throws IOException {
        // Arrange
        String imageUrl = "test1.png";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(clienteRepository.findById(cliente1.getId())).thenReturn(Optional.of(cliente1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when((storageService.getUrl(imageUrl))).thenReturn(imageUrl);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);
        doNothing().when(storageService).delete(imageUrl);
        doNothing().when(webSocketHandlerMock).sendMessage(anyString());


        // Act
        Cliente updatedCliente = clienteService.updateImg(cliente1.getId(), multipartFile);

        // Assert
        assertEquals(updatedCliente.getImagen(), imageUrl);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(storageService, times(1)).delete(cliente1.getImagen());
        verify(storageService, times(1)).store(multipartFile);
    }
}
