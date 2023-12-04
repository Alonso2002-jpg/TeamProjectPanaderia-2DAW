package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se detecta un precio no válido para un producto.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoBadPrice extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public ProductoBadPrice(String message) {
        super(message);
    }
}
