package org.develop.TeamProjectPanaderia.rest.pedidos.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementación del servicio para gestionar pedidos.
 *
 * Este servicio proporciona métodos para crear, actualizar, eliminar y obtener información
 * relacionada con pedidos, incluyendo la lógica para reservar y devolver stock.
 *
 * @author  Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 * @version 1.0
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "pedidos")
public class PedidoServiceImpl implements PedidoService{
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    /**
     * Constructor de la clase que inyecta las dependencias necesarias.
     *
     * @param pedidoRepository    Repositorio de pedidos.
     * @param productoRepository  Repositorio de productos.
     */
    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene una página de todos los pedidos.
     *
     * @param pageable Configuración de paginación.
     * @return Página de pedidos.
     */
    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        log.info("Buscando todos los pedidos");
        return pedidoRepository.findAll(pageable);
    }

    /**
     * Obtiene un pedido por su identificador único.
     *
     * @param id Identificador único del pedido.
     * @return Pedido encontrado.
     */
    @Override
    @Cacheable(key = "#id")
    public Pedido findById(ObjectId id) {
        log.info("Obteniendo Pedido con ID: " + id);
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFound("id "+ id.toHexString()));
    }

    /**
     * Obtiene una página de pedidos para un usuario específico.
     *
     * @param idUsuario Identificador del usuario.
     * @param pageable  Configuración de paginación.
     * @return Página de pedidos para el usuario especificado.
     */
    @Override
    public Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable) {
        log.info(("Obteniendo pedidos de usuario con ID: " + idUsuario));
        return pedidoRepository.findByIdUsuario(idUsuario, pageable);
    }

    /**
     * Guarda un nuevo pedido.
     *
     * @param pedido Pedido a ser guardado.
     * @return Pedido guardado.
     */
    @Override
    @CachePut(key = "#pedido.id")
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido {}", pedido);

        checkPedido(pedido);

        var pedSave = reserveStockPedidos(pedido);

        pedSave.setCreatedAt(LocalDateTime.now());
        pedSave.setUpdatedAt(LocalDateTime.now());

        return pedidoRepository.save(pedSave);
    }

    /**
     * Actualiza un pedido existente.
     *
     * @param id     Identificador único del pedido a ser actualizado.
     * @param pedido Pedido con los cambios.
     * @return Pedido actualizado.
     */
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

    /**
     * Elimina un pedido por su ID, devuelve el stock y actualiza la caché.
     *
     * @param id ID del pedido a eliminar
     * @throws PedidoNotFound si el pedido no se encuentra
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteById(ObjectId id) {
        log.info("Eliminando Pedido con ID: " + id);

        var pedDel = pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFound("id "+ id.toHexString()));

        returnStockPedidos(pedDel);

        pedidoRepository.deleteById(id);
    }

    /**
     * Verifica si existe un producto por su ID.
     *
     * @param id ID del producto a verificar
     * @return true si el producto existe, false de lo contrario
     */
    @Override
    public Boolean findByIdProducto(Long id) {
        return null;
    }

    /**
     * Reserva el stock de productos para un pedido.
     *
     * @param pedido Pedido para el cual se reserva el stock
     * @return Pedido con el stock reservado
     * @throws PedidoEmpty si el pedido no tiene líneas de pedido
     */
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

    /**
     * Verifica la validez de un pedido.
     *
     * @param pedido Pedido a verificar
     * @throws PedidoEmpty si el pedido no tiene líneas de pedido
     * @throws ProductoNotStock si un producto no tiene suficiente stock
     * @throws ProductoBadPrice si el precio de un producto es incorrecto
     * @throws ProductoNotActive si un producto no está activo
     */
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

    /**
     * Devuelve el stock de productos para un pedido.
     *
     * @param pedido Pedido para el cual se devuelve el stock
     * @return Pedido con el stock devuelto
     */
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
