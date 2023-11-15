package org.develop.TeamProjectPanaderia.cliente.services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.exceptions.ClienteNotFoundException;
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
import java.time.LocalDateTime;
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
    void findAll_ByIsActive(){
        // Arrange
        Optional <Boolean> isActive = Optional.of(true);
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
    void findById_IdTrue() {
        // Arrange
        Long id = cliente1.getId();
        Cliente expecCliente = cliente1;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(expecCliente));

        // Act
        Cliente clienteActual = clienteService.findById(id);

        // Assert
        assertEquals(expecCliente, clienteActual);

        // Verify
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void findById_IdFalse() {
        // Arrange
        Long id = 99L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ClienteNotFoundException.class, () -> clienteService.findById(id));
        assertEquals("Cliente con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(clienteRepository, times(1)).findById(id);
    }


    @Test
    void findByDni(){
        // Arrange
        String dni = cliente1.getDni();
        Cliente expecCliente = cliente1;
        when(clienteRepository.findClienteByDniEqualsIgnoreCase(dni)).thenReturn(Optional.of(expecCliente));

        // Act
        Cliente clienteActual = clienteService.findByDni(dni);

        // Assert
        assertEquals(expecCliente, clienteActual);

        // Verify
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase(dni);
    }

    @Test
    void findByDniNotExist(){
        // Arrange
        String dniNoExiste = "dni_falso";

        when(clienteRepository.findClienteByDniEqualsIgnoreCase(dniNoExiste)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ClienteNotFoundException.class, () -> clienteService.findByDni(dniNoExiste));
        assertEquals("Cliente con DNI " + dniNoExiste + " no encontrado", res.getMessage());

        // Verify
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase(dniNoExiste);
    }

    @Test
    void save() throws IOException {
        // Arrange
        Long id = 1L;
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg",categoriaCliente.getNameCategory(),true);
        Cliente expecCliente = Cliente.builder()
                .id(1L)
                .nombreCompleto("nuevo_cliente")
                .correo("nuevo_cliente@gmail.com")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .dni("03480731C")
                .telefono("602697985")
                .imagen("test3.jpg")
                .categoria(categoriaCliente)
                .isActive(true)
                .build();

        when(clienteRepository.findClienteByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(categoriaService.findByName(clienteCreateDto.getCategoria())).thenReturn(categoriaCliente);
        when(clienteMapper.toCliente(clienteCreateDto, categoriaCliente)).thenReturn(expecCliente);
        when(clienteRepository.save(expecCliente)).thenReturn(expecCliente);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        Cliente clienteActual = clienteService.save(clienteCreateDto);

        // Assert
        assertEquals(expecCliente, clienteActual);

        // Verify
        verify(categoriaService, times(1)).findByName(clienteCreateDto.getCategoria());
        verify(clienteRepository, times(1)).save(expecCliente);
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase((any(String.class)));
        verify(clienteMapper, times(1)).toCliente(clienteCreateDto, categoriaCliente);
    }

    @Test
    void save_categoryNotExist(){
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg", categoriaCliente.getNameCategory(),true);

        when(clienteRepository.findClienteByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(categoriaService.findByName(clienteCreateDto.getCategoria())).thenThrow(new CategoriaNotFoundException(clienteCreateDto.getCategoria()));

        // Act
        var res = assertThrows(CategoriaNotFoundException.class, () -> clienteService.save(clienteCreateDto));
        assertEquals("Categoria not found with " + clienteCreateDto.getCategoria(), res.getMessage());

        // Verift
        verify(categoriaService, times(1)).findByName(clienteCreateDto.getCategoria());
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase((any(String.class)));
    }

    @Test
    void update() throws IOException {
        // Arrange
        Long id = cliente1.getId();

        ClienteUpdateDto clienteUpdateDto =  ClienteUpdateDto.builder()
                .nombreCompleto("EvelynObando")
                .correo("evelynobando@gmail.com")
                .telefono("722663186")
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(cliente1)).thenReturn(cliente1);
        when(clienteMapper.toCliente(clienteUpdateDto, cliente1, categoriaCliente)).thenReturn(cliente1);
        when(categoriaService.findByName(clienteUpdateDto.getCategoria())).thenReturn(categoriaCliente);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        Cliente  clienteActualizado = clienteService.update(id,clienteUpdateDto);

        // Assert
        assertAll(
                () -> assertNotNull(clienteActualizado),
                () -> assertEquals(cliente1, clienteActualizado)
        );

        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(1)).save(cliente1);
        verify(categoriaService, times(1)).findByName(clienteUpdateDto.getCategoria());
        verify(clienteMapper, times(1)).toCliente(clienteUpdateDto, cliente1, categoriaCliente);
    }

    @Test
    void update_IdNotExist() {
        // Arrange
        Long id = cliente1.getId();
        ClienteUpdateDto clienteUpdateDto = ClienteUpdateDto.builder()
                .nombreCompleto("EvelynObando")
                .correo("evelynobando@gmail.com")
                .telefono("722663186")
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ClienteNotFoundException.class, () -> clienteService.update(id,clienteUpdateDto));
        assertEquals("Cliente con id " + id + " no encontrado", res.getMessage());

        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void update_CategoryNotExist() {
        // Arrange
        String category = "Categoria_Falsa";
        Long id = cliente1.getId();
        ClienteUpdateDto clienteUpdateDto =  ClienteUpdateDto.builder()
                .nombreCompleto("EvelynObando")
                .correo("evelynobando@gmail.com")
                .telefono("722663186")
                .categoria("Categoria_Falsa")
                .build();
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente1));
        when(categoriaService.findByName(category)).thenThrow(new CategoriaNotFoundException(category));

        // Act & Assert
        var res = assertThrows(CategoriaNotFoundException.class, () -> clienteService.update(id, clienteUpdateDto));
        assertEquals("Categoria not found with " + category, res.getMessage());

        // Verify
        verify(clienteRepository, times(1)).findById(id);
        verify(categoriaService, times(1)).findByName(category);
    }


    @Test
    void deleteById() throws IOException {
        // Arrange
        Long id = cliente2.getId();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente2));
        doNothing().when(webSocketHandlerMock).sendMessage(any());

        // Act
        clienteService.deleteById(id);

        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_idNotExist(){
        // Arrange
        Long id = cliente2.getId();

        when( clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ClienteNotFoundException.class, () ->  clienteService.deleteById(id));
        assertEquals("Cliente con id " + id + " no encontrado", res.getMessage());

        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(0)).deleteById(id);
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
