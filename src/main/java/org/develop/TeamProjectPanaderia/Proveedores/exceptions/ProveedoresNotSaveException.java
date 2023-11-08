package org.develop.TeamProjectPanaderia.Proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedoresNotSaveException extends ProveedoresException{
    public ProveedoresNotSaveException(String mensaje){
        super(mensaje);
    }
}
