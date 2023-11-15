package org.develop.TeamProjectPanaderia.producto.repositories;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;

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
    private final Proveedor proveedor = new Proveedor(1L, "12345678D", categoriaProveedor, "722663185", "Test S.L.", true, LocalDate.now(), LocalDate.now());
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
    void findById_ExistId(){
        // Act
        UUID id = producto1.getId();
        Optional<Producto> foundProduct = productoRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(foundProduct),
                () -> assertTrue(foundProduct.isPresent()),
                () -> assertEquals(producto1.getId(), foundProduct.get().getId())
        );
    }

    @Test
    void findById_NotExistId(){
        // Act
        UUID id = UUID.randomUUID();
        Optional<Producto> foundProduct = productoRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(foundProduct),
                () -> assertTrue(foundProduct.isEmpty())
        );
    }


    @Test
    void findByNombre_ExistNombre(){
        // Act
        String nombre = producto1.getNombre();
        Optional<Producto> foundProduct = productoRepository.findByNombreEqualsIgnoreCase(nombre);

        // Assert
        assertAll(
                () -> assertNotNull(foundProduct),
                () -> assertFalse(foundProduct.isEmpty()),
                () -> assertEquals(producto1.getNombre(), foundProduct.get().getNombre())
        );
    }


    @Test
    void findByNombre_NotExistNombre(){
        // Act
        String nombre = "nombreNoRegistrado";
        Optional<Producto> foundProduct = productoRepository.findByNombreEqualsIgnoreCase(nombre);

        // Assert
        assertAll(
                () -> assertNotNull(foundProduct),
                () -> assertTrue(foundProduct.isEmpty())
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

    @Test
    void deleteById_IdNotExist(){
        // Act
        UUID id = UUID.randomUUID();
        productoRepository.deleteById(id);
        List<Producto> productoList = productoRepository.findAll();

        assertAll("delete",
                () -> assertNotNull(productoList),
                () -> assertEquals(1, productoList.size())
        );
    }
}
