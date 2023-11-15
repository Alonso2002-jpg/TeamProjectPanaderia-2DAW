package org.develop.TeamProjectPanaderia.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProveedorNotFoundException extends ProveedorException {
    public ProveedorNotFoundException(String id){
        super("Proveedor : "+id+" no encontrado");
    }
}