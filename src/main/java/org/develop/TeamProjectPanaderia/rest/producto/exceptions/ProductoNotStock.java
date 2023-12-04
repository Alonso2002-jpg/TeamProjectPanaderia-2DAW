package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que indica que no hay suficiente stock disponible para realizar una operación en un producto.
 * Se lanza cuando se intenta realizar una operación que requiere más stock del disponible en el producto.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoNotStock extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje especificado.
     *
     * @param message Mensaje que describe la razón de la excepción.
     */
    public ProductoNotStock(String message) {
        super(message);
    }
}
