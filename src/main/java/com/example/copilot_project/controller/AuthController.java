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
 * AuthController Class - Modified for strict User Entity Constraints
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    // ===== REGISTRATION ENDPOINTS =====

    @GetMapping("/register")
    public String showRegistrationForm() {
        log.debug("Displaying registration page");
        return "register";
    }

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
            return "register";
        }

        // Step 2: Validate password length (at least 6 characters)
        if (password.length() < 6) {
            log.warn("Registration failed: Password too short for email: {}", email);
            model.addAttribute("error", "Password must be at least 6 characters long!");
            return "register";
        }

        try {
            // FIX: Email se automatic unique username nikalna (e.g., sahilpatil0211@gmail.com -> sahilpatil0211)
            String generatedUsername = email.split("@")[0];

            // Step 3: Create User object with ALL fields required by your strict User Entity
            User user = User.builder()
                    .firstName(name)
                    .lastName("")              // Blank string taaki Validation validation fail na ho
                    .username(generatedUsername) // FIX: Ab database mai username null nahi jayega
                    .email(email)
                    .password(password)
                    .isActive(true)            // Explicitly setting primitive/wrapper defaults
                    .isPrivate(false)
                    .isVerified(false)
                    .build();

            // Step 4: Call service to register user (hashes password and saves)
            User savedUser = userService.registerUser(user);
            log.info("User registered successfully: {}", email);

            // Auto-login the user after successful registration
            try {
                User authenticated = userService.authenticateUser(email, password);

                // Session updates using exact entity getters
                session.setAttribute("userId", authenticated.getId());
                session.setAttribute("userName", authenticated.getName()); // Calls the transient getName()
                session.setAttribute("userEmail", authenticated.getEmail());

                log.info("User auto-logged in after registration: {}", email);
                return "redirect:/home";
            } catch (Exception authEx) {
                log.warn("Auto-login after registration failed for {}: {}", email, authEx.getMessage());
                redirectAttributes.addFlashAttribute("success",
                        "Registration successful! Please login with your credentials.");
                return "redirect:/login";
            }

        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            log.error("Registration failed with unexpected error", e);
            model.addAttribute("error", "An error occurred during registration. Please check console logs.");
            return "register";
        }
    }

    // ===== LOGIN ENDPOINTS =====

    @GetMapping("/login")
    public String showLoginForm() {
        log.debug("Login Page is Displaying.... changes from Sahil Side");
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        log.info("Processing login for email: {}", email);

        try {
            // Step 1: Authenticate user
            User user = userService.authenticateUser(email, password);

            // Step 2: Login successful - Store user in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName()); // Uses the transient helper method safely
            session.setAttribute("userEmail", user.getEmail());

            log.info("User logged in successfully: {}", email);
            return "redirect:/home";

        } catch (InvalidCredentialsException e) {
            log.warn("Login failed: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (Exception e) {
            log.error("Login failed with unexpected error", e);
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "login";
        }
    }

    // ===== HOME PAGE ENDPOINT =====

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            log.warn("Unauthorized access attempt to /home - no session");
            return "redirect:/login";
        }

        String userName = (String) session.getAttribute("userName");
        log.info("Displaying home page for user: {}", userName);

        model.addAttribute("userName", userName);
        return "home";
    }

    // ===== PROFILE ENDPOINTS =====

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            log.warn("Unauthorized access attempt to /profile - no session");
            return "redirect:/login";
        }

        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam(name = "bio", required = false) String bio,
            @RequestParam(name = "website", required = false) String website,
            @RequestParam(name = "gender", required = false) String gender,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.warn("Unauthorized profile update attempt - no session");
            return "redirect:/login";
        }

        try {
            userService.updateProfile(userId, bio, website, gender);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            log.error("Profile update failed for user {}", userId, e);
            redirectAttributes.addFlashAttribute("error", "Could not update profile. Please try again.");
        }

        return "redirect:/profile";
    }

    // ===== LOGOUT ENDPOINT =====

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("User logging out");
        session.invalidate();
        return "redirect:/login";
    }

    // ===== ROOT ENDPOINT =====

    @GetMapping("/")
    public String redirectToLoginOrHome(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}