package org.develop.TeamProjectPanaderia.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class PersonalNotFoundException extends RuntimeException{
    public PersonalNotFoundException(String message) {
        super(message);
    }
}
