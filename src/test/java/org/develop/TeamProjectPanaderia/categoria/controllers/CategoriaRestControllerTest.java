package org.develop.TeamProjectPanaderia.categoria.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotSaveException;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class)
class CategoriaRestControllerTest {

    private final String initEndPoint = "/categoria";
    private Categoria categoria1, categoria2;
    private CategoriaResponseDto categoriaResponseDto, categoriaResponseDto2;
    private final ObjectMapper mapper = new ObjectMapper();

     @Autowired
     MockMvc mockMvc;
     @MockBean
     private CategoriaService categoriaService;
     @MockBean
     private CategoriaMapper categoriaMapper;
     @Autowired
     private JacksonTester<CategoriaCreateDto> jsonCreateDto;
     @Autowired
     private JacksonTester<CategoriaUpdateDto> jsonUpdateDto;

     @Autowired
    public CategoriaRestControllerTest(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;

        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setup(){
         categoria1 = Categoria.builder()
                 .nameCategory("Panaderia")
                 .isActive(true)
                 .build();
         categoria2 = Categoria.builder()
                 .nameCategory("Charcuteria")
                 .isActive(false)
                 .build();

         categoriaResponseDto = new CategoriaResponseDto(
                 "Panaderia",
                 LocalDate.now().toString(),
                 LocalDate.now().toString(),
                 true
         );
         categoriaResponseDto2 = new CategoriaResponseDto(
                 "Charcuteria",
                 LocalDate.now().toString(),
                 LocalDate.now().toString(),
                 false
         );
    }

    @Test
    void getAll() throws Exception {
        List<Categoria> categoriasList =List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);

        when(categoriaService.findAll(Optional.empty(),Optional.empty(),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toResponseList(categoriasList)).thenReturn(categoriaResponseList);

        MockHttpServletResponse response = mockMvc.perform(
                get(initEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<CategoriaResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll("findall",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.empty(),Optional.empty(),pageable);
        verify(categoriaMapper,times(1)).toResponseList(categoriasList);
    }

    @Test
    void getAllisActive() throws Exception {
         var localEndPoint= initEndPoint + "?isActive=true";
         List<Categoria> categoriasList =List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePage = new PageImpl<>(categoriasList);

        when(categoriaService.findAll(Optional.of(true),Optional.empty(),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toResponseList(categoriasList)).thenReturn(categoriaResponseList);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<CategoriaResponseDto> categoriaResponseDtoList = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CategoriaResponseDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertNotNull(categoriaResponseDtoList),
                () -> assertEquals(1,categoriaResponseDtoList.size()),
                () -> assertEquals(categoria1.getNameCategory(),categoriaResponseDtoList.get(0).nameCategory())
        );
    }
    @Test
    void findById() throws Exception {
         var localEndPoint = initEndPoint + "/1";
        when(categoriaService.findById(1L)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        CategoriaResponseDto responseDto = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(CategoriaResponseDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(categoria1.getNameCategory(), responseDto.nameCategory())
        );

        verify(categoriaService,times(1)).findById(1L);
    }

    @Test
    void findByIdError() throws Exception {
        var localEndPoint = initEndPoint + "/100";

        when(categoriaService.findById(100L)).thenThrow(new CategoriaNotFoundException("id " + 100L));

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404,response.getStatus());

        verify(categoriaService,times(1)).findById(100L);
    }

    @Test
    void postCategoria() throws Exception {
         CategoriaCreateDto categoria = new CategoriaCreateDto("Panaderia",true);

        when(categoriaService.save(categoria)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(initEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoria))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponseDto responseDto = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(CategoriaResponseDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(categoria1.getNameCategory(), responseDto.nameCategory())
        );

        verify(categoriaService,times(1)).save(categoria);
    }

    @Test
    void postCategoriaError() throws Exception {
        CategoriaCreateDto categoria = new CategoriaCreateDto("Panaderia",true);
        when(categoriaService.save(categoria)).thenThrow(new CategoriaNotSaveException("Category already exists"));

        MockHttpServletResponse response = mockMvc.perform(
                post(initEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoria))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());

        verify(categoriaService,times(1)).save(categoria);
    }

    @Test
    void putCategoria() throws Exception {
        var localEndPoint = initEndPoint + "/1";
        CategoriaUpdateDto categoria = new CategoriaUpdateDto(categoria1.getNameCategory(),true);
        when(categoriaService.update(1L,categoria)).thenReturn(categoria1);
        when(categoriaMapper.toResponse(categoria1)).thenReturn(categoriaResponseDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateDto.write(categoria).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponseDto responseDto = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructType(CategoriaResponseDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(categoria1.getNameCategory(), responseDto.nameCategory())
        );

        verify(categoriaService,times(1)).update(1L,categoria);
    }

    @Test
    void putCategoriaError() throws Exception {
        var localEndPoint = initEndPoint + "/1";
        CategoriaUpdateDto categoria = new CategoriaUpdateDto(categoria1.getNameCategory(),true);
        when(categoriaService.update(1L,categoria)).thenThrow(new CategoriaNotFoundException("id " + 1L));

        MockHttpServletResponse response = mockMvc.perform(
                put(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateDto.write(categoria).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        verify(categoriaService,times(1)).update(1L,categoria);
    }

    @Test
    void deleteById() throws Exception {
        var localEndPoint = initEndPoint + "/1";
        doNothing().when(categoriaService).deleteById(1L);

        MockHttpServletResponse response = mockMvc.perform(
            delete(localEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204,response.getStatus());

        verify(categoriaService,times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFound() throws Exception {
        var localEndPoint = initEndPoint + "/100";
        doThrow(new CategoriaNotFoundException("id " + 100L)).when(categoriaService).deleteById(100L);

        MockHttpServletResponse response = mockMvc.perform(
                delete(localEndPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(categoriaService, times(1)).deleteById(100L);
    }
}