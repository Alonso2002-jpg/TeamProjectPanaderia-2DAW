package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends CategoriaException{
    public CategoriaNotFoundException(String message) {
        super("Categoria not found with " + message);
    }
}
