package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que indica que no se ha podido guardar la información de un trabajador (Personal) porque el DNI ya está registrado en la base de datos.
 * Se lanza cuando se intenta guardar un nuevo trabajador con un DNI que ya existe en la base de datos.
 *
 * @RuntimeException Clase base para excepciones de tiempo de ejecución no verificadas.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalNotSaved extends RuntimeException {

    /**
     * Constructor que recibe el DNI que ya está registrado en la base de datos.
     *
     * @param dni DNI que ya está registrado en la base de datos y no puede ser duplicado.
     */
    public PersonalNotSaved(String dni) {
        super("El dni " + dni + " ya esta registrado en la BD ");
    }
}
