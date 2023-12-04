package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de tiempo de ejecución que indica que no se puede borrar un proveedor porque tiene productos asociados.
 * Se lanza cuando se intenta eliminar un proveedor que aún tiene productos vinculados a él.
 *
 * Esta clase extiende la clase base de excepciones `ProveedorException`.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ProveedorNotDeletedException extends ProveedorException {

    /**
     * Constructor que acepta el ID del proveedor como parámetro y lo utiliza para inicializar la excepción.
     *
     * @param id El identificador único del proveedor que no puede ser eliminado.
     */
    public ProveedorNotDeletedException(Long id) {
        super("No se puede borrar el proveedor con id " + id + " porque tiene productos asociados");
    }
}
