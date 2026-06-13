package com.example.copilot_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SecurityConfig Class
 *
 * This configuration class sets up security beans for the application.
 * It defines how passwords are encoded using BCrypt.
 *
 * Note: We're using a simplified configuration without Spring Security's full filter chain
 * because we're handling authentication manually in the service layer.
 * In a production application, you would use Spring Security's more advanced features.
 *
 * @Configuration: Marks this class as a Spring configuration class
 *                 Methods with @Bean annotations are registered as Spring beans
 */

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * PasswordEncoder Bean
     *
     * This method creates a PasswordEncoder bean that uses BCrypt algorithm.
     * BCrypt is a strong, secure password hashing algorithm that:
     * 1. Automatically generates a salt (random value) for each password
     * 2. Applies the salt before hashing (makes rainbow table attacks ineffective)
     * 3. Uses slow algorithmic rounds (currently 10) to make brute force attacks slow
     * 4. Includes the salt in the hash output so verification works without storing it separately
     *
     * Strength of 10 means the algorithm will apply 2^10 = 1024 rounds of hashing
     * This makes password verification slower (more secure) than with strength 4
     *
     * Usage:
     * - passwordEncoder.encode(plainPassword): Encodes a plain-text password to hash
     * - passwordEncoder.matches(plainPassword, encodedPassword): Verifies a password
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // strength = 10 provides good balance between security and performance
        // Range is 4-31, where higher numbers are more secure but slower
        return new BCryptPasswordEncoder(10);
    }
}

