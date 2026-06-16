package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Reel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReelRepository extends JpaRepository<Reel, Long> {
    Page<Reel> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT r FROM Reel r WHERE r.isPublic = true ORDER BY r.createdAt DESC")
    Page<Reel> findPublicReelsOrderByLatest(Pageable pageable);

    @Query("SELECT r FROM Reel r WHERE r.isPublic = true ORDER BY r.likesCount DESC, r.createdAt DESC")
    Page<Reel> findTrendingReels(Pageable pageable);

    long countByUserId(Long userId);
}

