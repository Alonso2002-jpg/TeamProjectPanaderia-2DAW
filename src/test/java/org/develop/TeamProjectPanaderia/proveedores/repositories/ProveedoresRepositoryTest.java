package org.develop.TeamProjectPanaderia.proveedores.repositories;

import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProveedoresRepositoryTest {

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveProveedores() {
        // Arrange
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Juan");
        proveedor.setNIF("123456789");

        // Act
        Proveedores savedProveedor = proveedoresRepository.save(proveedor);

        // Assert
        assertAll("savedProveedor",
                () -> assertNotNull(savedProveedor.getId()),
                () -> assertEquals("Juan", savedProveedor.getNombre()),
                () -> assertEquals("123456789", savedProveedor.getNIF())
        );
    }

    @Test
    public void testFindById() {
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Pedro");
        proveedor.setNIF("987654321");

        entityManager.persist(proveedor);

        Optional<Proveedores> foundProveedor = proveedoresRepository.findById(proveedor.getId());

        assertAll("foundProveedor",
                () -> assertTrue(foundProveedor.isPresent()),
                () -> assertEquals(proveedor, foundProveedor.get())
        );
    }

    @Test
    public void testDeleteById() {
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Luis");
        proveedor.setNIF("567890123");

        entityManager.persist(proveedor);

        proveedoresRepository.deleteById(proveedor.getId());
        Proveedores deletedProveedor = entityManager.find(Proveedores.class, proveedor.getId());

        assertNull(deletedProveedor);
    }

    @Test
    public void testFindAll() {
        Proveedores proveedor1 = new Proveedores();
        proveedor1.setNombre("Ana");
        proveedor1.setNIF("111111111");

        Proveedores proveedor2 = new Proveedores();
        proveedor2.setNombre("María");
        proveedor2.setNIF("222222222");

        entityManager.persist(proveedor1);
        entityManager.persist(proveedor2);

        List<Proveedores> proveedores = proveedoresRepository.findAll();

        assertEquals(2, proveedores.size());
    }

    @Test
    public void testFindByNIF() {
        // Arrange
        Proveedores proveedor = new Proveedores();
        proveedor.setNombre("Juan");
        proveedor.setNIF("123456789");

        proveedoresRepository.save(proveedor);

        // Act
        Proveedores foundProveedor = proveedoresRepository.findByNIF("123456789");

        assertAll("foundProveedor",
                () -> assertNotNull(foundProveedor),
                () -> assertEquals("Juan", foundProveedor.getNombre()),
                () -> assertEquals("123456789", foundProveedor.getNIF())
        );
    }

    @Test
    public void testFindByNombre() {
        // Arrange
        Proveedores proveedor1 = new Proveedores();
        proveedor1.setNombre("Juan");
        proveedor1.setNIF("123456789");

        Proveedores proveedor2 = new Proveedores();
        proveedor2.setNombre("Maria");
        proveedor2.setNIF("987654321");

        proveedoresRepository.save(proveedor1);
        proveedoresRepository.save(proveedor2);

        // Act
        List<Proveedores> foundProveedores = proveedoresRepository.findByNombre("Juan");

        assertAll("foundProveedores",
                () -> assertNotNull(foundProveedores),
                () -> assertEquals(1, foundProveedores.size()),
                () -> assertEquals("Juan", foundProveedores.get(0).getNombre()),
                () -> assertEquals("123456789", foundProveedores.get(0).getNIF())
        );
    }
}
