package com.example.user_service.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Represents a standard response structure used in API responses.
 *
 * <p>This record is used to encapsulate the essential components of an API response,
 * including metadata about the request, the status of the response, and any data
 * or error information associated with it.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @JsonInclude(JsonInclude.Include.NON_DEFAULT)} - Excludes fields with default values from the serialized JSON response.</li>
 * </ul>
 *
 * @param time      The timestamp when the response was generated.
 * @param code      The HTTP status code associated with the response.
 * @param path      The URI path of the request that generated the response.
 * @param status    The HttpStatus enum representing the status of the response (e.g., OK, BAD_REQUEST, etc.).
 * @param message   A human-readable message providing additional information about the response.
 * @param exception The name or type of the exception that occurred (if any).
 * @param data      The payload or data being returned as part of the response.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record Response(String time,
                       int code,
                       String path,
                       HttpStatus status,
                       String message,
                       String exception,
                       Map<?, ?> data) {
}

