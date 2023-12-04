package org.develop.TeamProjectPanaderia.rest.pedidos.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.exceptions.PedidoBadRequest;
import org.develop.TeamProjectPanaderia.rest.pedidos.exceptions.PedidoEmpty;
import org.develop.TeamProjectPanaderia.rest.pedidos.exceptions.PedidoNotFound;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.LineaPedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.pedidos.repositories.PedidoRepository;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadPrice;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotActive;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotStock;
import org.develop.TeamProjectPanaderia.rest.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.rest.users.model.Role;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.develop.TeamProjectPanaderia.rest.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@CacheConfig(cacheNames = "pedidos")
public class PedidoServiceImpl implements PedidoService{
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductoRepository productoRepository, UserRepository userRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        log.info("Buscando todos los pedidos");
        return pedidoRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public Pedido findById(ObjectId id) {
        log.info("Obteniendo Pedido con ID: " + id);
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFound("id "+ id.toHexString()));
    }

    @Override
    public Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable) {
        log.info(("Obteniendo pedidos de usuario con ID: " + idUsuario));
        return pedidoRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @CachePut(key = "#pedido.id")
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido {}", pedido);
        User user = userRepository.findById(pedido.getIdUsuario()).orElseThrow(() -> new PedidoBadRequest("El usuario con ID :" + pedido.getIdUsuario() + " no existe"));
        if (!user.getRoles().stream().anyMatch(rl -> rl.equals(Role.SELLER) || rl.equals(Role.ADMIN))) {
            throw new PedidoBadRequest("El usuario con ID :" + pedido.getIdUsuario() + " no puede realizar ventas");
        }
        log.info("Id del cliente: {}", pedido.getIdCliente());
        checkPedido(pedido);

        var pedSave = reserveStockPedidos(pedido);

        pedSave.setCreatedAt(LocalDateTime.now());
        pedSave.setUpdatedAt(LocalDateTime.now());

        return pedidoRepository.save(pedSave);
    }
    @Override
    @CachePut(key = "#pedido.id")
    public Pedido update(ObjectId id, Pedido pedido) {
        log.info("Actualizando pedido {}", pedido);

        var pedidoupd = pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFound("id "+ id.toHexString()));

        returnStockPedidos(pedido);

        checkPedido(pedido);

        var pedUpd = reserveStockPedidos(pedido);
        pedUpd.setId(pedidoupd.getId());

        pedUpd.setUpdatedAt(LocalDateTime.now());

        return pedidoRepository.save(pedUpd);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(ObjectId id) {
        log.info("Eliminando Pedido con ID: " + id);

        var pedDel = pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFound("id "+ id.toHexString()));

        returnStockPedidos(pedDel);

        pedidoRepository.deleteById(id);
    }

    @Override
    public Boolean findByIdProducto(Long id) {
        return null;
    }

        public Pedido reserveStockPedidos(Pedido pedido){
        log.info("Reservando stock del pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoEmpty("Pedido debe tener minimo una linea de Pedido");
        }

        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productoRepository.findById(lineaPedido.getIdProducto()).orElseThrow(() -> new ProductoNotFound("Producto con ID: " + lineaPedido.getIdProducto() + " no encontrado"));
            producto.setStock(producto.getStock() - lineaPedido.getCantidad());

            productoRepository.save(producto);

            lineaPedido.setTotal(lineaPedido.getCantidad() * lineaPedido.getPrecioProducto());
        });

        var total = pedido.getLineasPedido().stream()
                .map(linea -> linea.getCantidad() * linea.getPrecioProducto())
                .reduce(0.0, Double::sum);

        var totalItems = pedido.getLineasPedido().stream()
                .map(LineaPedido::getCantidad)
                .reduce(0,Integer::sum);

        pedido.setTotal(total);
        pedido.setTotalItems(totalItems);

        return pedido;
    }
    public void checkPedido(Pedido pedido){
        log.info("Comprobando pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoEmpty(pedido.get_Id());
        }

        pedido.getLineasPedido().forEach(linea -> {
            var producto = productoRepository.findById(linea.getIdProducto()).orElseThrow(() -> new ProductoNotFound("Producto con ID: " + linea.getIdProducto() + " no encontrado"));

            if (producto.getStock() < linea.getCantidad() && linea.getCantidad() > 0) {
                throw new ProductoNotStock("Producto con ID: " + linea.getIdProducto() + " no tiene stock suficiente");
            }

            if (!producto.getPrecio().equals(linea.getPrecioProducto())){
                throw new ProductoBadPrice("Producto con ID: " + linea.getIdProducto() + " tiene un precio erroneo");
            }

            if (!producto.getIsActivo()) throw new ProductoNotActive("Producto con ID: " + linea.getIdProducto() + " no se encuentra activo");
        });
    }
    public Pedido returnStockPedidos(Pedido pedido){
        log.info("Devolviendo stock del pedido: {}", pedido);
        if (pedido.getLineasPedido() != null){
            pedido.getLineasPedido().forEach(linea -> {
                var producto = productoRepository.findById(linea.getIdProducto()).orElseThrow(() -> new ProductoNotFound("Producto con ID " + linea.getIdProducto() + " no encontrado"));
                producto.setStock(producto.getStock() + linea.getCantidad());

                productoRepository.save(producto);
            });
        }
        return pedido;
    }

}
