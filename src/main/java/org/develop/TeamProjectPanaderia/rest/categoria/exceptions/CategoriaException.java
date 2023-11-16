package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class CategoriaException extends RuntimeException{
    public CategoriaException(String message) {
        super(message);
    }
}
