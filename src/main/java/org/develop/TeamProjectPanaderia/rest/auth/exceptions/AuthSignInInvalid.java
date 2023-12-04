package org.develop.TeamProjectPanaderia.rest.auth.exceptions;

/**
 * Excepcion especifica que representa un intento de inicio de sesion no valido en el sistema de autenticacion.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public class AuthSignInInvalid extends AuthException{
    /**
     * Constructor que acepta un mensaje de error que describe la causa de la excepcion.
     *
     * @param message Mensaje que describe la causa de la excepcion.
     */
    public AuthSignInInvalid(String message) {
        super(message);
    }
}
