package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion de base abstracta para manejar errores relacionados con operaciones en categorias.
 * La anotacion @ResponseStatus se utiliza para indicar que, cuando se lanza esta excepcion, se debe responder
 * con un codigo de estado HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class CategoriaException extends RuntimeException{
    public CategoriaException(String message) {
        super(message);
    }
}
