package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    boolean existsByFollowerIdAndFollowingIdAndIsApprovedTrue(Long followerId, Long followingId);
    Page<Follow> findByFollowingIdAndIsApprovedTrueOrderByCreatedAtDesc(Long followingId, Pageable pageable);
    Page<Follow> findByFollowerIdAndIsApprovedTrueOrderByCreatedAtDesc(Long followerId, Pageable pageable);
    long countByFollowingIdAndIsApprovedTrue(Long followingId);
    long countByFollowerIdAndIsApprovedTrue(Long followerId);
    List<Long> findFollowingIdsByFollowerIdAndIsApprovedTrue(Long followerId);
}

