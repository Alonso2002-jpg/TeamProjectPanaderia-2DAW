package org.develop.TeamProjectPanaderia.rest.proveedores.exceptions;

/**
 * Excepción de tiempo de ejecución personalizada para el módulo de proveedores.
 * Se utiliza como clase base para otras excepciones específicas del dominio de proveedores.
 *
 * Esta clase extiende la clase base de excepciones `RuntimeException`.
 */
public class ProveedorException extends RuntimeException{

    /**
     * Constructor que acepta un mensaje de error y lo utiliza para inicializar la excepción.
     *
     * @param mensaje El mensaje de error asociado con la excepción.
     */
    public ProveedorException(String mensaje){
        super(mensaje);
    }
}