package com.example.user_service.service;

/**
 * Service interface for handling email notifications for user-related events.
 *
 * <p>This interface defines the methods for sending email notifications related to user
 * actions, such as account creation and password reset requests. Implementations of
 * this interface are responsible for the actual sending of emails.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     emailService.sendNewAccountEmail("John Doe", "john.doe@example.com", "token123");
 *     emailService.sendPasswordResetEmail("John Doe", "john.doe@example.com", "resetToken456");
 * </pre>
 */
public interface EmailService {

    /**
     * Sends an email to notify a new user about their account creation.
     *
     * <p>This method generates and sends an email containing a verification link that
     * allows the user to verify their account.</p>
     *
     * @param name the name of the user
     * @param email the email address of the recipient
     * @param key the verification token used in the link
     */
    void sendNewAccountEmail(String name, String email, String key);

    /**
     * Sends an email to notify a user about a password reset request.
     *
     * <p>This method generates and sends an email containing a password reset link that
     * allows the user to reset their password.</p>
     *
     * @param name the name of the user
     * @param email the email address of the recipient
     * @param token the password reset token used in the link
     */
    void sendPasswordResetEmail(String name, String email, String token);
}

