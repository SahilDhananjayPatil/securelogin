package com.example.copilot_project.controller;

import com.example.copilot_project.entity.User;
import com.example.copilot_project.exception.InvalidCredentialsException;
import com.example.copilot_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight REST controller that exposes a minimal API under /api/auth/internal
 * for internal/legacy usage. This avoids conflicting mappings with the primary
 * API controller and keeps a small example surface for internal calls.
 */
@RestController
@RequestMapping("/api/auth/internal")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final UserService userService;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Collections.singletonMap("message", "pong"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        log.info("API login attempt for email: {}", request.getEmail());

        try {
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());

            // store minimal session info
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());

            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "success");
            resp.put("userId", user.getId());
            resp.put("userName", user.getName());
            resp.put("userEmail", user.getEmail());

            return ResponseEntity.ok(resp);

        } catch (InvalidCredentialsException e) {
            log.warn("API login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            log.error("API login unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Internal server error"));
        }
    }

    // Simple DTO for login request payload
    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
