package com.example.copilot_project.repository;

import com.example.copilot_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository Interface - Extended for Instagram Clone features
 *
 * This interface extends JpaRepository, which provides built-in CRUD (Create, Read, Update, Delete)
 * operations for the User entity.
 *
 * @Repository: Marks this interface as a Spring component for data access
 *
 * Spring Data JPA automatically creates an implementation of this interface at runtime.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    /**
     * Case-insensitive email lookup (helps when users enter mixed-case email)
     */
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsername(String username);

    /**
     * Convenience lookup that tries email OR username in a single repository call.
     * Useful when callers pass the same input which could be either email or username.
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    /**
     * Search users by username, firstName, or lastName
     */
    Page<User> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String usernameQuery,
        String firstNameQuery,
        String lastNameQuery,
        Pageable pageable
    );

    /**
     * Find verified active users for explore page
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isVerified = true ORDER BY u.createdAt DESC")
    Page<User> findVerifiedUsers(Pageable pageable);

    /**
     * Find public profiles (not private)
     */
    @Query("SELECT u FROM User u WHERE u.isPrivate = false ORDER BY u.createdAt DESC")
    Page<User> findPublicProfiles(Pageable pageable);

    /**
     * Find newest users
     */
    List<User> findTop10ByOrderByCreatedAtDesc();

    /**
     * Check if user is active
     */
    boolean existsByIdAndIsActiveTrue(Long id);
}

