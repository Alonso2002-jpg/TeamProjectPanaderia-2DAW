package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta realizar una operación en un pedido vacío.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoEmpty extends PedidoException {

    /**
     * Constructor que acepta un mensaje personalizado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public PedidoEmpty(String message) {
        super(message);
    }
}
