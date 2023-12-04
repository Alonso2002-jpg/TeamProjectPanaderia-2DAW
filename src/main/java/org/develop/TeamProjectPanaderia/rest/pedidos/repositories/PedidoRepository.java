package org.develop.TeamProjectPanaderia.rest.pedidos.repositories;

import org.bson.types.ObjectId;
import org.develop.TeamProjectPanaderia.rest.pedidos.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repositorio para la entidad Pedido en MongoDB.
 */
@Repository
public interface PedidoRepository extends MongoRepository<Pedido, ObjectId> {

    /**
     * Obtiene una página de pedidos para un usuario específico.
     *
     * @param idUsuario Identificador del usuario.
     * @param pageable  Configuración de paginación.
     * @return Página de pedidos para el usuario especificado.
     */
    Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable);

    /**
     * Obtiene la lista de pedidos para un usuario específico.
     *
     * @param idUsuario Identificador del usuario.
     * @return Lista de pedidos para el usuario especificado.
     */
    List<Pedido> findPedidosByIdUsuario(Long idUsuario);


    /**
     * Verifica si existen pedidos para un usuario específico.
     *
     * @param idUsuario Identificador del usuario.
     * @return Verdadero si existen pedidos para el usuario especificado, falso en caso contrario.
     */
    boolean existsByIdUsuario(Long idUsuario);
}
