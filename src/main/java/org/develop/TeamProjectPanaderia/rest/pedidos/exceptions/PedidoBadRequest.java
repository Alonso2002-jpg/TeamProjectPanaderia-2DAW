package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoBadRequest extends PedidoException {
    public PedidoBadRequest(String message) {
        super(message);
    }
}
