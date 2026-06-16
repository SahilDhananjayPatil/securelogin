package com.example.copilot_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity Class - Core user information for Instagram Clone
 * 
 * This class represents a User in the database with complete profile information,
 * authentication details, and relationships to posts, follows, and messages.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "username")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username cannot be empty")
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "First name cannot be empty")
    @Column(nullable = false, length = 100)
    private String firstName;
    
    @Column(length = 100)
    private String lastName;
    
    // Profile Information
    @Column(length = 500)
    private String bio;
    
    @Column(length = 255)
    private String website;
    
    @Column(length = 50)
    private String gender;  // MALE, FEMALE, OTHER, PREFER_NOT_SAY
    
    @Column(length = 255)
    private String profilePictureUrl;
    
    @Column(columnDefinition = "TEXT")
    private String profilePicturePath;
    
    // Account Status
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private Boolean isPrivate = false;
    
    @Column(nullable = false)
    private Boolean isVerified = false;
    
    // Timestamps
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime lastLoginAt;
    
    // Relationships
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Like> likes = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Follow> following = new HashSet<>();
    
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Follow> followers = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Story> stories = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Reel> reels = new HashSet<>();
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Message> sentMessages = new HashSet<>();
    
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Message> receivedMessages = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Notification> notifications = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Transient
    public Long getFollowersCount() {
        return (long) (followers != null ? followers.size() : 0);
    }
    
    @Transient
    public Long getFollowingCount() {
        return (long) (following != null ? following.size() : 0);
    }
    
    @Transient
    public Long getPostsCount() {
        return (long) (posts != null ? posts.size() : 0);
    }
    /**
     * Convenience getter for the user's display name.
     * If lastName is present it will return "firstName lastName",
     * otherwise just firstName. Returns empty string when firstName is null.
     */
    @Transient
    public String getName() {
        if (firstName == null) return "";
        if (lastName == null || lastName.isBlank()) return firstName;
        return firstName + " " + lastName;
    }

}
