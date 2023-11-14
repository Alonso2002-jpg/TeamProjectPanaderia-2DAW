package org.develop.TeamProjectPanaderia.cliente.repositories;

import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = "spring.sql.init.mode = never")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class ClienteRepositoryTest {
    private final Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Cliente cliente1 = Cliente.builder()
            .id(null)
            .nombreCompleto("Joselyn Obando")
            .correo("prueba@test.com")
            .dni("12345678A")
            .telefono("612345678")
            .imagen("prueba.jpg")
            .fechaActualizacion(LocalDateTime.now())
            .fechaActualizacion(LocalDateTime.now())
            .categoria(categoriaCliente)
            .build();
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        // Categoria
        entityManager.merge(categoriaCliente);
        entityManager.flush();
        // Cliente
        entityManager.merge(cliente1);
        entityManager.flush();
    }

    @Test
    void findAll(){
        // Act
        List<Cliente> clientList = clienteRepository.findAll();

        // Assert
        assertAll(
                () -> assertNotNull(clientList),
                () -> assertFalse(clientList.isEmpty()),
                () -> assertEquals(cliente1, clientList.get(0))
        );
    }

    @Test
    void findById_ExistId(){
        // Act
        Long id = 1L;
        Optional<Cliente> foundClient = clienteRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(foundClient),
                () -> assertTrue(foundClient.isPresent()),
                () -> assertEquals(id, foundClient.get().getId())
        );
    }

    @Test
    void findById_NotExistId(){
        // Act
        Long id = 99L;
        Optional<Cliente> foundClient = clienteRepository.findById(id);

        // Assert
        assertAll(
                () -> assertNotNull(foundClient),
                () -> assertTrue(foundClient.isEmpty())
        );
    }


    @Test
    void save(){
        Cliente newClient =  Cliente.builder()
                .id(null)
                .nombreCompleto("Cliente Nuevo")
                .correo("nuevocliente@gmail.com")
                .dni("21234567A")
                .telefono("623456789")
                .imagen("nuevocliente.jpg")
                .fechaActualizacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .categoria(categoriaCliente)
                .build();

        // Act
        Cliente savedClient = clienteRepository.save(newClient);
        List<Cliente> clientList = clienteRepository.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(savedClient),
                () -> assertTrue(clienteRepository.existsById(savedClient.getId())),
                () -> assertTrue(clientList.size() >= 2)
        );
    }

    @Test
    void save_alreadyExist(){
        Cliente newClient =  Cliente.builder()
                .id(1L)
                .nombreCompleto("Cliente Nuevo")
                .correo("nuevocliente@gmail.com")
                .dni("21234567A")
                .telefono("623456789")
                .imagen("nuevocliente.jpg")
                .fechaActualizacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .categoria(categoriaCliente)
                .build();

        // Act
        Cliente savedClient = clienteRepository.save(newClient);
        List<Cliente> clientList = clienteRepository.findAll();

        // Assert
        assertAll("save",
                () -> assertEquals(newClient, savedClient),
                () -> assertNotNull(savedClient),
                () -> assertTrue(clienteRepository.existsById(savedClient.getId())),
                () -> assertTrue(clientList.size() >= 2)
        );
    }

    @Test
    void deleteById(){
        // Act
        Long id = 1L;
        clienteRepository.deleteById(1L);
        List<Cliente> clientList = clienteRepository.findAll();

        assertAll("delete",
                () -> assertFalse(clienteRepository.existsById(id)),
                () -> assertFalse(clientList.isEmpty())
        );
    }

}


