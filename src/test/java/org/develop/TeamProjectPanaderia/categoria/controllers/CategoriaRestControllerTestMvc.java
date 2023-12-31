package org.develop.TeamProjectPanaderia.categoria.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class CategoriaRestControllerTestMvc {

    private final String initEndPoint = "/v1/categoria";
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
    public CategoriaRestControllerTestMvc(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
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
        Page<CategoriaResponseDto> responseDtos = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.empty(),Optional.empty(),pageable)).thenReturn(responsePage);
        when(categoriaMapper.toPageResponse(responsePage)).thenReturn(responseDtos);

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
        verify(categoriaMapper,times(1)).toPageResponse(responsePage);
    }

    @Test
    void getAllName() throws Exception {
        var localEndPoint= initEndPoint + "?name=Panaderia";
        List<Categoria> categoriasList =List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePageCat = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responsePageDto = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.empty(),Optional.of("Panaderia"),pageable)).thenReturn(responsePageCat);
        when(categoriaMapper.toPageResponse(responsePageCat)).thenReturn(responsePageDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<CategoriaResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.empty(),Optional.of("Panaderia"),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePageCat);
    }
    @Test
    void getAllisActive() throws Exception {
        var localEndPoint= initEndPoint + "?isActive=true";
        List<Categoria> categoriaList = List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePageCat = new PageImpl<>(categoriaList);
        Page<CategoriaResponseDto> responsePage = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.of(true),Optional.empty(),pageable)).thenReturn(responsePageCat);
        when(categoriaMapper.toPageResponse(responsePageCat)).thenReturn(responsePage);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<CategoriaResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.of(true),Optional.empty(),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePageCat);
    }

    @Test
    void getAllIsActiveAndName() throws Exception {
        var localEndPoint = initEndPoint + "?isActive=true&name=Panaderia";
        List<Categoria> categoriasList = List.of(categoria1,categoria2);
        List<CategoriaResponseDto> categoriaResponseList = List.of(categoriaResponseDto,categoriaResponseDto2);
        Pageable pageable = PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Categoria> responsePageCat = new PageImpl<>(categoriasList);
        Page<CategoriaResponseDto> responsePageDto = new PageImpl<>(categoriaResponseList);

        when(categoriaService.findAll(Optional.of(true),Optional.of("Panaderia"),pageable)).thenReturn(responsePageCat);
        when(categoriaMapper.toPageResponse(responsePageCat)).thenReturn(responsePageDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(localEndPoint)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<CategoriaResponseDto> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(categoriaService,times(1)).findAll(Optional.of(true),Optional.of("Panaderia"),pageable);
        verify(categoriaMapper,times(1)).toPageResponse(responsePageCat);
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
    void postCategoriaBadRequest() throws Exception {
        CategoriaCreateDto categoria = new CategoriaCreateDto(null,true);

        MockHttpServletResponse response = mockMvc.perform(
                        post(initEndPoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(categoria))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El nombre de la categoria no puede estar vacio"))
        );
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
    void deleteByIdErrorCategoryExists() throws Exception {
        var localEndPoint = initEndPoint + "/1";
        doThrow(new CategoriaNotDeleteException("Category cant be deleted because it has something")).when(categoriaService).deleteById(1L);

            MockHttpServletResponse response = mockMvc.perform(
            delete(localEndPoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

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