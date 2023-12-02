package org.develop.TeamProjectPanaderia.rest.users.exceptions;

public class UserNotFound extends UserException {
    public UserNotFound(String message) {
        super("User with " + message + " not found");
    }
}
