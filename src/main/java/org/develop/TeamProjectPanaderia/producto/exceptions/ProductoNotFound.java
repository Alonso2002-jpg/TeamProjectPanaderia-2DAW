package org.develop.TeamProjectPanaderia.producto.exceptions;

import java.util.UUID;

public class ProductoNotFound extends ProductoException{
    public ProductoNotFound(UUID id) {
        super("Producto con id " + id +" no encontrado");
    }
    public ProductoNotFound(String nombre) {
        super("Producto con nombre " + nombre +" no encontrado");
    }
}


