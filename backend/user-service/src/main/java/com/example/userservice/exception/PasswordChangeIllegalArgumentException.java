package com.example.userservice.exception;

public class PasswordChangeIllegalArgumentException extends RuntimeException{
    public PasswordChangeIllegalArgumentException(String message) {
        super(message);
    }
}
