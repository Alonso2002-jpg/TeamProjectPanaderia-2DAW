package org.develop.TeamProjectPanaderia.proveedores.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedoresRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProveedoresRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllProveedores() throws Exception {
        mockMvc.perform(get("/proveedores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProveedorById() throws Exception {
        Proveedores proveedor = new Proveedores();
        proveedor.setId(1L);
        proveedor.setNombre("Nombre del Proveedor");
        proveedor.setNIF("123456789");
        proveedoresRepository.save(proveedor);

        Long proveedorId = 1L;

        mockMvc.perform(get("/proveedores/{id}", proveedorId))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateProveedor() throws Exception {
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Juan");
        proveedor.setNIF("123456789");

        ResultActions result = mockMvc.perform(post("/proveedores")
                .content("{\"nombre\":\"Juan\",\"nif\":\"123456789\"}")); // Envía datos JSON directamente como cadena

        result.andExpect(status().isCreated());
    }




    @Test
    public void testUpdateProveedor() throws Exception {
        // Agrega lógica para guardar un proveedor en proveedoresRepository (si es necesario).

        Long proveedorId = 1L;
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Nuevo Nombre");

        ResultActions result = mockMvc.perform(put("/proveedores/{id}", proveedorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor)));

        assertAll(
                () -> result.andExpect(status().isOk()),
                () -> result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        );
    }

    @Test
    public void testDeleteProveedor() throws Exception {
        // Agrega lógica para guardar un proveedor en proveedoresRepository (si es necesario).

        Long proveedorId = 1L;

        mockMvc.perform(delete("/proveedores/{id}", proveedorId))
                .andExpect(status().isNoContent());
    }
}
