package org.develop.TeamProjectPanaderia.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
/**
 * Excepción lanzada cuando se intenta crear un nuevo usuario con un nombre de usuario
 * o una dirección de correo electrónico que ya existen en el sistema.
 */
public class UsernameOrEmailExists extends UserException{
    /**
     * Constructor que utiliza el mensaje proporcionado para indicar que el nombre de usuario
     * o la dirección de correo electrónico especificados ya existen en el sistema.
     *
     * @param message Detalles adicionales sobre la existencia del nombre de usuario o correo electrónico.
     */
    public UsernameOrEmailExists(String message) {
        super("Username or email " + message + " already exists");
    }
}
