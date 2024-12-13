package com.example.user_service.service.impl;

import com.example.user_service.exception.ApiException;
import com.example.user_service.service.EmailService;
import com.example.user_service.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for sending email notifications for user-related events.
 *
 * <p>This class implements the EmailService interface and provides methods for sending
 * email notifications to users for events like account registration and password resets.
 * The emails are sent asynchronously using the Spring @Async annotation.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    /**
     * Subject line for new user account verification emails.
     */
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New user account verification";

    /**
     * Subject line for password reset request emails.
     */
    private static final String PASSWORD_RESET_REQUEST = "Password reset request";

    private final JavaMailSender sender;

    /**
     * The host URL used for generating email links (e.g., verification links).
     */
    @Value("${spring.mail.verify.host}")
    private String host;

    /**
     * The sender email address used in the From field of the email.
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Sends an email to notify a new user about their account creation.
     *
     * <p>This method sends an email containing a verification link that allows the user
     * to verify their account. It runs asynchronously using the @Async annotation.</p>
     *
     * @param name  the name of the user
     * @param email the email address of the recipient
     * @param token the verification token used in the link
     */
    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setTo(fromEmail);
            message.setTo(email);
            message.setText(EmailUtils.getEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }

    /**
     * Sends an email to notify a user about a password reset request.
     *
     * <p>This method sends an email containing a password reset link that allows the user
     * to reset their password. It runs asynchronously using the @Async annotation.</p>
     *
     * @param name  the name of the user
     * @param email the email address of the recipient
     * @param token the password reset token used in the link
     */
    @Override
    @Async
    public void sendPasswordResetEmail(String name, String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setTo(fromEmail);
            message.setTo(email);
            message.setText(EmailUtils.getResetPasswordMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }
}

