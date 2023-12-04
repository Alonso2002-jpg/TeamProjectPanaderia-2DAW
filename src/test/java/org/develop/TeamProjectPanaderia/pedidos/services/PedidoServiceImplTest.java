package org.develop.TeamProjectPanaderia.pedidos.services;

import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.pedidos.exceptions.PedidoEmpty;
import org.develop.TeamProjectPanaderia.rest.pedidos.exceptions.PedidoNotFound;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.LineaPedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.repositories.PedidoRepository;
import org.develop.TeamProjectPanaderia.rest.pedidos.services.PedidoServiceImpl;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadPrice;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotStock;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {
    @Mock
    PedidoRepository pedidoRepository;
    @Mock
    ProductoRepository productoRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    PedidoServiceImpl pedidoService;
    private final Direccion direccion = new Direccion("Calle", "Numero", "Ciudad", "Provincia", "Pais", "12345");
    Categoria categoriaCliente = new Categoria(1L, "CLIENTE_TEST", LocalDate.now(), LocalDate.now(), true);
    private final User user = User.builder()
            .id(50L)
            .name("test perez")
            .password("password_test")
            .username("username_test")
            .email("test@test.com")
            .roles(Set.of(Role.ADMIN))
            .build();
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

    @Test
    void findAllPedidos(){
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());
        Page<Pedido> expectedPage = new PageImpl<>(pedidos);
        Pageable pageable = PageRequest.of(0,10);

        when(pedidoRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Pedido> actualPage = pedidoService.findAll(pageable);

        assertAll(
                () -> assertEquals(expectedPage, actualPage),
                () -> assertEquals(expectedPage.getContent(), actualPage.getContent()),
                () -> assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements())
        );

        verify(pedidoRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById(){
        ObjectId idPedido = new ObjectId();
        Pedido expectedPedido = new Pedido();

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(expectedPedido));

        Pedido actualPedido = pedidoService.findById(idPedido);

        assertEquals(expectedPedido, actualPedido);

        verify(pedidoRepository).findById(idPedido);
    }

    @Test
    void findById_PedidoNotFound() {
        ObjectId idPedido = new ObjectId();
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFound.class, () -> pedidoService.findById(idPedido));

        verify(pedidoRepository).findById(idPedido);
    }


    @Test
    void findPedidosByIdCliente() {
        Long idCliente = cliente1.getId();
        Pageable pageable = mock(Pageable.class);
        @SuppressWarnings("unchecked")
        Page<Pedido> expectedPage = mock(Page.class);
        when(pedidoRepository.findByIdUsuario(idCliente, pageable)).thenReturn(expectedPage);

        Page<Pedido> resultPage = pedidoService.findByIdUsuario(idCliente, pageable);

        assertEquals(expectedPage, resultPage);

        verify(pedidoRepository).findByIdUsuario(idCliente, pageable);
    }

    @Test
    void save(){
        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(10)
                .precio(19.99)
                .isActivo(true)
                .build();
        Pedido pedido = new Pedido();
        LineaPedido lineaPedido = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(5)
                .precioProducto(19.99)
                .build();

        pedido.setLineasPedido(List.of(lineaPedido));
        Pedido pedidoToSave = new Pedido();
        pedidoToSave.setLineasPedido(List.of(lineaPedido));
        pedido.setIdUsuario(50L);

        when(userRepository.findById(50L)).thenReturn(Optional.of(user));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoToSave);
        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.of(producto));

        Pedido actualPedido = pedidoService.save(pedido);

        assertAll(
                () -> assertEquals(pedidoToSave, actualPedido),
                () -> assertEquals(pedidoToSave.getLineasPedido(), actualPedido.getLineasPedido()),
                () -> assertEquals(pedidoToSave.getLineasPedido().size(), actualPedido.getLineasPedido().size())
        );

        verify(pedidoRepository).save(any(Pedido.class));
        verify(productoRepository, times(2)).findById(any(UUID.class));
    }


    @Test
    void save_PedidoWithNotItems() {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(50L);
        when(userRepository.findById(50L)).thenReturn(Optional.of(user));
        assertThrows(PedidoEmpty.class, () -> pedidoService.save(pedido));

        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(productoRepository, never()).findById(any(UUID.class));
    }

    @Test
    void testDelete() {
        ObjectId idPedido = new ObjectId();
        Pedido pedidoToDelete = new Pedido();
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoToDelete));

        pedidoService.deleteById(idPedido);

        verify(pedidoRepository).findById(idPedido);
        verify(pedidoRepository).deleteById(idPedido);
    }

    @Test
    void delete_PedidoNotFound() {
        ObjectId idPedido = new ObjectId();

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFound.class, () -> pedidoService.deleteById(idPedido));

        verify(pedidoRepository).findById(idPedido);
        verify(pedidoRepository, never()).deleteById(idPedido);
    }

    @Test
    void testUpdate() {
        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(5)
                .precio(10.0)
                .isActivo(true)
                .build();


        LineaPedido lineaPedido = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(2)
                .precioProducto(10.0)
                .build();

        ObjectId idPedido = new ObjectId();
        Pedido pedido = new Pedido();
        pedido.setLineasPedido(List.of(lineaPedido));
        Pedido pedidoToUpdate = new Pedido();
        pedidoToUpdate.setLineasPedido(List.of(lineaPedido));

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoToUpdate));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoToUpdate);
        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.of(producto));

        Pedido actualPedido = pedidoService.update(idPedido, pedido);

        assertAll(
                () -> assertEquals(pedidoToUpdate, actualPedido),
                () -> assertEquals(pedidoToUpdate.getLineasPedido(), actualPedido.getLineasPedido()),
                () -> assertEquals(pedidoToUpdate.getLineasPedido().size(), actualPedido.getLineasPedido().size())
        );

        verify(pedidoRepository).findById(idPedido);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(productoRepository, times(3)).findById(any(UUID.class));
    }

    @Test
    void update_PedidoNotFound() {
        ObjectId idPedido = new ObjectId();
        Pedido pedido = new Pedido();
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFound.class, () -> pedidoService.update(idPedido, pedido));

        verify(pedidoRepository).findById(idPedido);
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(productoRepository, never()).findById(any(UUID.class));
    }

    @Test
    void reserveStockPedidos() throws PedidoNotFound, ProductoNotFound, ProductoBadPrice {
        Pedido pedido = new Pedido();
        List<LineaPedido> lineasPedido = new ArrayList<>();

        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(5)
                .precio(10.0)
                .isActivo(true)
                .build();

        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(2)
                .precioProducto(10.0)
                .build();

        lineasPedido.add(lineaPedido1);

        pedido.setLineasPedido(lineasPedido);

        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));

        Pedido result = pedidoService.reserveStockPedidos(pedido);

        assertAll(
                () -> assertEquals(3, producto.getStock()),
                () -> assertEquals(20.0, lineaPedido1.getTotal()),
                () -> assertEquals(20.0, result.getTotal()),
                () -> assertEquals(2, result.getTotalItems())
        );

        verify(productoRepository, times(1)).findById(producto.getId());
        verify(productoRepository, times(1)).save(producto);
    }


    @Test
    void returnStockPedidos() {
        Pedido pedido = new Pedido();
        List<LineaPedido> lineasPedido = new ArrayList<>();
        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(13)
                .build();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(2)
                .build();

        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        Pedido result = pedidoService.returnStockPedidos(pedido);

        assertEquals(15, producto.getStock());
        assertEquals(pedido, result);

        verify(productoRepository, times(1)).findById(producto.getId());
        verify(productoRepository, times(1)).save(producto);
    }


    @Test
    void checkPedido() {
        Pedido pedido = new Pedido();
        List<LineaPedido> lineasPedido = new ArrayList<>();
        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(5)
                .precio(10.0)
                .isActivo(true)
                .build();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(2)
                .precioProducto(10.0)
                .build();

        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));

        assertDoesNotThrow(() -> pedidoService.checkPedido(pedido));

        verify(productoRepository, times(1)).findById(producto.getId());
    }


    @Test
    void checkPedido_ProductNotFound() {
        Pedido pedido = new Pedido();
        List<LineaPedido> lineasPedido = new ArrayList<>();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idProducto(UUID.randomUUID())
                .cantidad(2)
                .precioProducto(10.0)
                .build();

        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ProductoNotFound.class, () -> pedidoService.checkPedido(pedido));

        // Verify
        verify(productoRepository, times(1)).findById(any(UUID.class));
    }


    @Test
    void checkPedido_ProductoNotStock() {
        // Arrange
        Pedido pedido = new Pedido();
        Producto producto = Producto.builder()
                .id(UUID.randomUUID())
                .stock(5)
                .precio(10.0)
                .isActivo(true)
                .build();

        List<LineaPedido> lineasPedido = new ArrayList<>();
        LineaPedido lineaPedido1 = LineaPedido.builder()
                .idProducto(producto.getId())
                .cantidad(2)
                .precioProducto(10.0)
                .build();
        lineaPedido1.setIdProducto(producto.getId());
        lineaPedido1.setCantidad(10);
        lineasPedido.add(lineaPedido1);
        pedido.setLineasPedido(lineasPedido);

        when(productoRepository.findById(any(UUID.class))).thenReturn(Optional.of(producto));

        assertThrows(ProductoNotStock.class, () -> pedidoService.checkPedido(pedido));

        verify(productoRepository, times(1)).findById(any(UUID.class));
    }

}
