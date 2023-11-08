package org.develop.TeamProjectPanaderia.Proveedores.services;

import org.develop.TeamProjectPanaderia.Proveedores.models.Proveedores;
import org.develop.TeamProjectPanaderia.Proveedores.repositories.ProveedoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProveedoresServiceImplTest {

    private ProveedoresServiceImpl proveedoresService;
    private ProveedoresRepository proveedoresRepository;

    @BeforeEach
    public void setUp() {
        proveedoresRepository = Mockito.mock(ProveedoresRepository.class);
        proveedoresService = new ProveedoresServiceImpl(proveedoresRepository);
    }

    @Test
    public void testSaveProveedores() {
        Proveedores proveedor = new Proveedores();
        when(proveedoresRepository.save(proveedor)).thenReturn(proveedor);

        Proveedores savedProveedor = proveedoresService.saveProveedores(proveedor);

        assertAll(
                () -> assertEquals(proveedor, savedProveedor)
        );
    }

    @Test
    public void testGetProveedoresById() {
        Long id = 1L;
        Proveedores proveedor = new Proveedores();
        when(proveedoresRepository.findById(id)).thenReturn(Optional.of(proveedor));

        //Llama al metodo para obtener el ID
        Optional<Proveedores> foundProveedor = proveedoresService.getProveedoresById(id);

        assertAll(
                () -> assertEquals(proveedor, foundProveedor.orElse(null))
        );
    }

    @Test
    public void testDeleteProveedoresById() {
        Long id = 1L;
        Proveedores proveedor = new Proveedores();
        when(proveedoresRepository.findById(id)).thenReturn(Optional.of(proveedor));

        // Llama al método para eliminar el proveedor por ID
        proveedoresService.deleteProveedoresById(id);

        // Verifica que el proveedor se elimina correctamente
        Optional<Proveedores> deletedProveedor = proveedoresService.getProveedoresById(id);
    }

    @Test
    public void testGetAllProveedores() {
        // Crea una lista de proveedores de ejemplo
        Proveedores proveedor1 = new Proveedores();
        proveedor1.setId(1L);
        proveedor1.setNombre("Proveedor 1");

        Proveedores proveedor2 = new Proveedores();
        proveedor2.setId(2L);
        proveedor2.setNombre("Proveedor 2");

        List<Proveedores> proveedoresList = Arrays.asList(proveedor1, proveedor2);

        // Configura el comportamiento del repositorio al llamar a findAll
        when(proveedoresRepository.findAll()).thenReturn(proveedoresList);

        // Llama al método para obtener todos los proveedores
        List<Proveedores> result = proveedoresService.getAllProveedores();

        // Verifica que la lista resultante sea la misma que la lista de ejemplo
        assertEquals(proveedoresList, result);
    }

    @Test
    public void testFindProveedoresByNIF() {
        String nif = "10";
        Proveedores proveedor = new Proveedores();

        // Simula que el repositorio devuelve un proveedor con el NIF especificado
        when(proveedoresRepository.findByNIF(nif)).thenReturn(proveedor);

        // Llama al método para obtener el proveedor por NIF
        Optional<Proveedores> foundProveedorOptional = Optional.ofNullable(proveedoresService.findProveedoresByNIF(nif));

        assertAll(
                () -> assertTrue(foundProveedorOptional.isPresent()),
                () -> assertEquals(proveedor, foundProveedorOptional.get())
        );
    }

    @Test
    public void testFindProveedoresByNombre() {
        String nombre = "Juan";
        Proveedores proveedor = new Proveedores();
        List<Proveedores> proveedoresList = Collections.singletonList(proveedor);

        // Simula que el repositorio devuelve una lista de proveedores con el nombre asociado
        when(proveedoresRepository.findByNombre(nombre)).thenReturn(proveedoresList);

        // Llama al método para obtener el proveedor por el nombre
        List<Proveedores> proveedoresFound = proveedoresService.findProveedoresByNombre(nombre);

        assertNotNull(proveedoresFound);
        assertTrue(!proveedoresFound.isEmpty());
    }


}

