package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProveedorNotDeletedException extends ProveedorException {

    public ProveedorNotDeletedException(Long id) {
        super("No se puede borrar el proveedor con id " + id + " porque tiene productos asociados");
    }
}
