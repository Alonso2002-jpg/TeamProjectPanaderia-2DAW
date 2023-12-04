package org.develop.TeamProjectPanaderia.rest.personal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para representar un error en la solicitud relacionado con un UUID no válido o de formato incorrecto
 * en el contexto del trabajador (Personal), con código de respuesta HTTP BAD_REQUEST.
 *
 * @PersonalException Clase base para excepciones personalizadas relacionadas con el trabajador (Personal).
 * @ResponseStatus     Anotación que indica el código de respuesta HTTP que se enviará en caso de excepción.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalBadUuid extends PersonalException {

    /**
     * Constructor que recibe el UUID que causó la excepción y genera un mensaje descriptivo.
     *
     * @param uuid UUID que causó la excepción.
     */
    public PersonalBadUuid(String uuid) {
        super("UUID: " + uuid + " no válido o de formato incorrecto");
    }
}