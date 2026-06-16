package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * PostRepository - Data access for Post entity
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts for a specific user
     */
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find public posts ordered by creation date
     */
    Page<Post> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find posts from followed users (feed)
     */
    @Query("SELECT p FROM Post p WHERE p.user.id IN :followingIds AND p.isPublic = true ORDER BY p.createdAt DESC")
    Page<Post> findFollowingFeed(@Param("followingIds") List<Long> followingIds, Pageable pageable);

    /**
     * Get trending posts by likes count
     */
    @Query("SELECT p FROM Post p WHERE p.isPublic = true ORDER BY p.likesCount DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(Pageable pageable);

    /**
     * Find posts by hashtag
     */
    @Query(value = "SELECT DISTINCT p.* FROM posts p WHERE p.caption LIKE CONCAT('%', :hashtag, '%') AND p.is_public = true ORDER BY p.created_at DESC",
           nativeQuery = true)
    Page<Post> findByHashtag(@Param("hashtag") String hashtag, Pageable pageable);

    /**
     * Find posts by location
     */
    Page<Post> findByLocationIgnoreCaseAndIsPublicTrue(String location, Pageable pageable);

    long countByUserId(Long userId);
}

