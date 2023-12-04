package org.develop.TeamProjectPanaderia.pedidos.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.LineaPedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.services.PedidoService;
import org.develop.TeamProjectPanaderia.utils.pageresponse.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class PedidoRestControllerTest {
    private final String myEndpoint = "/v1/pedidos";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Direccion direccion = new Direccion("Calle", "Numero", "Ciudad", "Provincia", "Pais", "12345");
    Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final Cliente cliente1 =
            Cliente.builder()
                    .id(1L)
                    .nombreCompleto("TEST1_LOLA")
                    .correo("test1@gmail.com")
                    .dni("03480731A")
                    .telefono("602697979")
                    .imagen("test1.jpg")
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .isActive(true)
                    .categoria(categoriaCliente)
                    .direccion(direccion.toString())
                    .build();
    private final Pedido pedido = Pedido.builder()
            .id(new ObjectId("5f9f1a3b9d6b6d2e3c1d6f1a"))
            .idUsuario(1L)
            .idCliente(cliente1.getId())
            .lineasPedido(List.of(LineaPedido.builder()
                    .idProducto(UUID.randomUUID())
                    .cantidad(20)
                    .precioProducto(19.99)
                    .build()))
            .build();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private PedidoService pedidosService;

    @Autowired
    public PedidoRestControllerTest(PedidoService pedidosService) {
        this.pedidosService = pedidosService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAll() throws Exception {
        var pedidosList = List.of(pedido);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(pedidosList);

        when(pedidosService.findAll(pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<Pedido> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );

        verify(pedidosService, times(1)).findAll(any(Pageable.class));
    }


    @Test
    @WithAnonymousUser
    void authentication_false() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    void getPedidoById() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        when(pedidosService.findById(any(ObjectId.class))).thenReturn(pedido);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(pedido, res)
        );

        verify(pedidosService, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void createPedido() throws Exception {
        when(pedidosService.save(any(Pedido.class))).thenReturn(pedido);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(pedido)))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(pedido, res)
        );

        verify(pedidosService, times(1)).save(any(Pedido.class));
    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        when(pedidosService.update(any(ObjectId.class), any(Pedido.class))).thenReturn(pedido);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(pedido)))
                .andReturn().getResponse();

        Pedido res = mapper.readValue(response.getContentAsString(), Pedido.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(pedido, res)
        );

        verify(pedidosService, times(1)).update(any(ObjectId.class), any(Pedido.class));
    }

    @Test
    void deletePedido() throws Exception {
        var myLocalEndpoint = myEndpoint + "/5f9f1a3b9d6b6d2e3c1d6f1a";

        doNothing().when(pedidosService).deleteById(any(ObjectId.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(pedidosService, times(1)).deleteById(any(ObjectId.class));
    }
}
