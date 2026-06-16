package com.example.copilot_project.service;

import com.example.copilot_project.entity.Post;
import com.example.copilot_project.entity.PostImage;
import com.example.copilot_project.entity.User;
import com.example.copilot_project.exception.ResourceNotFoundException;
import com.example.copilot_project.exception.UnauthorizedException;
import com.example.copilot_project.repository.PostImageRepository;
import com.example.copilot_project.repository.PostRepository;
import com.example.copilot_project.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


/**
 * PostService - Handles all post-related business logic
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FollowRepository followRepository;
    private final UserService userService;

    /**
     * Create a new post
     */
    @Transactional
    public Post createPost(Long userId, String caption, String location) {
        User user = userService.findById(userId);

        Post post = Post.builder()
            .user(user)
            .caption(caption)
            .location(location)
            .isPublic(true)
            .allowComments(true)
            .likesCount(0L)
            .commentsCount(0L)
            .build();

        log.info("Post created by user: {}", userId);
        return postRepository.save(post);
    }

    /**
     * Add image to post
     */
    @Transactional
    public void addPostImage(Long postId, String imageUrl, String imagePath, Integer imageIndex) {
        Post post = getPostById(postId);

        PostImage image = PostImage.builder()
            .post(post)
            .imageUrl(imageUrl)
            .imagePath(imagePath)
            .imageIndex(imageIndex)
            .build();

        postImageRepository.save(image);
        log.info("Image added to post: {}", postId);
    }

    /**
     * Get post by ID
     */
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
    }

    /**
     * Get posts for user
     */
    public Page<Post> getUserPosts(Long userId, Pageable pageable) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Get feed for logged-in user (posts from followed users)
     */
    public Page<Post> getFeed(Long userId, Pageable pageable) {
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerIdAndIsApprovedTrue(userId);
        followingIds.add(userId); // Include own posts

        if (followingIds.isEmpty()) {
            // If no followers, return public posts
            return postRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageable);
        }

        return postRepository.findFollowingFeed(followingIds, pageable);
    }

    /**
     * Get explore feed (public posts)
     */
    public Page<Post> getExploreFeed(Pageable pageable) {
        return postRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageable);
    }

    /**
     * Get trending posts
     */
    public Page<Post> getTrendingPosts(Pageable pageable) {
        return postRepository.findTrendingPosts(pageable);
    }

    /**
     * Search posts by hashtag
     */
    public Page<Post> searchByHashtag(String hashtag, Pageable pageable) {
        return postRepository.findByHashtag(hashtag, pageable);
    }

    /**
     * Update post caption
     */
    @Transactional
    public Post updatePost(Long postId, Long userId, String caption) {
        Post post = getPostById(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You cannot edit this post");
        }

        post.setCaption(caption);
        post.setUpdatedAt(LocalDateTime.now());

        log.info("Post updated: {}", postId);
        return postRepository.save(post);
    }

    /**
     * Delete post
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You cannot delete this post");
        }

        postImageRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
        log.info("Post deleted: {}", postId);
    }

    /**
     * Toggle post comment permissions
     */
    @Transactional
    public void toggleComments(Long postId, Long userId) {
        Post post = getPostById(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You cannot modify this post");
        }

        post.setAllowComments(!post.getAllowComments());
        postRepository.save(post);
        log.info("Comment settings toggled for post: {}", postId);
    }

    /**
     * Increment likes count
     */
    @Transactional
    public void incrementLikesCount(Long postId) {
        Post post = getPostById(postId);
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    /**
     * Decrement likes count
     */
    @Transactional
    public void decrementLikesCount(Long postId) {
        Post post = getPostById(postId);
        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);
    }

    /**
     * Increment comments count
     */
    @Transactional
    public void incrementCommentsCount(Long postId) {
        Post post = getPostById(postId);
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);
    }

    /**
     * Decrement comments count
     */
    @Transactional
    public void decrementCommentsCount(Long postId) {
        Post post = getPostById(postId);
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);
    }

    /**
     * Get post images
     */
    public List<PostImage> getPostImages(Long postId) {
        return postImageRepository.findByPostIdOrderByImageIndex(postId);
    }
}

