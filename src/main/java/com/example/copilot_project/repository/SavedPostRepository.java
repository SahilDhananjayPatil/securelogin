package com.example.copilot_project.repository;

import com.example.copilot_project.entity.SavedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    Optional<SavedPost> findByUserIdAndPostId(Long userId, Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Page<SavedPost> findByUserIdOrderBySavedAtDesc(Long userId, Pageable pageable);

    Page<SavedPost> findByUserIdAndCollectionNameOrderBySavedAtDesc(Long userId, String collectionName, Pageable pageable);

    long countByUserId(Long userId);
}

