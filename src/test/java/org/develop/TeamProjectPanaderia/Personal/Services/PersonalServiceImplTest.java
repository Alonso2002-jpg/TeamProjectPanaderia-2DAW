package org.develop.TeamProjectPanaderia.Personal.Services;

import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.personal.mapper.PersonalMapper;
import org.develop.TeamProjectPanaderia.personal.models.Personal;
import org.develop.TeamProjectPanaderia.personal.repositories.PersonalRepository;
import org.develop.TeamProjectPanaderia.personal.services.PersonalServiceImpl;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PersonalServiceImplTest {
    private final Categoria categoriaPersonal = new Categoria(1L, "PRERSONAL_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Personal personal1 = Personal.builder()
            .id(UUID.randomUUID())
            .nombre("PERSONAL_TEST1")
            .dni("12345678A")
            .fechaAlta(LocalDate.now())
            .fechaBaja(LocalDate.now())
            .seccion(categoriaPersonal)
            .build();
    private final Personal personal = Personal.builder()
            .id(UUID.randomUUID())
            .nombre("PERSONAL_TEST2")
            .dni("12345678B")
            .fechaAlta(LocalDate.now())
            .fechaBaja(LocalDate.now())
            .seccion(categoriaPersonal)
            .build();
    WebSocketHandler webSocketHandlerMock = mock (WebSocketHandler.class);
    @Mock
    private PersonalRepository personalRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriaService categoriaService;

    @Mock
    private PersonalMapper personalMapper;
    @InjectMocks
    private PersonalServiceImpl personalService;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private NotificacionMapper<Personal> personalNotificacionMapper;

    @BeforeEach
    void setup() {
        personalService.setWebSocketService(webSocketHandlerMock);
    }
    @Test
    void findAll_NotParameters() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal);

        when(personalRepository.findAll()).thenReturn(expectedPersonal);

        // Act
        Page<Personal> actualPage = personalService.findAll( Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, expectedPage)
        );

        // Verify
        verify(personalRepository, times(1)).findAll();
    }
    @Test
    void findAll_ByNombre() {
        // Arrange
        List<Personal> expectedPersonal = List.of(personal1, personal);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Personal> expectedPage = new PageImpl<>(expectedPersonal, pageable, expectedPersonal.size());
        String nombre = "PERSONAL_TEST1";

        // Simulamos que el repositorio devuelve 'expectedPage' cuando se llama con los parámetros adecuados
        when(personalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<Personal> actualPage = personalService.findAll(Optional.of(nombre), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll(
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements()),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent())
        );

        // Verify
        verify(personalRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }


}

