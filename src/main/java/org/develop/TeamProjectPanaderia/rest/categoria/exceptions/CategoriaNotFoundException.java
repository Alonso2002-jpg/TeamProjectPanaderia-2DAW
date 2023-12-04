package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Excepcion especifica que se lanza cuando no se encuentra una categoria.
 * Extiende la clase abstracta CategoriaException y lleva un mensaje descriptivo.
 * Tiene la anotacion @ResponseStatus para indicar que se debe responder con un codigo de estado NOT_FOUND (404).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends CategoriaException{
    public CategoriaNotFoundException(String message) {
        super("Categoria not found with " + message);
    }
}
