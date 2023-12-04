package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un pedido con la información proporcionada.
 */
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
