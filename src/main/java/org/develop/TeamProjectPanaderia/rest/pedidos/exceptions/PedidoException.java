package org.develop.TeamProjectPanaderia.rest.pedidos.exceptions;
/**
 * Clase base abstracta para excepciones relacionadas con operaciones de pedidos.
 * Extiende RuntimeException para permitir excepciones no comprobadas.
 */
public abstract class PedidoException extends RuntimeException{

    /**
     * Constructor que acepta un mensaje personalizado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public PedidoException(String message){
            super(message);
        }
}
