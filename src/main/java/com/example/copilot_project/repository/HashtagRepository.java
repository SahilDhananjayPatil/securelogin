package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByTagIgnoreCase(String tag);

    @Query("SELECT h FROM Hashtag h ORDER BY h.usageCount DESC LIMIT 10")
    Page<Hashtag> findTrendingHashtags(Pageable pageable);

    Page<Hashtag> findByTagContainingIgnoreCaseOrderByUsageCountDesc(String query, Pageable pageable);
}

