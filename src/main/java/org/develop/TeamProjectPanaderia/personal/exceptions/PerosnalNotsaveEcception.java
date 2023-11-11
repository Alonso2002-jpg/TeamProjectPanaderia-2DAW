package org.develop.TeamProjectPanaderia.personal.exceptions;

import lombok.Builder;


public abstract class PerosnalNotsaveEcception extends RuntimeException {
    public PerosnalNotsaveEcception(String message) {
        super(message);
    }
}
