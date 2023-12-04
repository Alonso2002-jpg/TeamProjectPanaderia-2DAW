package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con productos en el sistema.
 * Extiende la clase RuntimeException.
 */
public abstract class ProductoException extends RuntimeException {

    /**
     * Construye una nueva instancia de la excepción con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public ProductoException(String message) {
        super(message);
    }
}
