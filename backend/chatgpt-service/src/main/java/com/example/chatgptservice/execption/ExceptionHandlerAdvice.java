package com.example.chatgptservice.execption;


import com.example.chatgptservice.dto.Result;
import com.example.chatgptservice.dto.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ChatGptPromptException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleChatGptPromptExceptionException(ChatGptPromptException ex) {
        System.out.println(ex.getClass().getName());
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Prompt error: " + ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleTooManyRequestsException(HttpClientErrorException ex) {
        System.out.println(ex.getClass().getName());
        return new Result(false, StatusCode.FORBIDDEN, "API error: " + ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleTooManyRequestsException(HttpClientErrorException.TooManyRequests ex) {
        System.out.println(ex.getClass().getName());
        return new Result(false, StatusCode.FORBIDDEN, "Rate limit exceeded. Try again later.", ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleException(Exception ex) {
        System.out.println(ex.getClass().getName());
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "Server internal error", ex.getMessage());
    }
}

