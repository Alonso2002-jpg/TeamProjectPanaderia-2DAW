package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un pedido con la información proporcionada.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoNotFound extends PedidoException {

    /**
     * Constructor que acepta un mensaje personalizado.
     *
     * @param message Información adicional sobre la no existencia del pedido.
     */
    public PedidoNotFound(String message) {
        super("Pedido con " + message + " not found");
    }
}
