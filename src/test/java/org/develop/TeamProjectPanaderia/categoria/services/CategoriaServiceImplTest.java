package org.develop.TeamProjectPanaderia.categoria.services;

import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    private Categoria categoria1, categoria;

    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private CategoriaMapper categoriaMapper;
    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp(){
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
    void findAll(){
        List<Categoria> categorias = List.of(categoria,categoria1);

        when(categoriaRepository.findAll()).thenReturn(categorias);
        var result = categoriaService.findAll();

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2,result.size()),
                ()-> assertEquals("Chucherias",result.get(0).getNameCategory()),
                ()-> assertEquals("Bebidas",result.get(1).getNameCategory())
        );

        verify(categoriaRepository,times(1)).findAll();
    }

    @Test
    void findById(){
        when(categoriaRepository.findById(1L)).thenReturn(java.util.Optional.of(categoria));
        var result = categoriaService.findById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1,result.getId()),
                () -> assertEquals("Chucherias",result.getNameCategory())

        );

        verify(categoriaRepository,times(1)).findById(1L);
    }

    @Test
    void findByIdError(){
        when(categoriaRepository.findById(100L)).thenReturn(java.util.Optional.empty());
        var result = assertThrows(Exception.class, () -> categoriaService.findById(100L));

        assertEquals("Categoria not found with id 100", result.getMessage());
    }
}