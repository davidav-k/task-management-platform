package com.example.userservice.exception;

public class AlreadyExitsException extends RuntimeException{
    public AlreadyExitsException(String message) {
        super(message);
    }
}
