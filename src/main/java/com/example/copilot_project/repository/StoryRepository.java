package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.expiresAt > :now AND s.isPublic = true ORDER BY s.createdAt DESC")
    Page<Story> findActivePublicStories(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.user.id IN :userIds AND s.expiresAt > :now ORDER BY s.createdAt DESC")
    List<Story> findActiveStoriesFromUsers(@Param("userIds") List<Long> userIds, @Param("now") LocalDateTime now);

    @Query("DELETE FROM Story WHERE expiresAt <= :now")
    void deleteExpiredStories(@Param("now") LocalDateTime now);

    long countByUserId(Long userId);
}

