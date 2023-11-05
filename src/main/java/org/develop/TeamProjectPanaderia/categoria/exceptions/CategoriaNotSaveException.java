package org.develop.TeamProjectPanaderia.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaNotSaveException extends CategoriaException{
    public CategoriaNotSaveException(String message) {
        super(message);
    }
}
