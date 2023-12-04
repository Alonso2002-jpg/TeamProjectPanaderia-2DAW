package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * {@code ClienteBadRequest} es una clase de excepción específica para casos en los que se produce
 * una solicitud incorrecta relacionada con un cliente, como una categoría inexistente.
 *
 * @ResponseStatus(HttpStatus.BAD_REQUEST) Anotación que indica que, cuando esta excepción se lanza,
 * se debería devolver un código de estado HTTP 400 (Bad Request).
 * @extends ClienteException Clase base para excepciones relacionadas con clientes en la tienda.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteBadRequest extends ClienteException{

    /**
     * Constructor que toma el nombre de la categoría inexistente como parámetro.
     *
     * @param categoria El nombre de la categoría inexistente que causó la excepción.
     */
    public ClienteBadRequest(String categoria) {
        super("La categoria con nombre " + categoria + " no existe");
    }
}
