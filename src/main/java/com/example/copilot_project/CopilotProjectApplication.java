package com.example.copilot_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CopilotProjectApplication
 *
 * Main entry point for the Spring Boot application.
 *
 * @SpringBootApplication: This is a convenience annotation that combines:
 * 1. @Configuration - Marks this class as a configuration class
 * 2. @EnableAutoConfiguration - Enables Spring Boot's auto-configuration
 * 3. @ComponentScan - Scans for @Component, @Service, @Repository, @Controller classes
 *                     in this package and all sub-packages
 *
 * This annotation auto-discovers and registers all our classes:
 * - Controllers (AuthController)
 * - Services (UserService)
 * - Repositories (UserRepository)
 * - Configuration classes (SecurityConfig)
 * - Exception handlers (GlobalExceptionHandler)
 * - Entities (User)
 */
@SpringBootApplication
public class CopilotProjectApplication {

	/**
	 * Main method - Entry point for the application
	 *
	 * When you run the application:
	 * 1. SpringApplication.run() initializes the Spring container
	 * 2. Loads configuration from application.properties
	 * 3. Starts an embedded Tomcat server on port 8080
	 * 4. Scans and registers all Spring components
	 * 5. Application is ready to receive HTTP requests
	 *
	 * @param args Command-line arguments (not used in this application)
	 */
	public static void main(String[] args) {
		SpringApplication.run(CopilotProjectApplication.class, args);
	}

}
