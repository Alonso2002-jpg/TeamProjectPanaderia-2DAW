package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * {@code ClienteNotFoundException} es una clase de excepción específica para casos en los que un cliente
 * no se encuentra en la tienda, ya sea por su identificador único o por su número de identificación (DNI).
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND) Anotación que indica que, cuando esta excepción se lanza,
 * se debería devolver un código de estado HTTP 404 (Not Found).
 * @extends CategoriaException Clase base para excepciones relacionadas con categorías en la tienda.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFoundException extends CategoriaException {
    /**
     * Constructor que toma el identificador único del cliente como parámetro.
     *
     * @param id Identificador único del cliente no encontrado.
     */
    public ClienteNotFoundException(Long id) {
        super("Cliente not found with id " + id);
    }
    /**
     * Constructor que toma el número de identificación (DNI) del cliente como parámetro.
     *
     * @param dni Número de identificación (DNI) del cliente no encontrado.
     */
    public ClienteNotFoundException(String dni) {
        super("Cliente con dni " + dni + " no encontrado");
    }
}
