package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se produce una solicitud incorrecta relacionada con un producto.
 * Puede deberse a datos de entrada no válidos u otros problemas relacionados con la solicitud.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoBadRequest extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public ProductoBadRequest(String message) {
        super(message);
    }
}
