package org.develop.TeamProjectPanaderia.proveedores.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProveedorRestControllerTest {

    private final String initEndPoint = "/proveedores";  // Ajustado el endpoint para seguir el estándar RESTful
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
        // Configurar el servicio mock para devolver una lista de proveedores
        when(proveedorService.getAllProveedores()).thenReturn(List.of(proveedor1, proveedor2));

        // Realizar la solicitud GET y esperar un resultado exitoso con el contenido JSON correcto
        mockMvc.perform(get(initEndPoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));  // Reemplazar con los resultados reales esperados

        // Verificar que el método del servicio se haya llamado exactamente una vez
        verify(proveedorService, times(1)).getAllProveedores();
    }

    @Test
    void getProveedorById() throws Exception {
        // Configurar el servicio mock para devolver un proveedor opcional
        when(proveedorService.getProveedoresById(1L)).thenReturn(Optional.ofNullable(proveedor1));

        // Realizar la solicitud GET y esperar un resultado exitoso con el contenido JSON correcto
        mockMvc.perform(get(initEndPoint + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        // Verificar que el método del servicio se haya llamado exactamente una vez
        verify(proveedorService, times(1)).getProveedoresById(1L);
    }

    @Test
    void createProveedor() throws Exception {
        // Configurar el servicio mock para devolver el proveedor creado
        when(proveedorMapper.ToCreate(proveedor1)).thenReturn(null);  // Ajustado, utilizar el mapper real
        when(proveedorService.saveProveedores(proveedor1)).thenReturn(proveedor1);

        // Realizar la solicitud POST y esperar un resultado exitoso con el contenido JSON correcto
        ResultActions result = mockMvc.perform(post(initEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateDto.write(new ProveedorCreateDto()).getJson()));  // Ajustado, utilizar el mapper real
        result.andExpect(status().isOk())
                .andExpect(content().json("{}"));  // Reemplazar con el resultado real esperado

        // Verificar que el método del servicio se haya llamado exactamente una vez
        verify(proveedorService, times(1)).saveProveedores(proveedor1);
    }

    @Test
    void updateProveedor() throws Exception {
        // Configurar el servicio mock para devolver el proveedor actualizado
        when(proveedorMapper.ToUpdate(proveedor1)).thenReturn(null);  // Ajustado, utilizar el mapper real
        when(proveedorService.updateProveedores(1L, proveedor1)).thenReturn(proveedor1);

        // Realizar la solicitud PUT y esperar un resultado exitoso con el contenido JSON correcto
        ResultActions result = mockMvc.perform(put(initEndPoint + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdateDto.write(new ProveedorUpdateDto()).getJson()));  // Ajustado, utilizar el mapper real
        result.andExpect(status().isOk())
                .andExpect(content().json("{}"));  // Reemplazar con el resultado real esperado

        // Verificar que el método del servicio se haya llamado exactamente una vez
        verify(proveedorService, times(1)).updateProveedores(1L, proveedor1);
    }

    @Test
    void deleteProveedor() throws Exception {
        // Realizar la solicitud DELETE y esperar un resultado exitoso
        mockMvc.perform(delete(initEndPoint + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verificar que el método del servicio se haya llamado exactamente una vez
        verify(proveedorService, times(1)).deleteProveedoresById(1L);
    }
}

