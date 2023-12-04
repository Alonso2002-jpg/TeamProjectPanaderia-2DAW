package org.develop.TeamProjectPanaderia.rest.auth.exceptions;

/**
 * Excepcion especifica que indica que ya existe un usuario o correo electronico en el sistema de autenticacion.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public class AuthUserOrEmailExists extends AuthException{
    /**
     * Constructor que acepta un mensaje de error que describe la causa de la excepcion.
     *
     * @param message Mensaje que describe la causa de la excepcion.
     */
    public AuthUserOrEmailExists(String message) {
        super(message);
    }
}
