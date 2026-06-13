package com.example.copilot_project.controller;

import com.example.copilot_project.entity.User;
import com.example.copilot_project.exception.InvalidCredentialsException;
import com.example.copilot_project.exception.UserAlreadyExistsException;
import com.example.copilot_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AuthController Class
 *
 * This controller handles all authentication-related HTTP requests
 * (registration, login, logout, displaying pages).
 *
 * @Controller: Marks this class as a Spring MVC controller
 *             Returns view names (Thymeleaf templates) instead of JSON
 * @RequiredArgsConstructor: Generates constructor that injects the UserService
 * @Slf4j: Generates a logger for logging important events
 *
 * URL Mappings:
 * - GET /register: Display registration form
 * - POST /register: Handle registration form submission
 * - GET /login: Display login form
 * - POST /login: Handle login form submission
 * - GET /home: Display home page (secured, requires login)
 * - GET /logout: Handle logout
 * - GET /: Redirect to login or home depending on session
 */

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    /**
     * UserService - Contains business logic for user registration and login
     * Injected via constructor (best practice)
     */
    private final UserService userService;

    // ===== REGISTRATION ENDPOINTS =====

    /**
     * Display registration page
     *
     * This method handles GET requests to /register
     * It displays the registration form where users can enter name, email, and password
     *
     * @return The name of the Thymeleaf template to render: "register"
     *
     * Spring will look for: src/main/resources/templates/register.html
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        log.debug("Displaying registration page");
        return "register"; // Returns register.html view
    }

    /**
     * Handle registration form submission
     *
     * This method handles POST requests to /register with form data
     * It validates the email doesn't exist and saves the user
     *
     * How it works:
     * 1. Receives form parameters (name, email, password, confirmPassword)
     * 2. Validates passwords match
     * 3. Creates a new User object
     * 4. Calls userService.registerUser() to save it
     * 5. Redirects to login page with success message
     *
     * @param name User's full name from form
     * @param email User's email from form
     * @param password User's password from form
     * @param confirmPassword Password confirmation from form
     * @param model Used to pass data to the view
     * @param redirectAttributes Used to pass messages during redirects (flash attributes)
     * @return Redirect URL:
     *         - "redirect:/login" if registration successful
     *         - "register" if validation fails
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        log.info("Processing registration for email: {}", email);

        // Step 1: Validate passwords match
        if (!password.equals(confirmPassword)) {
            log.warn("Registration failed: Passwords do not match for email: {}", email);
            model.addAttribute("error", "Passwords do not match!");
            return "register"; // Return to registration page with error
        }

        // Step 2: Validate password length (at least 6 characters)
        if (password.length() < 6) {
            log.warn("Registration failed: Password too short for email: {}", email);
            model.addAttribute("error", "Password must be at least 6 characters long!");
            return "register";
        }

        try {
            // Step 3: Create User object
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .build();

            // Step 4: Call service to register user
            // This will encode password with BCrypt and save to database
            User savedUser = userService.registerUser(user);

            log.info("User registered successfully: {}", email);

            // OPTIONAL: Auto-login the user after successful registration
            // Authenticate and store user info in session, then redirect to /home
            try {
                User authenticated = userService.authenticateUser(email, password);
                session.setAttribute("userId", authenticated.getId());
                session.setAttribute("userName", authenticated.getName());
                session.setAttribute("userEmail", authenticated.getEmail());
                log.info("User auto-logged in after registration: {}", email);
                return "redirect:/home";
            } catch (Exception authEx) {
                // If auto-login fails for any reason, fall back to redirecting to login with success message
                log.warn("Auto-login after registration failed for {}: {}", email, authEx.getMessage());
                redirectAttributes.addFlashAttribute("success",
                        "Registration successful! Please login with your credentials.");
                return "redirect:/login";
            }

        } catch (UserAlreadyExistsException e) {
            // Catch if email already exists
            log.warn("Registration failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register"; // Return to registration page with error
        } catch (Exception e) {
            // Catch any other unexpected errors
            log.error("Registration failed with unexpected error", e);
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "register";
        }
    }

    // ===== LOGIN ENDPOINTS =====

    /**
     * Display login page
     *
     * This method handles GET requests to /login
     * It displays the login form where users can enter email and password
     *
     * @return The name of the Thymeleaf template to render: "login"
     *
     * Spring will look for: src/main/resources/templates/login.html
     */
    @GetMapping("/login")
    public String showLoginForm() {
        log.debug("Displaying login page");
        return "login"; // Returns login.html view
    }

    /**
     * Handle login form submission
     *
     * This method handles POST requests to /login with form data
     * It validates credentials and creates a user session if valid
     *
     * How it works:
     * 1. Receives email and password from form
     * 2. Calls userService.authenticateUser() to validate credentials
     * 3. If valid, stores user info in HttpSession
     * 4. Redirects to /home
     *
     * HttpSession: Server-side storage that persists user data across requests
     * We store the user ID so we can retrieve user info on /home page
     * Session expires when user logs out or after timeout period
     *
     * @param email User's email from form
     * @param password User's password from form
     * @param model Used to pass data to the view
     * @param session HttpSession for storing user authentication data
     * @return Redirect URL:
     *         - "redirect:/home" if login successful
     *         - "login" if credentials invalid
     */
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        log.info("Processing login for email: {}", email);

        try {
            // Step 1: Authenticate user
            // This method will:
            // - Find user by email
            // - Compare password with BCrypt encoded password
            // - Throw exception if invalid
            User user = userService.authenticateUser(email, password);

            // Step 2: Login successful - Store user in session
            // Session is maintained by the server automatically
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());

            log.info("User logged in successfully: {}", email);

            // Step 3: Redirect to home page
            return "redirect:/home";

        } catch (InvalidCredentialsException e) {
            // Catch if credentials are invalid
            log.warn("Login failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "login"; // Return to login page with error
        } catch (Exception e) {
            // Catch any other unexpected errors
            log.error("Login failed with unexpected error", e);
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "login";
        }
    }

    // ===== HOME PAGE ENDPOINT =====

    /**
     * Display home page (protected - requires login)
     *
     * This method handles GET requests to /home
     * It displays a welcome page that's only accessible after successful login
     *
     * Security: This page checks if user is logged in by verifying session
     * If user is not logged in (no session), they get redirected to login page
     *
     * @param session HttpSession containing user authentication data
     * @param model Used to pass data to the Thymeleaf template
     * @return Redirect URL or view name:
     *         - "home" if user is logged in (session exists)
     *         - "redirect:/login" if user not logged in (no session)
     */
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        // Step 1: Check if user is logged in by checking if userId exists in session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            // User is not logged in - redirect to login page
            log.warn("Unauthorized access attempt to /home - no session");
            return "redirect:/login";
        }

        // Step 2: Retrieve user info from session
        String userName = (String) session.getAttribute("userName");
        log.info("Displaying home page for user: {}", userName);

        // Step 3: Pass user name to the view for displaying welcome message
        model.addAttribute("userName", userName);

        // Step 4: Return home view
        return "home"; // Returns home.html view
    }

    // ===== LOGOUT ENDPOINT =====

    /**
     * Handle user logout
     *
     * This method handles GET requests to /logout
     * It invalidates the user session and redirects to login page
     *
     * How it works:
     * 1. Gets the HttpSession
     * 2. Invalidates it (clears all attributes)
     * 3. Redirects to login page
     *
     * After logout, the user must login again to access /home
     *
     * @param session HttpSession to invalidate
     * @return Redirect URL to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("User logging out");

        // Invalidate session - this clears all session attributes
        // After this, the user is no longer authenticated
        session.invalidate();

        // Redirect to login page
        return "redirect:/login";
    }

    // ===== ROOT ENDPOINT =====

    /**
     * Handle requests to root URL (/)
     *
     * This method redirects the user to either:
     * - /login if not logged in
     * - /home if already logged in
     *
     * @param session HttpSession to check if user is already logged in
     * @return Redirect URL
     */
    @GetMapping("/")
    public String redirectToLoginOrHome(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            // User is already logged in
            return "redirect:/home";
        }

        // User is not logged in
        return "redirect:/login";
    }
}

