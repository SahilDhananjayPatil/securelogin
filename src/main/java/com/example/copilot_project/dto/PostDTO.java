package com.example.copilot_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PostDTO - Data Transfer Object for Post entity
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private Long userId;
    private String caption;
    private String location;
    private Long likesCount;
    private Long commentsCount;
    private String createdAt;
    private String updatedAt;
}

