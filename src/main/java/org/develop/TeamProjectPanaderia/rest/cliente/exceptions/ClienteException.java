package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

public abstract class ClienteException extends RuntimeException {
    public ClienteException(String message) {
        super(message);
    }
}
