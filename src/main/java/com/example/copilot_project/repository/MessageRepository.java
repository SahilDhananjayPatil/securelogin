package com.example.copilot_project.repository;

import com.example.copilot_project.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Find conversation between two users
     */
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.recipient.id = :userId2) OR (m.sender.id = :userId2 AND m.recipient.id = :userId1) ORDER BY m.createdAt DESC")
    Page<Message> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2, Pageable pageable);

    /**
     * Find unread messages for a user
     */
    Page<Message> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    /**
     * Count unread messages
     */
    long countByRecipientIdAndIsReadFalse(Long recipientId);
}

