package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de tiempo de ejecución que indica que no se pudo guardar un proveedor en la base de datos.
 * Se lanza cuando hay un problema al intentar almacenar un proveedor.
 *
 * Esta clase extiende la clase base de excepciones `ProveedorException`.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorNotSaveException extends ProveedorException {

    /**
     * Constructor que acepta un mensaje como parámetro y lo utiliza para inicializar la excepción.
     *
     * @param mensaje El mensaje que describe la razón por la cual no se pudo guardar el proveedor.
     */
    public ProveedorNotSaveException(String mensaje){
        super(mensaje);
    }
}