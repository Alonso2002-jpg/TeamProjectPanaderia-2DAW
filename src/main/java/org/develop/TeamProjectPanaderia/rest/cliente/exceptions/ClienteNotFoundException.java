package org.develop.TeamProjectPanaderia.rest.cliente.exceptions;

import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFoundException extends CategoriaException {
    public ClienteNotFoundException(Long id) {
        super("Cliente not found with id " + id);
    }
    public ClienteNotFoundException(String dni) {
        super("Cliente con dni " + dni + " no encontrado");
    }
}
