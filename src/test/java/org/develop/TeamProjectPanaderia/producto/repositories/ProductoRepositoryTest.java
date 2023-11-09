package org.develop.TeamProjectPanaderia.producto.repositories;

import jakarta.persistence.*;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ProductoRepositoryTest {
    private final Categoria categoriaProducto = new Categoria(1L, "PRODUCTO_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Categoria categoriaProveedor = new Categoria(2L, "PROVEEDOR_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Proveedores proveedor = new Proveedores(1L, "Y7821803T", categoriaProveedor, "722663185", "Test S.L.", LocalDate.now(), LocalDate.now());
    private final Producto producto1 =
                Producto.builder()
                        .id(UUID.randomUUID())
                        .nombre("TEST-1")
                        .stock(10)
                        .fechaCreacion(LocalDateTime.now())
                        .fechaActualizacion(LocalDateTime.now())
                        .imagen("test1.png")
                        .precio(49.99)
                        .isActivo(true)
                        .categoria(categoriaProducto)
                        .proveedor(proveedor)
                        .build();

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        // Categoria
        entityManager.merge(categoriaProducto);
        entityManager.merge(categoriaProveedor);
        entityManager.flush();
        // Proveedor
        entityManager.merge(proveedor);
        entityManager.flush();
        // Productos
        entityManager.merge(producto1);
        entityManager.flush();
    }

    @Test
    void findAll(){
        // Act
        List<Producto> productoList = productoRepository.findAll();

        // Assert
        assertAll(
                () -> assertNotNull(productoList),
                () -> assertFalse(productoList.isEmpty()),
                () -> assertEquals(producto1, productoList.get(0))
        );
    }

    @Test
    void findAllByCategoryAndProveedor(){
        // Act
        List<Producto> productoList = productoRepository.findAllByCategoriaContainsIgnoreCaseAndProveedorContainsIgnoreCase("producto_test", "Y7821803T");

        // Assert
        assertAll(
                () -> assertNotNull(productoList),
                () -> assertFalse(productoList.isEmpty()),
                () -> assertEquals(producto1, productoList.get(0))
        );
    }


    @Test
    void findAllByCategory(){
        // Act
        List<Producto> productoList = productoRepository.findAllByCategoriaContainsIgnoreCase("producto_test");

        // Assert
        assertAll(
                () -> assertNotNull(productoList),
                () -> assertFalse(productoList.isEmpty()),
                () -> assertEquals(producto1, productoList.get(0))
        );
    }

    @Test
    void findAllByProveedor(){
        // Act
        List<Producto> productoList = productoRepository.findAllByProveedorContainsIgnoreCase("Y7821803T");

        // Assert
        assertAll(
                () -> assertNotNull(productoList),
                () -> assertFalse(productoList.isEmpty()),
                () -> assertEquals(producto1, productoList.get(0))
        );
    }


    @Test
    void findById_ExistId(){
        // Act
        UUID id = producto1.getId();
        Optional<Producto> producto = productoRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(producto),
                () -> assertTrue(producto.isPresent()),
                () -> assertEquals(producto1.getId(), producto.get().getId())
        );
    }

    @Test
    void findById_NotExistId(){
        // Act
        UUID id = UUID.randomUUID();
        Optional<Producto> producto = productoRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(producto),
                () -> assertTrue(producto.isEmpty())
        );
    }


    @Test
    void save(){
        Producto nuevoProducto =
                Producto.builder()
                        .id(UUID.randomUUID())
                        .nombre("TEST-2")
                        .stock(20)
                        .fechaCreacion(LocalDateTime.now())
                        .fechaActualizacion(LocalDateTime.now())
                        .imagen("test2.png")
                        .precio(59.99)
                        .isActivo(true)
                        .categoria(categoriaProducto)
                        .proveedor(proveedor)
                        .build();

        // Act
        productoRepository.save(nuevoProducto);
        List<Producto> productoList = productoRepository.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(productoList),
                () -> assertEquals(nuevoProducto, productoList.get(1)),
                () -> assertTrue(productoList.size() >= 2)
        );
    }

    @Test
    void save_alreadyExist(){
        Producto nuevoProducto =
                Producto.builder()
                        .id(producto1.getId())
                        .nombre("TEST-2")
                        .stock(20)
                        .fechaCreacion(LocalDateTime.now())
                        .fechaActualizacion(LocalDateTime.now())
                        .imagen("test2.png")
                        .precio(59.99)
                        .isActivo(true)
                        .categoria(categoriaProducto)
                        .proveedor(proveedor)
                        .build();

        // Act
        productoRepository.save(nuevoProducto);
        List<Producto> productoList = productoRepository.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(productoList),
                () -> assertEquals(nuevoProducto, productoList.get(0)),
                () -> assertFalse(productoList.isEmpty()),
                () -> assertEquals(1, productoList.size())
        );
    }

    @Test
    void deleteById(){
        // Act
        productoRepository.deleteById(producto1.getId());
        List<Producto> productoList = productoRepository.findAll();

        assertAll("delete",
                () -> assertTrue(productoList.isEmpty())
        );
    }

}
