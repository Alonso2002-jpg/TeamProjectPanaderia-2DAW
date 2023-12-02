package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteBadRequest extends ClienteException{
    public ClienteBadRequest(String categoria) {
        super("La categoria con nombre " + categoria + " no existe");
    }
}
