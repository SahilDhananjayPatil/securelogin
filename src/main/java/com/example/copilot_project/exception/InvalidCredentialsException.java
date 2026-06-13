package com.example.copilot_project.exception;

/**
 * InvalidCredentialsException
 *
 * Custom exception thrown when login credentials (email/password) are invalid.
 * This helps distinguish authentication failures from other types of errors.
 */
public class InvalidCredentialsException extends RuntimeException {
    /**
     * Constructor with error message
     * @param message Description of the error
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

