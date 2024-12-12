package com.example.user_service.domain;


/**
 * Utility class for managing request-scoped context data, specifically the user ID.
 */
public class RequestContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private RequestContext() {}

    private static void start() {
        USER_ID.remove();
    }

    private static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }
}

