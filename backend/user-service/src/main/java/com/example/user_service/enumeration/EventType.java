package com.example.user_service.enumeration;

/**
 * Enum representing different types of events that can occur in the system.
 *
 * <p>This enum defines the types of events that may require user action or system notification,
 * such as user registration or password reset. It can be used to categorize and identify
 * the type of event being processed.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     EventType eventType = EventType.REGISTRATION;
 * </pre>
 */
public enum EventType {
    REGISTRATION,
    RESETPASSWORD
}
