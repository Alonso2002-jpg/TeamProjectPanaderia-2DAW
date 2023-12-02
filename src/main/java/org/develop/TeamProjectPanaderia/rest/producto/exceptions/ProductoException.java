package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

public abstract class ProductoException extends RuntimeException {
    public ProductoException(String message) {
        super(message);
    }
}
