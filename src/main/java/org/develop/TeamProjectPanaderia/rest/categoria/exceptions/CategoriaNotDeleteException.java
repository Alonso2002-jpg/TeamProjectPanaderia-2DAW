package org.develop.TeamProjectPanaderia.rest.categoria.exceptions;

/**
 * Excepcion especifica que se lanza cuando no se puede eliminar una categoia.
 * Extiende la clase abstracta CategoriaException y lleva un mensaje descriptivo.
 */
public class CategoriaNotDeleteException extends CategoriaException {
    public CategoriaNotDeleteException(String message) {
        super(message);
    }
}
