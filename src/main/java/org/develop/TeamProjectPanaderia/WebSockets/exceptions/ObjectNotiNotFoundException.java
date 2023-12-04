package org.develop.TeamProjectPanaderia.WebSockets.exceptions;

/**
 * Excepcion personalizada para representar la falta de un objeto en el contexto de notificaciones.
 * Extiende NotificacionException para indicar que es una excepcion especifica de notificaciones.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public class ObjectNotiNotFoundException extends NotificacionException {

    /**
     * Constructor de la clase ObjectNotiNotFoundException.
     *
     * @param message El mensaje de la excepcion.
     */
    public ObjectNotiNotFoundException(String message) {
        super(message);
    }
}
