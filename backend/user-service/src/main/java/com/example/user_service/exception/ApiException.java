package com.example.user_service.exception;

/**
 * Custom exception class for handling application-specific errors.
 *
 * <p>This class extends {@code RuntimeException}, allowing it to be thrown during
 * normal operation without being explicitly declared in a method's throws clause.
 * It provides constructors to create an exception with a custom message or a default
 * message when no specific message is provided.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     throw new ApiException("Custom error message");
 * </pre>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>None</li>
 * </ul>
 */
public class ApiException extends RuntimeException {

    /**
     * Constructs a new ApiException with the specified detail message.
     *
     * @param message the detail message for this exception
     */
    public ApiException(String message) {
        super(message);
    }

    /**
     * Constructs a new ApiException with a default message "An error occurred".
     */
    public ApiException() {
        super("An error occurred");
    }
}

