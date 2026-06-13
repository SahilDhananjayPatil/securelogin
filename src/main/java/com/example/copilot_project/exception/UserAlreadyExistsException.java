package com.example.copilot_project.exception;

/**
 * UserAlreadyExistsException
 *
 * Custom exception thrown when trying to register with an email that already exists in the database.
 * Using custom exceptions makes it easier to handle specific error cases in the application.
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructor with error message
     * @param message Description of the error
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

