package com.example.copilot_project.repository;

import com.example.copilot_project.entity.ReelComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReelCommentRepository extends JpaRepository<ReelComment, Long> {
    Page<ReelComment> findByReelIdOrderByCreatedAtDesc(Long reelId, Pageable pageable);
    long countByReelId(Long reelId);
}

