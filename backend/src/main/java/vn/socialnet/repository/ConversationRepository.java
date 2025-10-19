package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Conversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find all conversations for a user
     */
    @Query("SELECT DISTINCT c FROM conversations c " +
            "JOIN c.participants p " +
            "WHERE p.userJoined.id = :userId " +
            "ORDER BY c.updatedAt DESC")
    List<Conversation> findUserConversations(@Param("userId") Long userId);

    /**
     * Find existing direct conversation between two users
     */
    @Query("SELECT c FROM conversations c " +
            "JOIN c.participants p1 " +
            "JOIN c.participants p2 " +
            "WHERE p1.userJoined.id = :user1Id " +
            "AND p2.userJoined.id = :user2Id " +
            "AND c.type = 'DIRECT' " +
            "AND p1.conversation.id = p2.conversation.id")
    Optional<Conversation> findDirectConversation(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id
    );


}
