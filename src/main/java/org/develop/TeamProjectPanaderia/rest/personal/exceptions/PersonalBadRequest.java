package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Excepción personalizada para representar un error en la solicitud relacionado con el trabajador (Personal),
 * con código de respuesta HTTP BAD_REQUEST.
 *
 * @PersonalException Clase base para excepciones personalizadas relacionadas con el trabajador (Personal).
 * @ResponseStatus     Anotación que indica el código de respuesta HTTP que se enviará en caso de excepción.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalBadRequest extends PersonalException {


    /**
     * Constructor que recibe un mensaje descriptivo para la excepción.
     *
     * @param message Mensaje descriptivo que detalla la razón de la excepción.
     */

    public PersonalBadRequest(String message) {
        super(message);
    }
}
