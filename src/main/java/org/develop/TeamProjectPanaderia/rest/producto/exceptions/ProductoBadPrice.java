package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

/**
 * Excepción lanzada cuando se detecta un precio no válido para un producto.
 */
public class ProductoBadPrice extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public ProductoBadPrice(String message) {
        super(message);
    }
}
