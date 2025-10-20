package vn.socialnet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Conversation;
import vn.socialnet.model.Message;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find messages in a conversation with pagination, ordered by creation time
     */
    Page<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation, Pageable pageable);

    /**
     * Find the last message in a conversation
     */
    Optional<Message> findFirstByConversationOrderByCreatedAtDesc(Conversation conversation);

    /**
     * Count unread messages in a conversation for a specific user
     */
    @Query("SELECT COUNT(m) FROM messages m " +
            "WHERE m.conversation.id = :conversationId " +
            "AND m.user.id != :userId " +
            "AND m.isRead = false")
    Long countUnreadMessages(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );

    /**
     * Mark all messages in a conversation as read for a user
     */
    @Modifying
    @Query("UPDATE messages m SET m.isRead = true " +
            "WHERE m.conversation.id = :conversationId " +
            "AND m.user.id != :userId " +
            "AND m.isRead = false")
    void markMessagesAsRead(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );
}
