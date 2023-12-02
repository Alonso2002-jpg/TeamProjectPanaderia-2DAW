package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

public class PedidoNotFound extends PedidoException {
    public PedidoNotFound(String message) {
        super("Pedido con " + message + " not found");
    }
}
