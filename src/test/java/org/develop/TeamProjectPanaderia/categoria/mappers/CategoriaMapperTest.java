package org.develop.TeamProjectPanaderia.categoria.mappers;

import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaMapperTest {
    private CategoriaMapper categoriaMapper;
    private Categoria categoria;
    private CategoriaCreateDto createDto;
    private CategoriaUpdateDto updateDto;
    private CategoriaResponseDto responseDto;

    @BeforeEach
    void setup() throws Exception {
        categoriaMapper = new CategoriaMapper();
        categoria = Categoria.builder()
                .id(1L)
                .nameCategory("Panaderia")
                .isActive(true)
                .build();

        createDto = categoriaMapper.toCreate(categoria);
        updateDto = categoriaMapper.toUpdate(categoria);
        responseDto = categoriaMapper.toResponse(categoria);
    }

    @Test
    void createToCategoria() {
     Categoria categoriaTest = categoriaMapper.toCategoria(createDto);

     assertAll(
             () -> assertNotNull(categoriaTest),
             () -> assertEquals("Panaderia", categoriaTest.getNameCategory()),
             () -> assertTrue(categoriaTest.isActive())
     );
    }

    @Test
    void toCreate() {
        CategoriaCreateDto creadtDtoTest = categoriaMapper.toCreate(categoria);

        assertAll(
                () -> assertNotNull(creadtDtoTest),
                () -> assertEquals("Panaderia", creadtDtoTest.nameCategory()),
                () -> assertTrue(creadtDtoTest.isActive())
        );
    }

    @Test
    void updateToCategoria() {
        Categoria categoriaUpdTest = categoriaMapper.toCategoria(updateDto,categoria);

        assertAll(
                () -> assertNotNull(categoriaUpdTest),
                () -> assertEquals(1, categoriaUpdTest.getId()),
                () -> assertEquals("Panaderia", categoriaUpdTest.getNameCategory()),
                () -> assertTrue(categoriaUpdTest.isActive())
        );
    }

    @Test
    void toUpdate() {
        CategoriaUpdateDto updateDtoTest = categoriaMapper.toUpdate(categoria);

        assertAll(
                () -> assertNotNull(updateDtoTest),
                () -> assertEquals("Panaderia", updateDtoTest.nameCategory()),
                () -> assertTrue(updateDtoTest.isActive())
        );
    }

    @Test
    void responseToCategoria() {
        Categoria categoriaResDto = categoriaMapper.toCategoria(responseDto, categoria.getId());

        assertAll(
                () -> assertNotNull(categoriaResDto),
                () -> assertEquals(1, categoriaResDto.getId()),
                () -> assertEquals("Panaderia", categoriaResDto.getNameCategory()),
                () -> assertTrue(categoriaResDto.isActive())
        );
    }

    @Test
    void toResponse() {
        CategoriaResponseDto responseDtoTest = categoriaMapper.toResponse(categoria);

        assertAll(
                () -> assertNotNull(responseDtoTest),
                () -> assertEquals("Panaderia", responseDtoTest.nameCategory()),
                () -> assertNotNull(responseDtoTest.createdAt()),
                () -> assertNotNull(responseDtoTest.updatedAt()),
                () -> assertTrue(responseDtoTest.isActive())
        );
    }

    @Test
    void toResponseList() {
        List<Categoria> categoryList = List.of(categoria,Categoria.builder().id(2L).nameCategory("Electricidad").isActive(true).build());
        List<CategoriaResponseDto> responseDtoList = categoriaMapper.toResponseList(categoryList);

        assertAll(
                () -> assertNotNull(responseDtoList),
                () -> assertFalse(responseDtoList.isEmpty()),
                () -> assertEquals(2, responseDtoList.size()),
                () -> assertEquals("Panaderia", responseDtoList.get(0).nameCategory()),
                () -> assertEquals("Electricidad", responseDtoList.get(1).nameCategory())
        );
    }

    @Test
    void toPageResponse(){
        Page<CategoriaResponseDto> pageResponse = categoriaMapper.toPageResponse(new PageImpl<>(List.of(categoria)));

        assertAll(
                () -> assertNotNull(pageResponse),
                () -> assertFalse(pageResponse.getContent().isEmpty()),
                () -> assertEquals(1, pageResponse.getContent().size()),
                () -> assertEquals("Panaderia", pageResponse.getContent().get(0).nameCategory())
        );
    }
}