package org.develop.TeamProjectPanaderia.categoria.controllers;

import org.develop.TeamProjectPanaderia.rest.categoria.controllers.CategoriaRestController;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotDeleteException;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotSaveException;
import org.develop.TeamProjectPanaderia.rest.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoriaRestControllerTest {

    private Categoria categoria1, categoria2;
    private CategoriaResponseDto categoriaResponseDto1, categoriaResponseDto2;

    @Mock
    private CategoriaMapper categoriaMapper;
    @Mock
    private CategoriaService categoriaService;
    @InjectMocks
    private CategoriaRestController categoriaRestController;

    @BeforeEach
    void setup() {
        categoria1 = Categoria.builder()
                .id(1L)
                .nameCategory("TestCategory")
                .isActive(true)
                .build();
        categoria2 = Categoria.builder()
                .id(2L)
                .nameCategory("TestCategory2")
                .isActive(false)
                .build();

        categoriaResponseDto1 = new CategoriaResponseDto(
                "TestCategory",
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                true
        );
        categoriaResponseDto2 = new CategoriaResponseDto(
                "TestCategory2",
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                false
        );
    }
    @Test
    void findAll() {
        List<Categoria> categoriasList =List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto1,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responseDtos = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.empty(), Optional.empty(),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toPageResponse(responsePage)).thenReturn(responseDtos);

        ResponseEntity<PageResponse<CategoriaResponseDto>> res = categoriaRestController.findAll(Optional.empty(), Optional.empty(),0 , 10, "id", "asc");

        // Assert
        assertAll("findall",
                () -> assertNotNull(res.getBody()),
                () -> assertEquals(200, res.getStatusCode().value()),
                () -> assertEquals(2, res.getBody().content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.empty(),Optional.empty(),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePage);
    }

    @Test
    void findAllByIsActive(){
        List<Categoria> categoriasList =List.of(categoria1);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto1);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responseDtos = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.of(true), Optional.empty(),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toPageResponse(responsePage)).thenReturn(responseDtos);

        ResponseEntity<PageResponse<CategoriaResponseDto>> res = categoriaRestController.findAll(Optional.of(true), Optional.empty(),0 , 10, "id", "asc");

        // Assert
        assertAll("findall",
                () -> assertNotNull(res.getBody()),
                () -> assertEquals(200, res.getStatusCode().value()),
                () -> assertEquals(1, res.getBody().content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.of(true),Optional.empty(),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePage);
    }

    @Test
    void findAllByName(){
        List<Categoria> categoriasList =List.of(categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responseDtos = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.empty(), Optional.of("TestCategory2"),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toPageResponse(responsePage)).thenReturn(responseDtos);

        ResponseEntity<PageResponse<CategoriaResponseDto>> res = categoriaRestController.findAll(Optional.empty(), Optional.of("TestCategory2"),0 , 10, "id", "asc");

        // Assert
        assertAll("findall",
                () -> assertNotNull(res.getBody()),
                () -> assertEquals(200, res.getStatusCode().value()),
                () -> assertEquals(1, res.getBody().content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.empty(),Optional.of("TestCategory2"),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePage);
    }

    @Test
    void findAllByIsActiveAndName(){
        List<Categoria> categoriasList =List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto1,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responseDtos = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.of(true), Optional.of("TestCategory"),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toPageResponse(responsePage)).thenReturn(responseDtos);

        ResponseEntity<PageResponse<CategoriaResponseDto>> res = categoriaRestController.findAll(Optional.of(true), Optional.of("TestCategory"),0 , 10, "id", "asc");

        // Assert
        assertAll("findall",
                () -> assertNotNull(res.getBody()),
                () -> assertEquals(200, res.getStatusCode().value()),
                () -> assertEquals(2, res.getBody().content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.of(true), Optional.of("TestCategory"),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePage);
    }
    @Test
    void findById() {
        when(categoriaService.findById(1L)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto1);

        ResponseEntity<CategoriaResponseDto> res = categoriaRestController.findById(1L);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), res.getStatusCode().value()),
                () -> assertEquals(categoria1.getNameCategory(), res.getBody().nameCategory())
        );

        verify(categoriaService,times(1)).findById(1L);
    }

    @Test
    void findByIdError(){

        when(categoriaService.findById(100L)).thenThrow(new CategoriaNotFoundException("id " + 100L));

        var res = assertThrows(CategoriaNotFoundException.class, () -> categoriaRestController.findById(100L));

        assertEquals("Categoria not found with id 100", res.getMessage());

        verify(categoriaService,times(1)).findById(100L);
    }

    @Test
    void postCategoria() {
        CategoriaCreateDto categoria = new CategoriaCreateDto("Panaderia",true);

        when(categoriaService.save(categoria)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto1);


        ResponseEntity<CategoriaResponseDto> res = categoriaRestController.postCategoria(categoria);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), res.getStatusCode().value()),
                () -> assertEquals(categoria1.getNameCategory(), res.getBody().nameCategory())
        );

        verify(categoriaService,times(1)).save(categoria);
    }

    @Test
    void postCategoriaError(){
        CategoriaCreateDto categoria = new CategoriaCreateDto("Panaderia",true);

        when(categoriaService.save(categoria)).thenThrow(new CategoriaNotSaveException("Category with name Panaderia already exists"));

        var res = assertThrows(CategoriaNotSaveException.class, () -> categoriaRestController.postCategoria(categoria));

        assertEquals("Category with name Panaderia already exists", res.getMessage());

        verify(categoriaService,times(1)).save(categoria);
    }

    @Test
    void putCategoria() {
        CategoriaUpdateDto categoria = new CategoriaUpdateDto(categoria1.getNameCategory(),true);
        when(categoriaService.update(1L,categoria)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto1);

        ResponseEntity<CategoriaResponseDto> res = categoriaRestController.putCategoria(1L,categoria);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), res.getStatusCode().value()),
                () -> assertEquals(categoria1.getNameCategory(), res.getBody().nameCategory())
        );

        verify(categoriaService,times(1)).update(1L,categoria);
    }

    @Test
    void putCategoriaError(){
        CategoriaUpdateDto categoria = new CategoriaUpdateDto(categoria1.getNameCategory(),true);
        when(categoriaService.update(100L,categoria)).thenThrow(new CategoriaNotFoundException("id " + 100L));

        var res = assertThrows(CategoriaNotFoundException.class, () -> categoriaRestController.putCategoria(100L,categoria));

        assertEquals("Categoria not found with id 100", res.getMessage());

        verify(categoriaService,times(1)).update(100L,categoria);
    }

    @Test
    void deleteById() {
        doNothing().when(categoriaService).deleteById(1L);

        var res = categoriaRestController.deleteById(1L);

        assertEquals(204,res.getStatusCode().value());

        verify(categoriaService,times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdErrorNotFound(){
        doThrow(new CategoriaNotFoundException("id " + 100L)).when(categoriaService).deleteById(100L);

        var res = assertThrows(CategoriaNotFoundException.class, () -> categoriaRestController.deleteById(100L));

        assertEquals("Categoria not found with id 100", res.getMessage());

        verify(categoriaService,times(1)).deleteById(100L);
    }

    @Test
    void deleteByIdErrorCategoryExists(){
        doThrow(new CategoriaNotDeleteException("Category cant be deleted because has something")).when(categoriaService).deleteById(100L);

        var res = assertThrows(CategoriaNotDeleteException.class, () -> categoriaRestController.deleteById(100L));

        assertEquals("Category cant be deleted because has something", res.getMessage());

        verify(categoriaService,times(1)).deleteById(100L);
    }
}