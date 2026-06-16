package com.example.copilot_project.repository;

import com.example.copilot_project.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostIdOrderByImageIndex(Long postId);
    void deleteByPostId(Long postId);
}

