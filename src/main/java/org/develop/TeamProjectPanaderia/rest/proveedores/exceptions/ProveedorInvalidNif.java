package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Constructor que acepta el NIF duplicado como parámetro y lo utiliza para inicializar la excepción.
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorInvalidNif extends ProveedorException{

    /**
     * Constructor que acepta el NIF duplicado como parámetro y lo utiliza para inicializar la excepción.
     *
     * @param nif El NIF que causó la excepción.
     */
    public ProveedorInvalidNif(String nif) {
        super("El nif " + nif + " ya existe en la base de datos");
    }
}
