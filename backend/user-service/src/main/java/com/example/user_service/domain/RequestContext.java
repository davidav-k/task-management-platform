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

    /**
     * Thread-local variable to store the user ID for the current thread.
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RequestContext() {}

    /**
     * Clears the user ID from the thread-local storage.
     *
     * <p>This method is called at the start of a request to ensure no residual user ID
     * is left in the thread-local variable from a previous request.</p>
     */
    private static void start() {
        USER_ID.remove();
    }

    /**
     * Sets the user ID in the thread-local storage for the current thread.
     *
     * @param userId the user ID to be associated with the current thread
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * Retrieves the user ID associated with the current thread.
     *
     * @return the user ID for the current thread, or {@code null} if no user ID is set
     */
    public static Long getUserId() {
        return USER_ID.get();
    }
}


