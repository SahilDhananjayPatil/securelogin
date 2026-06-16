package com.example.copilot_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SecurityConfig Class
 *
 * This configuration class sets up security beans for the application.
 * It defines how passwords are encoded using BCrypt.
 *
 * IMPORTANT: Spring Security is temporarily DISABLED to allow unrestricted access.
 * The SecurityFilterChain bean permits ALL requests and disables CSRF protection.
 * In a production application, you would configure proper authentication and authorization.
 *
 * @Configuration: Marks this class as a Spring configuration class
 *                 Methods with @Bean annotations are registered as Spring beans
 * @EnableWebSecurity: Enables custom Spring Security configuration (overrides default)
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * TEMPORARY: SecurityFilterChain configuration
     *
     * This bean disables Spring Security's default behavior:
     * - Allows ALL requests to pass through without authentication
     * - Disables CSRF protection
     * - Permits access to Swagger UI and all other endpoints
     *
     * To restore security later, remove or modify this bean with proper authentication rules.
     *
     * @param http The HttpSecurity object to configure
     * @return Configured SecurityFilterChain that permits all requests
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz ->
                authz.anyRequest().permitAll()  // Permit ALL requests without authentication
            )
            .csrf(csrf -> csrf.disable())  // Disable CSRF protection
            .headers(headers -> headers.disable());  // Disable security headers if needed

        return http.build();
    }

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

