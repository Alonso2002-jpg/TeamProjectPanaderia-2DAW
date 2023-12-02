package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorBadRequest extends ProveedorException {
    public ProveedorBadRequest(String categoria) {
        super("La categoria con nombre " + categoria + " no existe");
    }

}
