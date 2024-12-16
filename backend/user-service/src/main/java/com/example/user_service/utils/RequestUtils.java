package com.example.user_service.utils;

import com.example.user_service.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Utility class for generating standardized API response objects.
 *
 * <p>This class provides static methods to create {@code Response} objects that encapsulate
 * information about API responses. The response includes metadata such as the timestamp,
 * HTTP status, request URI, message, and any additional data related to the response.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Response response = RequestUtils.getResponse(request, dataMap, "Operation successful", HttpStatus.OK);
 * </pre>
 */
public class RequestUtils {

    /**
     * Creates a standardized {@code Response} object for an API request.
     *
     * <p>This method constructs a response object with the current timestamp, status code,
     * request URI, status, message, and any additional data. It is useful for creating
     * consistent response objects across the application.</p>
     *
     * @param request the HttpServletRequest object representing the incoming HTTP request
     * @param data the additional data to be included in the response
     * @param message a message providing additional information about the response
     * @param status the HTTP status code associated with the response
     * @return a Response object containing all the provided response details
     */
    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message, HttpStatus status) {
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), message, EMPTY, data);
    }
}

