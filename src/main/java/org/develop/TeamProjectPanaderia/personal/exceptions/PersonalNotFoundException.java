package org.develop.TeamProjectPanaderia.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonalNotFoundException extends RuntimeException{
    public PersonalNotFoundException(String dni) {
        super("Personal con dni " + dni + " no encontrado");
    }

    public PersonalNotFoundException(UUID uuid) {
        super("Personal con id " + uuid + " no encontrado");
    }
}