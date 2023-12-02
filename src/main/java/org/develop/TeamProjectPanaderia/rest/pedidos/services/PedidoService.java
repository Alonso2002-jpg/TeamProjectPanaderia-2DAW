package org.develop.TeamProjectPanaderia.rest.pedidos.services;

import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {
    Page<Pedido> findAll(Pageable pageable);
    Pedido findById(ObjectId id);
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);
    Pedido save(Pedido pedido);
    void deleteById(ObjectId id);
    Pedido update(ObjectId id,Pedido pedido);
    Boolean findByIdProducto(Long id);
}
