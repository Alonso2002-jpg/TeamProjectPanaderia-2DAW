package org.develop.TeamProjectPanaderia.rest.producto.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Construye una nueva instancia de la excepción con el mensaje que indica que el UUID proporcionado no es válido o tiene un formato incorrecto.
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoBadUuid extends ProductoException {

    /**
     * Construye una nueva instancia de la excepción con el mensaje que indica que el UUID proporcionado no es válido o tiene un formato incorrecto.
     *
     * @param uuid UUID no válido o con formato incorrecto.
     */
    public ProductoBadUuid(String uuid) {
        super("UUID: " + uuid + " no válido o de formato incorrecto");
    }
}