package org.develop.TeamProjectPanaderia.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorNotSaveException extends ProveedorException {
    public ProveedorNotSaveException(String mensaje){
        super(mensaje);
    }
}