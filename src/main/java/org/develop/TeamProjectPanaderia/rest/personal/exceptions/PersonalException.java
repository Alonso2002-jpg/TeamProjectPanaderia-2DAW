package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

/**
 * Clase base para excepciones personalizadas relacionadas con el trabajador (Personal).
 * Extiende la clase RuntimeException para representar excepciones que pueden ocurrir durante operaciones
 * relacionadas con el personal y proporciona un mensaje descriptivo.
 *
 * @RuntimeException Clase base para excepciones de tiempo de ejecución no verificadas.
 */
public class PersonalException extends RuntimeException{

    /**
     * Constructor que recibe un mensaje descriptivo para la excepción.
     *
     * @param message Mensaje descriptivo que detalla la naturaleza de la excepción.
     */
    public PersonalException(String message) {
        super(message);
    }

}
