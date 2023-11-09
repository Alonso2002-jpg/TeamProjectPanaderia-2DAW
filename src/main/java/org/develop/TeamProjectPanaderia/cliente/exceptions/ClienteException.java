package org.develop.TeamProjectPanaderia.cliente.exceptions;

public abstract class ClienteException extends RuntimeException {
    public ClienteException(String message) {
        super(message);
    }
}
