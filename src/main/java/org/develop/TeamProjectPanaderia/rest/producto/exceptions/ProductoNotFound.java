package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductoNotFound extends ProductoException{
    public ProductoNotFound(UUID id) {
        super("Producto con id " + id + " no encontrado");
    }
    public ProductoNotFound(String nombre) {
        super("Producto con nombre " + nombre + " no encontrado");
    }
}


