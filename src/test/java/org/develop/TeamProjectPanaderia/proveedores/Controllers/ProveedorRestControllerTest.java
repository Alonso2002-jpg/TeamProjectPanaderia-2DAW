package org.develop.TeamProjectPanaderia.proveedores.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.rest.proveedores.services.ProveedorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
public class ProveedorRestControllerTest {

    private final String initEndPoint = "/v1/proveedores";  // Ajustado el endpoint para seguir el estándar RESTful
    private Proveedor proveedor1, proveedor2;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProveedorServiceImpl proveedorService;
    @MockBean
    private ProveedorMapper proveedorMapper;
    @Autowired
    private JacksonTester<ProveedorCreateDto> jsonCreateDto;
    @Autowired
    private JacksonTester<ProveedorUpdateDto> jsonUpdateDto;

    @BeforeEach
    void setup() {
        proveedor1 = Proveedor.builder()
                .id(1L)
                .nif("11")
                .numero("12")
                .nombre("Juan")
                .build();
        proveedor2 = Proveedor.builder()
                .id(2L)
                .nif("22")
                .numero("21")
                .nombre("Jose")
                .build();
    }

    @Test
    void getAllProveedores() throws Exception {
        // Crear instancias reales de los objetos Optional, Sort y Pageable
        Optional<String> nif = Optional.of("1L");
        Optional<String> name = Optional.of("Jose");
        Optional<Boolean> isActive = Optional.of(true);
        Optional<String> tipo = Optional.of("Panaderia");
        int page = 0;
        int size = 10;
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Simular el comportamiento del servicio
        when(proveedorService.findAll(eq(nif), eq(name), eq(isActive), eq(tipo), eq(pageable)))
                .thenReturn(Page.empty());
    }

    @Test
    void getProveedorById() throws Exception {
        when(proveedorService.getProveedoresById(1L)).thenReturn(proveedor1);
        when(proveedorMapper.toResponse(proveedor1)).thenReturn(new ProveedorResponseDto());

        mockMvc.perform(get("/v1/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void createProveedor() throws Exception {
        ProveedorCreateDto createDto = new ProveedorCreateDto();
        when(proveedorService.saveProveedores(createDto)).thenReturn(proveedor1);
        when(proveedorMapper.toResponse(proveedor1)).thenReturn(new ProveedorResponseDto());

        ResultActions result = mockMvc.perform(post("/v1/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateDto.write(createDto).getJson()));
    }

    @Test
    void updateProveedor() throws Exception {
        ProveedorUpdateDto updateDto = new ProveedorUpdateDto();
        when(proveedorService.updateProveedor(updateDto, 1L)).thenReturn(proveedor1);
        when(proveedorMapper.toResponse(proveedor1)).thenReturn(new ProveedorResponseDto());

        ResultActions result = mockMvc.perform(put("/v1/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdateDto.write(updateDto).getJson()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void deleteProveedor() throws Exception {
        mockMvc.perform(delete("/v1/proveedores/1"))
                .andExpect(status().isNoContent());

        verify(proveedorService, times(1)).deleteProveedoresById(1L);
    }
}

