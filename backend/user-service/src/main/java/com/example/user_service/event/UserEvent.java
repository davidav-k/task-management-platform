package com.example.user_service.event;

import com.example.user_service.entity.UserEntity;
import com.example.user_service.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
/**
 * Represents an event related to a specific user and event type in the system.
 *
 * <p>This class holds information about the user, the type of event, and additional
 * data related to the event. It can be used to track or process specific user actions
 * within the application.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     UserEvent userEvent = new UserEvent(userEntity, EventType.REGISTRATION, dataMap);
 * </pre>
 */
@Getter
@Setter
@AllArgsConstructor
public class UserEvent {

    private UserEntity user;

    /**
     * The type of event that occurred.
     *
     * <p>This field specifies the type of event (e.g., registration, password reset) and is of type EventType.</p>
     */
    private EventType type;

    /**
     * Additional data related to the event.
     *
     * <p>This field stores supplementary information that may be required for event processing, such as
     * metadata or specific parameters related to the event (in this case - key).</p>
     */
    private Map<?,?> data;
}
