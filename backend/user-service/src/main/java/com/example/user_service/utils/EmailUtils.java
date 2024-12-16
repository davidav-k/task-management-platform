package com.example.user_service.utils;

/**
 * Utility class for generating email message content for user notifications.
 *
 * <p>This class provides static methods for creating email messages used to notify
 * users about events like account verification and password reset requests. The
 * messages include URLs that guide users to the appropriate action page.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     String message = EmailUtils.getEmailMessage("John", "https://example.com", "verificationToken");
 *     String resetMessage = EmailUtils.getResetPasswordMessage("John", "https://example.com", "resetToken");
 * </pre>
 */
public class EmailUtils {

    /**
     * Generates an email message for new user account verification.
     *
     * <p>This message informs the user that their account has been created and provides
     * a link to verify the account. The link contains a token that the user must click
     * to complete the verification process.</p>
     *
     * @param name  the name of the user to be addressed in the email
     * @param host  the base URL of the system that will be used to construct the verification URL
     * @param token the unique token used to verify the user's account
     * @return a formatted email message containing the user's name, instructions, and a verification URL
     */
    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
                getVerificationUrl(host, token) + "\n\n The Support Team";
    }

    /**
     * Generates an email message for a password reset request.
     *
     * <p>This message informs the user that a password reset request has been initiated for their account.
     * It provides a link that the user can click to reset their password. The link contains a token
     * that identifies the password reset request.</p>
     *
     * @param name  the name of the user to be addressed in the email
     * @param host  the base URL of the system that will be used to construct the password reset URL
     * @param token the unique token used to identify the password reset request
     * @return a formatted email message containing the user's name, instructions, and a password reset URL
     */
    public static String getResetPasswordMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour account has been change password. Please click on the link below to verify change password.\n\n" +
                getResetPasswordUrl(host, token) + "\n\n The Support Team";
    }

    /**
     * Generates the verification URL for new account verification.
     *
     * <p>The URL is constructed using the provided host and token. This URL is used in the
     * email sent to the user for account verification. The URL follows the format:
     * {@code <host>/verify/account?token=<token>}.</p>
     *
     * @param host  the base URL of the system
     * @param token the unique token used to verify the user's account
     * @return the fully constructed URL for account verification
     */
    private static String getVerificationUrl(String host, String token) {
        return host + "/verify/account?token=" + token;
    }

    /**
     * Generates the password reset URL for resetting a user's password.
     *
     * <p>The URL is constructed using the provided host and token. This URL is used in the
     * email sent to the user to reset their password. The URL follows the format:
     * {@code <host>/verify/password?token=<token>}.</p>
     *
     * @param host  the base URL of the system
     * @param token the unique token used to identify the password reset request
     * @return the fully constructed URL for password reset
     */
    private static String getResetPasswordUrl(String host, String token) {
        return host + "/verify/password?token=" + token;
    }
}


