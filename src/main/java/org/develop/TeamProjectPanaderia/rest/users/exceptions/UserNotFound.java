package org.develop.TeamProjectPanaderia.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
/**
 * Excepción lanzada cuando se intenta realizar una operación con un usuario que no se encuentra en el sistema.
 */
public class UserNotFound extends UserException {

    /**
     * Constructor que utiliza el mensaje proporcionado para indicar que el usuario especificado no se encuentra.
     *
     * @param message Detalles adicionales sobre la no existencia del usuario.
     */
    public UserNotFound(String message) {
        super("User with " + message + " not found");
    }
}
