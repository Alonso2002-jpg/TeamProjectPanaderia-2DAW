package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Excepcion especifica que se lanza cuando no se puede guardar una categoria.
 * Extiende la clase abstracta CategoriaException y lleva un mensaje descriptivo.
 * Tiene la anotacion @ResponseStatus para indicar que se debe responder con un codigo de estado BAD_REQUEST (400).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaNotSaveException extends CategoriaException{
    public CategoriaNotSaveException(String message) {
        super(message);
    }
}
