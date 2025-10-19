package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.ConversationParticipants;

@Repository
public interface ConversationParticipantsRepository extends JpaRepository<ConversationParticipants, Long> {

    /**
     * Check if a user is a participant in a conversation
     */
    @Query("SELECT CASE WHEN COUNT(cp) > 0 THEN true ELSE false END " +
            "FROM conversation_participants cp " +
            "WHERE cp.conversation.id = :conversationId " +
            "AND cp.userJoined.id = :userId")
    boolean isUserParticipant(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );

}
