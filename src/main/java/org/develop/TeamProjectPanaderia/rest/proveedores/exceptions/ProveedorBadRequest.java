package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

import org.develop.TeamProjectPanaderia.rest.personal.exceptions.PersonalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que indica un error relacionado con una solicitud incorrecta de proveedor.
 * Se lanza cuando se intenta realizar una operación de proveedor y la categoría asociada no existe.
 *
 * Esta clase extiende la clase base de excepciones `ProveedorException`.
 *
 * @see ProveedorException Clase base de excepciones para el módulo de proveedores.
 * @see org.springframework.web.bind.annotation.ResponseStatus Se utiliza para asignar un código de estado HTTP a la excepción.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorBadRequest extends ProveedorException {

    /**
     * Constructor que acepta el nombre de la categoría inexistente y genera un mensaje de error.
     *
     * @param categoria El nombre de la categoría inexistente.
     */
    public ProveedorBadRequest(String categoria) {
        super("La categoria con nombre " + categoria + " no existe");
    }

}
