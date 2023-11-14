package org.develop.TeamProjectPanaderia.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalNotSaved extends RuntimeException {
    public PersonalNotSaved(String dni) {
        super("El dni " + dni + " ya esta registrado en la BD ");
    }
}
