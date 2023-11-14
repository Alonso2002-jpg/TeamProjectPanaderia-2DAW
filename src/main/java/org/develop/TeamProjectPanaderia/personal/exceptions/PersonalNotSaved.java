package org.develop.TeamProjectPanaderia.personal.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PerosnalNotsaveEcception extends RuntimeException {
    public PerosnalNotsaveEcception(String dni) {
        super("El dni " + dni + " ya esta registrado en la BD ");
    }
}
