package org.develop.TeamProjectPanaderia.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductoNotSaved extends ProductoException{

    public ProductoNotSaved(String nombre) {
        super("El producto " + nombre + " ya existe en la BD");
    }
}
