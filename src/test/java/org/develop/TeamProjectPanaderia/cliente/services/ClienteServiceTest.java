package org.develop.TeamProjectPanaderia.cliente.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteBadRequest;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteNotSaveException;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Test
    void findAll_ByIsActive(){
        // Arrange
        Optional <Boolean> isActive = Optional.of(true);
        List<Cliente> expectedCliente = List.of(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expectedCliente);

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
        List<Cliente> expectedCliente = List.of(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Cliente> expectedPage = new PageImpl<>(expectedCliente);

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
        Cliente expectedCliente = cliente1;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(expectedCliente));

        // Act
        Cliente clienteActual = clienteService.findById(id);

        // Assert
        assertEquals(expectedCliente, clienteActual);

        // Verify
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void findById_IdFalse() { //Error
        // Arrange
        Long id = 99L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var res = assertThrows(ClienteNotFoundException.class, () -> clienteService.findById(id));
        assertEquals("Cliente not found with id " + id, res.getMessage());

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
        assertEquals("Cliente con dni " + dniNoExiste + " no encontrado", res.getMessage());

        // Verify
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase(dniNoExiste);
    }

   @Test
   void save() throws IOException {
       // Arrange
       Long id = 1L;
       ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg", direccion, categoriaCliente.getNameCategory(),true);
       Cliente expecCliente = Cliente.builder()
               .id(1L)
               .nombreCompleto("nuevo_cliente")
               .correo("nuevo_cliente@gmail.com")
               .dni("03480731C")
               .telefono("602697985")
               .fechaCreacion(LocalDateTime.now())
               .fechaActualizacion(LocalDateTime.now())
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
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg", direccion, categoriaCliente.getNameCategory(),true);

        when(clienteRepository.findClienteByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(categoriaService.findByName(clienteCreateDto.getCategoria())).thenThrow(new CategoriaNotFoundException(clienteCreateDto.getCategoria()));

        // Act
        var res = assertThrows(ClienteBadRequest.class, () -> clienteService.save(clienteCreateDto));
        assertEquals("La categoria con nombre " + clienteCreateDto.getCategoria() + " no existe", res.getMessage());

        // Verift
        verify(categoriaService, times(1)).findByName(clienteCreateDto.getCategoria());
        verify(clienteRepository, times(1)).findClienteByDniEqualsIgnoreCase((any(String.class)));
    }

    @Test
    void save_dniAlreadyExist(){
        // Arrange
        ClienteCreateDto clienteCreateDto = new ClienteCreateDto("nuevo_cliente","nuevo_cliente@gmail.com","03480731C", "602697985" ,"test3.jpg", direccion, categoriaCliente.getNameCategory(),true);

        when(clienteRepository.findClienteByDniEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(cliente1));

        // Act
        var res = assertThrows(ClienteNotSaveException.class, () -> clienteService.save(clienteCreateDto));
        assertEquals("El dni " + clienteCreateDto.getDni() + " ya existe en la BD", res.getMessage());

        // Verift
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
                .isActive(true)
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(cliente1)).thenReturn(cliente1);
        when(clienteMapper.toCliente(clienteUpdateDto, cliente1, categoriaCliente)).thenReturn(cliente1);
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
        assertEquals("Cliente not found with id " + id, res.getMessage());

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
       var res = assertThrows(ClienteBadRequest.class, () -> clienteService.update(id, clienteUpdateDto));
       assertEquals("La categoria con nombre " + clienteUpdateDto.getCategoria() + " no existe", res.getMessage());

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
        Long id = 99L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var res = assertThrows(ClienteNotFoundException.class, () -> clienteService.deleteById(id));
        assertEquals("Cliente not found with id " + id, res.getMessage());

        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(0)).deleteById(id);
    }


    @Test
    void updateImage() throws IOException {
        // Arrange
        String imageUrl = "test1.jpg";

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
