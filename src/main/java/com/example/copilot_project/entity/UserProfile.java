package com.example.copilot_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * UserProfile Entity - Extended profile information for users
 *
 * Stores additional profile metadata and statistics for each user
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Fixed: Added correct explicit inline initialization for Lombok Builder
    @Column(nullable = false)
    @Builder.Default
    private Long followersCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long followingCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long postsCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long storiesCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long reelsCount = 0L;

    @Column(columnDefinition = "TEXT")
    private String headerImageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean allowMessagesFromAnyone = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean allowCommentsOnPosts = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}