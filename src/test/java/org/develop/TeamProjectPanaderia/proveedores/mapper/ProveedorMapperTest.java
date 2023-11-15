package org.develop.TeamProjectPanaderia.proveedores.mapper;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorCreateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorResponseDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.dto.ProveedorUpdateDto;
import org.develop.TeamProjectPanaderia.rest.proveedores.mapper.ProveedorMapper;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.hibernate.mapping.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                .tipo(new Categoria()) // Puedes inicializar la categoría según tus necesidades
                .fechaCreacion(LocalDate.now())
                .fechaUpdate(LocalDate.now())
                .isActive(true)
                .build();

        createDto = proveedorMapper.ToCreate(proveedor);
        updateDto = proveedorMapper.ToUpdate(proveedor);
        responseDto = proveedorMapper.toResponse(proveedor);
    }

    @Test
    void toProveedor() {
        ProveedorCreateDto createDto = ProveedorCreateDto.builder()
                .nif("12345678A")
                .tipo("TipoCategoria")
                .numero("123")
                .nombre("Proveedor1")
                .build();

        Categoria categoria = new Categoria(); // Puedes inicializar la categoría según tus necesidades

        Proveedor proveedor = proveedorMapper.toProveedor(createDto, categoria);

        assertEquals(createDto.getNif(), proveedor.getNif());
        assertEquals(categoria, proveedor.getTipo());
        assertEquals(createDto.getNumero(), proveedor.getNumero());
        assertEquals(createDto.getNombre(), proveedor.getNombre());
    }

    @Test
    void toCreate() {
        ProveedorCreateDto createDto = proveedorMapper.ToCreate(proveedor);

        assertEquals(proveedor.getNif(), createDto.getNif());
        assertEquals(proveedor.getTipo().toString(), createDto.getTipo());
        assertEquals(proveedor.getNumero(), createDto.getNumero());
        assertEquals(proveedor.getNombre(), createDto.getNombre());
    }

    @Test
    void toUpdate() {
        ProveedorUpdateDto updateDto = proveedorMapper.ToUpdate(proveedor);

        assertEquals(proveedor.getNif(), updateDto.getNif());
        assertEquals(proveedor.getTipo().toString(), updateDto.getTipo());
        assertEquals(proveedor.getNumero(), updateDto.getNumero());
        assertEquals(proveedor.getNombre(), updateDto.getNombre());
    }

    @Test
    void toResponse() {
        ProveedorResponseDto responseDto = proveedorMapper.toResponse(proveedor);

        assertEquals(proveedor.getNif(), responseDto.getNif());
        assertEquals(proveedor.getTipo(), responseDto.getTipo());
        assertEquals(proveedor.getNumero(), responseDto.getNumero());
        assertEquals(proveedor.getNombre(), responseDto.getNombre());
        assertEquals(proveedor.getIsActive(), responseDto.getIsActive());
        assertEquals(proveedor.getFechaCreacion(), responseDto.getFechaCreacion());
        assertEquals(proveedor.getFechaUpdate(), responseDto.getFechaUpdate());
    }


}
