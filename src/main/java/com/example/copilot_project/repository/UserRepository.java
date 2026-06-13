package com.example.copilot_project.repository;

import com.example.copilot_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository Interface
 *
 * This interface extends JpaRepository, which provides built-in CRUD (Create, Read, Update, Delete)
 * operations for the User entity.
 *
 * JpaRepository<User, Long> means:
 * - User: The entity type this repository manages
 * - Long: The type of the primary key (ID)
 *
 * @Repository: Marks this interface as a Spring component for data access
 *
 * Spring Data JPA automatically creates an implementation of this interface at runtime.
 * We can add custom query methods here that Spring Data JPA will implement automatically
 * based on the method name following a specific naming convention.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Custom method to find a user by email address
     *
     * Method naming convention:
     * - findBy: Search operation
     * - Email: The field name in the User entity
     * - Return type: Optional<User> - A wrapper that can contain a User or be empty
     *   This is better than returning null for null-safety
     *
     * Spring Data JPA automatically generates SQL:
     * SELECT * FROM users WHERE email = ?
     *
     * @param email The email address to search for
     * @return Optional containing the User if found, or empty Optional if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Custom method to check if a user exists by email
     *
     * This method is useful for validating email uniqueness during registration.
     *
     * Spring Data JPA automatically generates SQL:
     * SELECT COUNT(*) FROM users WHERE email = ?
     *
     * @param email The email address to check
     * @return true if a user with this email exists, false otherwise
     */
    boolean existsByEmail(String email);
}

