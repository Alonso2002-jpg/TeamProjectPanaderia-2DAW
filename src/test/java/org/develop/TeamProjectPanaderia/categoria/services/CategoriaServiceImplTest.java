package org.develop.TeamProjectPanaderia.categoria.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    private Categoria categoria1, categoria;

    private WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private NotificacionMapper<CategoriaResponseDto> notificacionMapper;
    @Mock
    private CategoriaMapper categoriaMapper;
    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp() {
        categoria1 = Categoria.builder()
                .id(1L)
                .nameCategory("Chucherias")
                .isActive(true)
                .build();
        categoria = Categoria.builder()
                .id(2L)
                .nameCategory("Bebidas")
                .isActive(false)
                .build();
    }

    @Test
    void findAll() {
        List<Categoria> categorias = List.of(categoria, categoria1);

        when(categoriaRepository.findAll()).thenReturn(categorias);
        var result = categoriaService.findAll(null);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Chucherias", result.get(1).getNameCategory()),
                () -> assertEquals("Bebidas", result.get(0).getNameCategory())
        );

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(categoria1));
        var result = categoriaService.findById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Chucherias", result.getNameCategory())

        );

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdError() {
        when(categoriaRepository.findById(100L)).thenReturn(java.util.Optional.empty());
        var result = assertThrows(Exception.class, () -> categoriaService.findById(100L));

        assertEquals("Categoria not found with id 100", result.getMessage());
    }

    @Test
    void findByName() {
        when(categoriaRepository.findByNameCategoryIgnoreCase("Chucherias")).thenReturn(Optional.of(categoria1));
        var result = categoriaService.findByName("Chucherias");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Chucherias", result.getNameCategory())
        );

        verify(categoriaRepository, times(1)).findByNameCategoryIgnoreCase("Chucherias");
    }

    @Test
    void findByNameError() {
        when(categoriaRepository.findByNameCategoryIgnoreCase("Not Exists")).thenReturn(Optional.empty());
        var result = assertThrows(Exception.class, () -> categoriaService.findByName("Not Exists"));

        assertEquals("Categoria not found with name Not Exists", result.getMessage());
    }

    @Test
    void findByIsActive() {
        when(categoriaRepository.findByIsActive(true)).thenReturn(List.of(categoria1));
        var result = categoriaService.findByActiveIs(true);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Chucherias", result.get(0).getNameCategory())
        );

        verify(categoriaRepository, times(1)).findByIsActive(true);
    }

    @Test
    void save() throws IOException {
        CategoriaCreateDto createDto = new CategoriaCreateDto(categoria1.getNameCategory(), categoria1.isActive());
        when(categoriaMapper.toCategoria(createDto)).thenReturn(categoria1);
        when(categoriaRepository.findByNameCategoryIgnoreCase(categoria1.getNameCategory())).thenReturn(java.util.Optional.empty());
        when(categoriaRepository.save(categoria1)).thenReturn(categoria1);
        when(objectMapper.writeValueAsString(anyString())).thenReturn(categoria1.toString());
        doNothing().when(webSocketHandler).sendMessage(any());
        var result = categoriaService.save(createDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("Chucherias", result.getNameCategory()),
                () -> assertTrue(result.isActive())
        );

        verify(categoriaRepository, times(1)).findByNameCategoryIgnoreCase(categoria1.getNameCategory());
        verify(categoriaRepository, times(1)).save(categoria1);
        verify(categoriaMapper, times(1)).toCategoria(createDto);
    }

    @Test
    void saveError() {
        CategoriaCreateDto createDto = new CategoriaCreateDto("Not Exists", categoria1.isActive());

        when(categoriaRepository.findByNameCategoryIgnoreCase("Not Exists")).thenReturn(java.util.Optional.of(categoria1));

        var result = assertThrows(Exception.class, () -> categoriaService.save(createDto));

        assertAll(
                () -> assertEquals("Category already exists", result.getMessage())
        );

        verify(categoriaRepository, times(1)).findByNameCategoryIgnoreCase("Not Exists");
    }

    @Test
    void update() throws IOException {
        CategoriaUpdateDto updDto = new CategoriaUpdateDto(categoria1.getNameCategory(), categoria1.isActive());

        when(categoriaMapper.toCategoria(updDto, categoria1)).thenReturn(categoria1);
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(categoria1));
        when(categoriaRepository.save(categoria1)).thenReturn(categoria1);
        when(objectMapper.writeValueAsString(anyString())).thenReturn(categoria1.toString());
        doNothing().when(webSocketHandler).sendMessage(any());

        var result = categoriaService.update(1L, updDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getId()),
                () -> assertEquals(categoria1.getNameCategory(), result.getNameCategory()),
                () -> assertTrue(result.isActive())
        );

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(categoria1);
        verify(categoriaMapper, times(1)).toCategoria(updDto, categoria1);
    }

    @Test
    void deleteById() throws IOException {
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(categoria1));
        doNothing().when(categoriaRepository).deleteById(1L);
        categoriaService.deleteById(1L);
        when(objectMapper.writeValueAsString(anyString())).thenReturn(categoria1.toString());
        doNothing().when(webSocketHandler).sendMessage(any());

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void deleteAll() {
        doNothing().when(categoriaRepository).deleteAll();
        categoriaService.deleteAll();

        verify(categoriaRepository, times(1)).deleteAll();
    }

    @Test
    void onChange() throws IOException {
        doNothing().when(webSocketHandler).sendMessage(any());
        categoriaService.onChange(Notificacion.Tipo.CREATE, categoria1);
    }
}