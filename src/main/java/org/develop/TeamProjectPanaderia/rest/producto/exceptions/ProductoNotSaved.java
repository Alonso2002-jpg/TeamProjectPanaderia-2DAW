package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que indica que no se pudo guardar un producto debido a un conflicto de nombres duplicados.
 * Se lanza cuando se intenta guardar un nuevo producto con un nombre que ya existe en la base de datos.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoNotSaved extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el nombre del producto que causó el conflicto.
     *
     * @param nombre Nombre del producto que ya existe en la base de datos.
     */
    public ProductoNotSaved(String nombre) {
        super("El producto " + nombre + " ya existe en la BD");
    }
}
