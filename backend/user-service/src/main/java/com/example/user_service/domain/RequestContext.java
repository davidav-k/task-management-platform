package com.example.user_service.domain;


/**
 * Utility class for managing request-scoped context data, specifically the user ID.
 *
 * <p>This class makes use of {@code ThreadLocal} to store the user ID associated
 * with the current thread. It provides methods for setting, retrieving, and clearing
 * the user ID. The context is automatically cleared at the beginning of a new request.</p>
 */
public class RequestContext {

    /**
     * Thread-local variable to store the user ID for the current request.
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RequestContext() {}

    /**
     * Clears the user ID from the thread-local storage at the start of a new request.
     */
    private static void start() {
        USER_ID.remove();
    }

    /**
     * Sets the user ID in the thread-local storage for the current request.
     *
     * @param userId the user ID to be associated with the current thread
     */
    private static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * Retrieves the user ID associated with the current thread.
     *
     * @return the user ID for the current thread, or {@code null} if none is set
     */
    public static Long getUserId() {
        return USER_ID.get();
    }
}

