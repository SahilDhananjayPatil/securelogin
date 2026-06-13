package com.example.copilot_project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler Class
 *
 * This class handles exceptions globally across the entire application.
 * When any exception is thrown and not caught elsewhere, this class catches it
 * and handles it appropriately.
 *
 * @ControllerAdvice: Spring annotation that marks this class as a global exception handler
 *                    It intercepts exceptions from all controllers
 * @Slf4j: Generates a logger for logging exceptions
 *
 * Benefits of centralized exception handling:
 * 1. Consistent error responses across the application
 * 2. Single place to modify error handling logic
 * 3. Better separation of concerns
 * 4. Easier to add logging and monitoring
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle UserAlreadyExistsException
     *
     * This method is called when a UserAlreadyExistsException is thrown anywhere in the application.
     * It logs the error and returns a user-friendly error message.
     *
     * @ExceptionHandler(UserAlreadyExistsException.class): This method handles this specific exception type
     *
     * @param exception The exception that was thrown
     * @param model Model to pass data to the view
     * @return View name: "register" - Returns user to registration page
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException exception, Model model) {
        log.warn("UserAlreadyExistsException occurred: {}", exception.getMessage());

        // Add error message to model so it can be displayed in the view
        model.addAttribute("error", exception.getMessage());

        // Return to registration page
        return "register";
    }

    /**
     * Handle InvalidCredentialsException
     *
     * This method is called when an InvalidCredentialsException is thrown.
     * It logs the error and returns a user-friendly error message.
     *
     * @param exception The exception that was thrown
     * @param model Model to pass data to the view
     * @return View name: "login" - Returns user to login page
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentialsException(InvalidCredentialsException exception, Model model) {
        log.warn("InvalidCredentialsException occurred: {}", exception.getMessage());

        // Add error message to model so it can be displayed in the view
        model.addAttribute("error", exception.getMessage());

        // Return to login page
        return "login";
    }

    /**
     * Handle Generic Exception
     *
     * This method catches any other unexpected exceptions that aren't handled by specific handlers.
     * This is a safety net for unforeseen errors.
     *
     * @param exception The exception that was thrown
     * @param model Model to pass data to the view
     * @return View name: "login" - Returns to login page with generic error message
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception exception, Model model) {
        log.error("Unexpected exception occurred", exception);

        // Add generic error message (don't expose technical details to user)
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");

        // Return to login page
        return "login";
    }
}

