package com.example.user_service.exception;

public class ApiException extends RuntimeException {

    public ApiException(String massage) {
        super(massage);
    }

    public ApiException() {
        super("An error occurred");
    }
}
