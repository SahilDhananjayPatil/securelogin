package com.example.copilot_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDTO - Data Transfer Object for User entity
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String website;
    private String profilePictureUrl;
    private Boolean isPrivate;
    private Boolean isVerified;
    private Long followersCount;
    private Long followingCount;
    private Long postsCount;
}

