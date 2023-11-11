package org.develop.TeamProjectPanaderia.proveedores.services;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.repositories.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedor1, proveedor;

    @BeforeEach
    void setUp() {
        proveedor1 = Proveedor.builder()
                .id(1L)
                .nif("11")
                .numero("12")
                .nombre("Juan")
                .build();
        proveedor = Proveedor.builder()
                .id(2L)
                .nif("22")
                .numero("21")
                .nombre("Jose")
                .build();
    }

    @Test
    void saveProveedor() {
        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);

        Proveedor result = proveedorService.saveProveedores(proveedor);

        assertNotNull(result);
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void getProveedorById() {
        Long proveedorId = 1L;
        proveedor.setId(proveedorId);
        when(proveedorRepository.findById(proveedorId)).thenReturn(Optional.of(proveedor));

        Optional<Proveedor> result = proveedorService.getProveedoresById(proveedorId);

        assertTrue(result.isPresent());
        assertEquals(proveedorId, result.get().getId());
        verify(proveedorRepository, times(1)).findById(proveedorId);
    }

    @Test
    void deleteProveedorById() {
        Long proveedorId = 1L;

        assertDoesNotThrow(() -> proveedorService.deleteProveedoresById(proveedorId));

        verify(proveedorRepository, times(1)).deleteById(proveedorId);
    }

    @Test
    void getAllProveedores() {
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor, proveedor1));

        List<Proveedor> result = proveedorService.getAllProveedores();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(proveedorRepository, times(1)).findAll();
    }

    @Test
    void findProveedorByNIF() {
        String nif = "11";
        when(proveedorRepository.findByNIF(nif)).thenReturn(proveedor1);

        Proveedor result = proveedorService.findProveedoresByNIF(nif);

        assertNotNull(result);
        verify(proveedorRepository, times(1)).findByNIF(nif);
    }

    @Test
    void findProveedoresByNombre() {
        String nombre = "Juan";
        when(proveedorRepository.findByNombre(nombre)).thenReturn(List.of(proveedor1));

        List<Proveedor> result = proveedorService.findProveedoresByNombre(nombre);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proveedorRepository, times(1)).findByNombre(nombre);
    }

}

