package com.example.copilot_project.repository;

import com.example.copilot_project.entity.ReelLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {
    Optional<ReelLike> findByUserIdAndReelId(Long userId, Long reelId);
    boolean existsByUserIdAndReelId(Long userId, Long reelId);
    long countByReelId(Long reelId);
    void deleteByUserIdAndReelId(Long userId, Long reelId);
}

