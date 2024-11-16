package com.example.userservice.exception;


import com.example.userservice.dto.Result;
import com.example.userservice.dto.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleEntityNotFoundException(@NotNull EntityNotFoundException ex) {
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleUserNotFound(@NotNull Exception ex) {
        return new Result(false, StatusCode.UNAUTHORIZED, "username or password is incorrect", ex.getMessage());
    }

    @ExceptionHandler({UsernameAlreadyTakenException.class, EmailAlreadyInUseException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleUsernameOrEmailExceptions(@NotNull RuntimeException ex) {
        return new Result(false, StatusCode.INVALID_ARGUMENT, "username or password is incorrect", ex.getMessage());
    }

//    @ExceptionHandler(IllegalAccessException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    Result handleIllegalAccessException(@NotNull IllegalAccessException ex) {
//        return new Result(false, StatusCode.INVALID_ARGUMENT,"Provided arguments are not valid", ex.getMessage());
//    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleHttpMediaTypeNotSupportedException(@NotNull HttpMediaTypeNotSupportedException ex) {
        return new Result(false, StatusCode.INVALID_ARGUMENT, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleConstraintViolationException(@NotNull ConstraintViolationException ex) {
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(@NotNull MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });

        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are not valid", map);
    }


    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(@NotNull AccountStatusException ex) {
        return new Result(false, StatusCode.UNAUTHORIZED, "user account is abnormal", ex.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(@NotNull InvalidBearerTokenException ex) {
        return new Result(false, StatusCode.UNAUTHORIZED, "access token is invalid", ex.getMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException ex) {
        return new Result(false, StatusCode.FORBIDDEN, "No permission", ex.getMessage());
    }
//
//    @ExceptionHandler(InsufficientAuthenticationException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    Result handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
//        return new Result(false, StatusCode.FORBIDDEN, "No permission", ex.getMessage());
//    }
//
//    @ExceptionHandler(PasswordChangeIllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    Result handlePasswordChangeIllegalArgumentException(PasswordChangeIllegalArgumentException ex){
//        return new Result(false, StatusCode.INVALID_ARGUMENT, ex.getMessage());
//    }


    /**
     * Fallback handles any unhandled exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception ex) {
        System.out.println(ex.getClass().getName());
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "Server internal error", ex.getMessage());
    }
}
