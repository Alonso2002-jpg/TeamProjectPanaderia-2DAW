package org.develop.TeamProjectPanaderia.rest.auth.exceptions;

/**
 * Excepcion especifica que indica que las contrasenas proporcionadas son invalidas o no coinciden.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public class UserInvalidPasswords extends AuthException{
    /**
     * Constructor que acepta un mensaje de error que describe la causa de la excepcion.
     *
     * @param message Mensaje que describe la causa de la excepcion.
     */
    public UserInvalidPasswords(String message) {
        super(message);
    }
}
