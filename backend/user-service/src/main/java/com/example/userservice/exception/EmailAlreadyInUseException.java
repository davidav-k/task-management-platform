package com.example.userservice.exception;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException (String message) {
        super(message);
    }
}
