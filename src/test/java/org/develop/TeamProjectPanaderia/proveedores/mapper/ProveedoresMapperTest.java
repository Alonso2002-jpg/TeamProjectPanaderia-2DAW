package org.develop.TeamProjectPanaderia.proveedores.mapper;

import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedoresCreateDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedoresResponseDto;
import org.develop.TeamProjectPanaderia.proveedores.dto.ProveedoresUpdateDto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class ProveedoresMapperTest {

    @InjectMocks
    private ProveedoresMapper proveedoresMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToResponse() {
        Proveedores proveedores = new Proveedores();
        proveedores.setId(1L);
        proveedores.setNIF("123456789");
        proveedores.setNumero("P123");
        proveedores.setNombre("Juan");

        ProveedoresResponseDto responseDto = proveedoresMapper.toResponse(proveedores);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getNIF()).isEqualTo("123456789");
        assertThat(responseDto.getNumero()).isEqualTo("P123");
        assertThat(responseDto.getNombre()).isEqualTo("Juan");
    }
    @Test
    public void testToCreate() {
        Proveedores proveedores = new Proveedores();
        proveedores.setId(1L);
        proveedores.setNIF("123456789");
        proveedores.setNumero("P123");
        proveedores.setNombre("Juan");

        ProveedoresCreateDto createDto = proveedoresMapper.ToCreate(proveedores);

        assertThat(createDto).isNotNull();
        assertThat(createDto.getId()).isEqualTo(1L);
        assertThat(createDto.getNIF()).isEqualTo("123456789");
        assertThat(createDto.getNumero()).isEqualTo("P123");
        assertThat(createDto.getNombre()).isEqualTo("Juan");
    }

    @Test
    public void testToCreateList() {
        Proveedores proveedor1 = new Proveedores();
        proveedor1.setId(1L);
        proveedor1.setNIF("123456789");
        proveedor1.setNumero("P123");
        proveedor1.setNombre("Juan");

        Proveedores proveedor2 = new Proveedores();
        proveedor2.setId(2L);
        proveedor2.setNIF("987654321");
        proveedor2.setNumero("P456");
        proveedor2.setNombre("Maria");

        List<Proveedores> proveedoresList = Arrays.asList(proveedor1, proveedor2);

        List<ProveedoresCreateDto> createDtoList = proveedoresMapper.ToCreate(proveedoresList);

        assertThat(createDtoList).isNotNull().hasSize(2);
        assertThat(createDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(createDtoList.get(1).getId()).isEqualTo(2L);
    }
    @Test
    public void testToUpdate() {
        Proveedores proveedores = new Proveedores();
        proveedores.setId(1L);
        proveedores.setNIF("123456789");
        proveedores.setNumero("P123");
        proveedores.setNombre("Juan");

        ProveedoresUpdateDto updateDto = proveedoresMapper.ToUpdate(proveedores);

        assertThat(updateDto).isNotNull();
        assertThat(updateDto.getId()).isEqualTo(1L);
        assertThat(updateDto.getNIF()).isEqualTo("123456789");
        assertThat(updateDto.getNumero()).isEqualTo("P123");
        assertThat(updateDto.getNombre()).isEqualTo("Juan");
    }
    @Test
    public void testToUpdateList() {
        Proveedores proveedor1 = new Proveedores();
        proveedor1.setId(1L);
        proveedor1.setNIF("123456789");
        proveedor1.setNumero("P123");
        proveedor1.setNombre("Juan");

        Proveedores proveedor2 = new Proveedores();
        proveedor2.setId(2L);
        proveedor2.setNIF("987654321");
        proveedor2.setNumero("P456");
        proveedor2.setNombre("Maria");

        List<Proveedores> proveedoresList = Arrays.asList(proveedor1, proveedor2);

        List<ProveedoresUpdateDto> updateDtoList = proveedoresMapper.ToUpdate(proveedoresList);

        assertThat(updateDtoList).isNotNull().hasSize(2);
        assertThat(updateDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(updateDtoList.get(1).getId()).isEqualTo(2L);
    }
}
