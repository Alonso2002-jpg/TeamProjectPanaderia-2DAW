package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClienteNotSaveException extends ClienteException {
    public ClienteNotSaveException(String dni) {
        super("El dni " + dni + " ya existe en la BD");
    }
}


