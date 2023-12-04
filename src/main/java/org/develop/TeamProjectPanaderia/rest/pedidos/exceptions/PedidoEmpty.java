package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

/**
 * Excepción lanzada cuando se intenta realizar una operación en un pedido vacío.
 */
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
