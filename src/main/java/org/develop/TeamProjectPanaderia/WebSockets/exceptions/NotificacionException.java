package org.develop.TeamProjectPanaderia.WebSockets.exceptions;
/**
 * Clase base abstracta para excepciones relacionadas con notificaciones.
 * Extiende RuntimeException para indicar que es una excepcion no verificada.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public abstract class NotificacionException extends RuntimeException {

    /**
     * Constructor de la clase NotificacionException.
     *
     * @param message El mensaje de la excepcion.
     */
    public NotificacionException(String message) {
        super(message);
    }
}
