package org.develop.TeamProjectPanaderia.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProveedoresNotFoundException extends ProveedoresException{
    public ProveedoresNotFoundException (Long id){
        super("Proveedores con id:"+id+"No encontrado");
    }
}
