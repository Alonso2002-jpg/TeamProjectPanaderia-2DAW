package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción que indica que no se ha encontrado al trabajador (Personal).
 * Se lanza cuando se intenta buscar un trabajador por su DNI o ID y no se encuentra en el sistema.
 *
 * @RuntimeException Clase base para excepciones de tiempo de ejecución no verificadas.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonalNotFoundException extends RuntimeException{

    /**
     * Constructor que recibe el DNI del trabajador que no se encontró.
     *
     * @param dni DNI del trabajador que no se encontró.
     */
    public PersonalNotFoundException(String dni) {
        super("Personal con dni " + dni + " no encontrado");
    }

    /**
     * Constructor que recibe el identificador único del trabajador que no se encontró.
     *
     * @param uuid Identificador único del trabajador que no se encontró.
     */
    public PersonalNotFoundException(UUID uuid) {
        super("Personal con id " + uuid + " no encontrado");
    }
}