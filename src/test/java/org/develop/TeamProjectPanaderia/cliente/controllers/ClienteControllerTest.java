package org.develop.TeamProjectPanaderia.cliente.controllers;

import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.controllers.ClienteRestController;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {
    Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1_LOLA")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .build();
    private final Cliente cliente2 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST2_LILA")
                    .correo("test2@gmail.com")
                    .dni("03480731B")
                    .telefono("602697971")
                    .imagen("test2.jpg")
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .build();

    @Mock
    private ClienteService clienteService;
    @InjectMocks
    private ClienteRestController clienteController;

    @Test
    void getAllCliente(){

    }

}
