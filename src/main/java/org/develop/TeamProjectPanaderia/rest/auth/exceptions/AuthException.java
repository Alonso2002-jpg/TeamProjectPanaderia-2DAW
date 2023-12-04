package org.develop.TeamProjectPanaderia.rest.auth.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con la autenticacion en el sistema.
 *
 * <p>
 * Esta clase extiende la clase RuntimeException y sirve como base para excepciones especificas
 * relacionadas con errores en el proceso de autenticacion.
 * </p>
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public abstract class AuthException extends RuntimeException{
    /**
     * Constructor que acepta un mensaje de error que describe la excepcion.
     *
     * @param message Mensaje que describe la causa de la excepcion.
     */
    public AuthException(String message) {
        super(message);
    }
}
