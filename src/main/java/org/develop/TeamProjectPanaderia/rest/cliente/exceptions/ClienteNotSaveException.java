package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * {@code ClienteNotSaveException} es una clase de excepción específica para casos en los que se intenta
 * guardar un cliente con un número de identificación (DNI) que ya existe en la base de datos.
 *
 * @ResponseStatus(HttpStatus.BAD_REQUEST) Anotación que indica que, cuando esta excepción se lanza,
 * se debería devolver un código de estado HTTP 400 (Bad Request).
 * @extends ClienteException Clase base para excepciones relacionadas con clientes en la tienda.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteNotSaveException extends ClienteException {

    /**
     * Constructor que toma el número de identificación (DNI) del cliente como parámetro.
     *
     * @param dni Número de identificación (DNI) del cliente que ya existe en la base de datos.
     */
    public ClienteNotSaveException(String dni) {
        super("El dni " + dni + " ya existe en la BD");
    }
}


