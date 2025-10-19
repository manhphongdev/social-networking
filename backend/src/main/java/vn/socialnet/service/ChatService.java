package vn.socialnet.service;

import vn.socialnet.dto.request.SendMessageRequest;
import vn.socialnet.dto.response.ConversationResponse;
import vn.socialnet.dto.response.MessageResponse;
import vn.socialnet.dto.response.PageResponse;

import java.util.List;

public interface ChatService {

    /**
     * Get all conversations for the current user
     */
    List<ConversationResponse> getUserConversations(Long userId);

    /**
     * Start a new conversation or get existing direct conversation between two users
     */
    ConversationResponse startOrGetConversation(Long user1Id, Long user2Id);

    /**
     * Get messages in a conversation with pagination
     */
    PageResponse<List<MessageResponse>> getMessages(Long conversationId, Long userId, int page, int size);

    /**
     * Send a message in a conversation (REST fallback)
     */
    MessageResponse sendMessage(Long conversationId, Long userId, SendMessageRequest request);

    /**
     * Mark all messages in a conversation as read for the current user
     */
    void markAsRead(Long conversationId, Long userId);
}
