package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que indica que un producto no está activo.
 * Se lanza cuando se intenta realizar una operación en un producto no activo en el sistema.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoNotActive extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public ProductoNotActive(String message) {
        super(message);
    }
}
