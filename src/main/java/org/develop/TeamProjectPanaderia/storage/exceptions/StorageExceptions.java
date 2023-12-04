package org.develop.TeamProjectPanaderia.storage.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con operaciones de almacenamiento.
 * Extiende RuntimeException para indicar que es una excepcion no verificada.
 *
 * @author Joselyn Obando, Miguel Zanotto, Alonso Cruz, Kevin Bermudez, Laura Garrido.
 */
public abstract class StorageExceptions extends RuntimeException{
    /**
     * Constructor de la clase StorageExceptions.
     *
     * @param message El mensaje de la excepcion.
     */
    public StorageExceptions(String message) {
        super(message);
    }
}
