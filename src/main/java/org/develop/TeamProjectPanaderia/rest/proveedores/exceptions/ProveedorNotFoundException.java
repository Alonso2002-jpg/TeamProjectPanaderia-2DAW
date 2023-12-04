package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de tiempo de ejecución que indica que un proveedor no se encontró en la base de datos.
 * Se lanza cuando se intenta acceder a un proveedor que no existe.
 *
 * Esta clase extiende la clase base de excepciones `ProveedorException`.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProveedorNotFoundException extends ProveedorException {

    /**
     * Constructor que acepta el ID del proveedor como parámetro y lo utiliza para inicializar la excepción.
     *
     * @param id El identificador único del proveedor que no se pudo encontrar.
     */
    public ProveedorNotFoundException(String id){
        super("Proveedor : "+id+" no encontrado");
    }
}