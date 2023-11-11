package org.develop.TeamProjectPanaderia.cliente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteNotSaveException extends ClienteException {
    public ClienteNotSaveException(String message) {
        super(message);
    }
}
