package org.develop.TeamProjectPanaderia.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameOrEmailExists extends UserException{
    public UsernameOrEmailExists(String message) {
        super("Username or email " + message + " already exists");
    }
}
