package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;
/**
 * {@code ClienteException} es una clase base abstracta para excepciones relacionadas con clientes en la tienda.
 *
 * @extends RuntimeException Clase base para excepciones no verificadas, lo que significa que no se requiere
 * la declaración explícita de estas excepciones en la firma del método o el encabezado del método.
 */
public abstract class ClienteException extends RuntimeException {

    /**
     * Constructor que toma un mensaje de error como parámetro.
     *
     * @param message Mensaje que describe la excepción.
     */
    public ClienteException(String message) {
        super(message);
    }
}
