package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalBadRequest extends PersonalException {
    public PersonalBadRequest(String categoria) {
        super("La categoria con nombre " + categoria + " no existe");
    }
}
