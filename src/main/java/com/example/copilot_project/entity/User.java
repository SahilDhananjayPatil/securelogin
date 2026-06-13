package com.example.copilot_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity Class
 * 
 * This class represents a User in the database.
 * It uses JPA (Java Persistence API) annotations to map the class to a database table.
 * - @Entity: Marks this class as a JPA entity (database table)
 * - @Table(name = "users"): Specifies the table name in the database
 * - @Id: Marks the primary key field
 * - @GeneratedValue: Auto-generates the ID value
 * - Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder) 
 *   generate getters, setters, constructors, and builder pattern methods automatically
 */

@Entity
@Data // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor // Generates no-argument constructor
@AllArgsConstructor // Generates all-argument constructor
@Builder // Generates builder pattern for object creation
@Table(name = "users")
public class User {
    
    /**
     * Primary key field
     * - @Id: Marks this as primary key
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-increments the ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User's full name
     * - @NotBlank: Validation annotation - name cannot be null or empty
     * - @Column(nullable = false): Database constraint - column cannot be NULL
     */
    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;
    
    /**
     * User's email address
     * - @Email: Validates that the email format is correct
     * - @NotBlank: Email cannot be null or empty
     * - @Column(nullable = false, unique = true): 
     *   - nullable = false: Column cannot be NULL
     *   - unique = true: Email must be unique in the database (no duplicates)
     */
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    @Column(nullable = false, unique = true)
    private String email;
    
    /**
     * User's password (encrypted using BCrypt)
     * - @NotBlank: Password cannot be null or empty
     * - @Column(nullable = false): Column cannot be NULL
     * Note: Password is stored as encrypted text in the database
     */
    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false)
    private String password;
    
    /**
     * User creation timestamp
     * - @Column(updatable = false): This field cannot be updated once created
     */
    @Column(updatable = false)
    private Long createdAt;
    
    /**
     * PrePersist method - automatically called before the entity is saved to the database
     * Sets the createdAt timestamp to the current time
     */
    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
    }
}

