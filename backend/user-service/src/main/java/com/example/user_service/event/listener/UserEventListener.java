package com.example.user_service.event.listener;

import com.example.user_service.event.UserEvent;
import com.example.user_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for user-related events in the application.
 *
 * <p>This class listens for {@code UserEvent} instances and triggers appropriate
 * actions based on the type of event. It primarily handles registration and password
 * reset events, and uses the {@code EmailService} to send notification emails to users.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Component} - Marks this class as a Spring component, enabling it to be detected and managed by the Spring container.</li>
 *   <li>{@code @RequiredArgsConstructor} - Generates a constructor with required arguments for all final fields.</li>
 *   <li>{@code @EventListener} - Listens for events of type {@code UserEvent}.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     UserEventListener listener = new UserEventListener(emailService);
 *     listener.onUserEvent(userEvent);
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class UserEventListener {

    /**
     * Service responsible for sending emails related to user events.
     */
    private final EmailService emailService;

    /**
     * Handles incoming {@code UserEvent} and triggers the appropriate action based on the event type.
     *
     * <p>For REGISTRATION events, this method sends a "New Account" email to the user.
     * For RESETPASSWORD events, it sends a "Password Reset" email to the user.</p>
     *
     * @param userEvent the event that occurred, containing information about the user and event type
     */
    @EventListener
    public void onUserEvent(UserEvent userEvent) {
        switch (userEvent.getType()) {
            case REGISTRATION -> emailService.sendNewAccountEmail(userEvent.getUser().getFirstName(), userEvent.getUser().getEmail(), (String) userEvent.getData().get("key"));
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(userEvent.getUser().getFirstName(), userEvent.getUser().getEmail(), (String) userEvent.getData().get("key"));
            default -> {}
        }
    }
}

