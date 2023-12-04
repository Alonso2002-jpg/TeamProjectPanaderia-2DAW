package org.develop.TeamProjectPanaderia.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion personalizada para representar errores de solicitud incorrecta en operaciones de almacenamiento.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequestException extends StorageExceptions{
    /**
     * Constructor de la clase StorageBadRequestException.
     *
     * @param message El mensaje de la excepcion.
     */
    public StorageBadRequestException(String message) {
        super(message);
    }
}
