package org.develop.TeamProjectPanaderia.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Excepcion personalizada para representar errores de recurso no encontrado en operaciones de almacenamiento.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends StorageExceptions{
    /**
     * Constructor de la clase StorageNotFoundException.
     *
     * @param message El mensaje de la excepcion.
     */
    public StorageNotFoundException(String message) {
        super(message);
    }
}
