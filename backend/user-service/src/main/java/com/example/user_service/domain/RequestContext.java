package com.example.user_service.domain;


/**
 * Manages request-scoped context data, specifically the user ID, within a thread-local variable.
 *
 * <p>This class provides static methods to set, get, and clear the user ID for the current thread.
 * It is useful for associating user-specific information with a thread during the execution
 * of a request in a multi-threaded application.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code ThreadLocal} - Used to store user-specific data in the current thread's context.</li>
 * </ul>
 */
public class RequestContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private RequestContext() {}

    private static void start() {
        USER_ID.remove();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }
}


