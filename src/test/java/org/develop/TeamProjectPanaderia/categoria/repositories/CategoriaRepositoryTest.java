package org.develop.TeamProjectPanaderia.categoria.repositories;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@TestPropertySource(properties = "spring.sql.init.mode = never")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Categoria categoria1, categoria2;

    @BeforeEach
    void setUp(){
        categoria1 = Categoria.builder()
                .nameCategory("VIP")
                .isActive(true)
                .build();
        categoria2 = Categoria.builder()
                .nameCategory("Normal")
                .isActive(false)
                .build();

        entityManager.merge(categoria1);
        entityManager.merge(categoria2);
        entityManager.flush();
    }

    @Test
    void findByNameCategoryIgnoreCase(){
        Optional<Categoria> category = categoriaRepository.findByNameCategoryIgnoreCase("VIP");

        assertAll(
                () -> assertNotNull(category),
                () -> assertFalse(category.isEmpty()),
                () -> assertEquals("VIP", category.get().getNameCategory()),
                () -> assertTrue(category.get().isActive())
        );
    }

    @Test
    void getAll(){
        List<Categoria> categoriaList = categoriaRepository.findAll();

        assertAll(
                () -> assertNotNull(categoriaList),
                () -> assertFalse(categoriaList.isEmpty()),
                () -> assertEquals(2, categoriaList.size()),
                () -> assertEquals("VIP", categoriaList.get(0).getNameCategory()),
                () -> assertEquals("Normal", categoriaList.get(1).getNameCategory())
        );
    }

    @Test
    void findById(){
        Optional<Categoria> category = categoriaRepository.findById(1L);

        assertAll(
                () -> assertNotNull(category),
                () -> assertFalse(category.isEmpty()),
                () -> assertEquals("VIP", category.get().getNameCategory())
        );
    }

    @Test
    void save(){
        Categoria category = categoriaRepository.save(Categoria.builder().nameCategory("TEST").isActive(true).build());

        assertAll(
                () -> assertNotNull(category),
                () -> assertNotNull(category.getId()),
                () -> assertEquals("TEST", category.getNameCategory()),
                () -> assertTrue(category.isActive())
        );
    }

    @Test
    void update(){
        categoria1.setNameCategory("Prueba Test");
        Categoria categoria = categoriaRepository.save(categoria1);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(1, categoria.getId()),
                () -> assertNotEquals("VIP", categoria.getNameCategory()),
                () -> assertEquals("Prueba Test", categoria.getNameCategory()),
                () -> assertTrue(categoria.isActive())

        );
    }

    @Test
    void deleteById(){
        categoriaRepository.deleteById(1L);
        var categorias = categoriaRepository.findAll();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertEquals(1, categorias.size()),
                () -> assertEquals("Normal", categorias.get(0).getNameCategory())
        );
    }

    @Test
    void deleteAll(){
        categoriaRepository.deleteAll();
        var categorias = categoriaRepository.findAll();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertTrue(categorias.isEmpty())
        );
    }

    @Test
    void findByIsActive(){
        var categorias = categoriaRepository.findByIsActive(true);

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertFalse(categorias.isEmpty()),
                () -> assertEquals(1, categorias.size()),
                () -> assertEquals(categoria1.getNameCategory(), categorias.get(0).getNameCategory())
        );
    }

    @Test
    void existsProductById(){
        var result = categoriaRepository.existsProductoById(2L);

        assertAll(
                () -> assertFalse(result)
        );
    }

    @Test
    void existsClienteById(){
        var result = categoriaRepository.existsClienteById(2L);

        assertAll(
                () -> assertFalse(result)
        );
    }

    @Test
    void existsProveedorById(){
        var result = categoriaRepository.existsProveedorByID(2L);

        assertAll(
                () -> assertFalse(result)
        );
    }

    @Test
    void existsPersonalById(){
        var result = categoriaRepository.existsPersonalById(2L);

        assertAll(
                () -> assertFalse(result)
        );
    }
}
