package org.develop.TeamProjectPanaderia.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Excepcion personalizada para representar errores internos del servidor en operaciones de almacenamiento.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternalException extends StorageExceptions{
    /**
     * Constructor de la clase StorageInternalException.
     *
     * @param message El mensaje de la excepcion.
     */
    public StorageInternalException(String message) {
        super(message);
    }
}
