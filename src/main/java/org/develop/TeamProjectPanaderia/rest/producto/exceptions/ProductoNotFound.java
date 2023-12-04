package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Excepción que indica que un producto no ha sido encontrado.
 * Se lanza cuando se intenta acceder a un producto que no existe en el sistema.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductoNotFound extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el identificador único (UUID) del producto no encontrado.
     *
     * @param id Identificador único (UUID) del producto no encontrado.
     */
    public ProductoNotFound(UUID id) {
        super("Producto con id " + id + " no encontrado");
    }

    /**
     * Construye una nueva instancia de la excepción con el nombre del producto no encontrado.
     *
     * @param nombre Nombre del producto no encontrado.
     */
    public ProductoNotFound(String nombre) {
        super("Producto con nombre " + nombre + " no encontrado");
    }
}


