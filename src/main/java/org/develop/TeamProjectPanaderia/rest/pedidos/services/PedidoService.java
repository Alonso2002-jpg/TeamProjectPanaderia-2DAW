package org.develop.TeamProjectPanaderia.rest.pedidos.services;

import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.develop.TeamProjectPanaderia.rest.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz que define los servicios relacionados con la entidad Pedido.
 */
public interface PedidoService {


    /**
     * Obtiene una página de todos los pedidos.
     *
     * @param pageable Configuración de paginación.
     * @return Página de pedidos.
     */
    Page<Pedido> findAll(Pageable pageable);

    /**
     * Obtiene un pedido por su identificador único.
     *
     * @param id Identificador único del pedido.
     * @return Pedido encontrado.
     */
    Pedido findById(ObjectId id);

    /**
     * Obtiene una página de pedidos para un usuario específico.
     *
     * @param idUsuario Identificador del usuario.
     * @param pageable  Configuración de paginación.
     * @return Página de pedidos para el usuario especificado.
     */
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);

    /**
     * Guarda un nuevo pedido.
     *
     * @param pedido Pedido a ser guardado.
     * @return Pedido guardado.
     */

    Pedido save(Pedido pedido);

    /**
     * Elimina un pedido por su identificador único.
     *
     * @param id Identificador único del pedido a ser eliminado.
     */
    void deleteById(ObjectId id);

    /**
     * Actualiza un pedido existente.
     *
     * @param id     Identificador único del pedido a ser actualizado.
     * @param pedido Pedido con los cambios.
     * @return Pedido actualizado.
     */
    Pedido update(ObjectId id,Pedido pedido);

    /**
     * Verifica si existe algún pedido asociado a un producto específico.
     *
     * @param id Identificador del producto.
     * @return Verdadero si hay pedidos asociados al producto, falso en caso contrario.
     */
    Boolean findByIdProducto(Long id);
}
