package org.develop.TeamProjectPanaderia.rest.users.exceptions;
/**
 * Clase base abstracta para excepciones relacionadas con usuarios en la aplicación.
 * Extiende la clase RuntimeException para indicar que es una excepción no verificada.
 */
public abstract class UserException extends RuntimeException {
    /**
     * Constructor que permite establecer un mensaje descriptivo para la excepción.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public UserException(String message) {
        super(message);
    }
}
