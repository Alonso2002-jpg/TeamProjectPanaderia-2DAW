package org.develop.TeamProjectPanaderia.rest.producto.exceptions;

/**
 * Excepción que indica que no hay suficiente stock disponible para realizar una operación en un producto.
 * Se lanza cuando se intenta realizar una operación que requiere más stock del disponible en el producto.
 */
public class ProductoNotStock extends ProductoException{

    /**
     * Construye una nueva instancia de la excepción con el mensaje especificado.
     *
     * @param message Mensaje que describe la razón de la excepción.
     */
    public ProductoNotStock(String message) {
        super(message);
    }
}
