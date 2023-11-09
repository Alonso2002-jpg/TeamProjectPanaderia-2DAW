package org.develop.TeamProjectPanaderia.personal.exceptions;

public abstract class PersonalNotFoundException extends RuntimeException{
    public PersonalNotFoundException(String message) {
        super(message);
    }
}
