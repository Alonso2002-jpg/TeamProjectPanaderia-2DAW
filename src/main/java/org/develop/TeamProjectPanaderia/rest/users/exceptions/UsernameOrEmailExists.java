package org.develop.TeamProjectPanaderia.rest.users.exceptions;

public class UsernameOrEmailExists extends UserException{
    public UsernameOrEmailExists(String message) {
        super("Username or email " + message + " already exists");
    }
}
