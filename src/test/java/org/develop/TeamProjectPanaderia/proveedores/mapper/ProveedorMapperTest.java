package org.develop.TeamProjectPanaderia.proveedores.mapper;

import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProveedorMapperTest {

    private ProveedorMapper proveedorMapper;
    private Proveedor proveedor;
    private ProveedorCreateDto createDto;
    private ProveedorUpdateDto updateDto;
    private ProveedorResponseDto responseDto;

    @BeforeEach
    void setup() throws Exception {
        proveedorMapper = new ProveedorMapper();
        proveedor = Proveedor.builder()
                .nif("1L")
                .numero("12")
                .nombre("Juan")
                .build();

        createDto = proveedorMapper.ToCreate(proveedor);
        updateDto = proveedorMapper.ToUpdate(proveedor);
        responseDto = proveedorMapper.toResponse(proveedor);
    }

    @Test
    void toCreate() {
        assertEquals(proveedor.getNif(), createDto.getNif());
        assertEquals(proveedor.getNumero(), createDto.getNumero());
        assertEquals(proveedor.getNombre(), createDto.getNombre());
    }

    @Test
    void toUpdate() {
        assertEquals(proveedor.getNif(), updateDto.getNif());
        assertEquals(proveedor.getNumero(), updateDto.getNumero());
        assertEquals(proveedor.getNombre(), updateDto.getNombre());
    }

    @Test
    void toResponse() {
        assertEquals(proveedor.getNif(), responseDto.getNif());
        assertEquals(proveedor.getNumero(), responseDto.getNumero());
        assertEquals(proveedor.getNombre(), responseDto.getNombre());

    }
}
