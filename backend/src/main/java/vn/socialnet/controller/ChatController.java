package vn.socialnet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.socialnet.dto.request.SendMessageRequest;
import vn.socialnet.dto.response.ConversationResponse;
import vn.socialnet.dto.response.MessageResponse;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.model.User;
import vn.socialnet.service.ChatService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * GET /chats - Get all conversations for the current user
     */
    @GetMapping
    public ResponseData<List<ConversationResponse>> getChats(@AuthenticationPrincipal User user) {
        log.info("Get conversations for user {}", user.getId());
        List<ConversationResponse> conversations = chatService.getUserConversations(user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Conversations retrieved successfully", conversations);
    }

    /**
     * POST /chats/start/{userId} - Start or get existing conversation with another user
     */
    @PostMapping("/start/{userId}")
    public ResponseData<ConversationResponse> startChat(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        log.info("Start conversation between user {} and user {}", user.getId(), userId);
        ConversationResponse conversation = chatService.startOrGetConversation(user.getId(), userId);
        return new ResponseData<>(HttpStatus.OK.value(), "Conversation started", conversation);
    }

    /**
     * GET /chats/{chatId}/messages - Get messages in a conversation
     */
    @GetMapping("/{chatId}/messages")
    public ResponseData<PageResponse<List<MessageResponse>>> getMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal User user) {
        log.info("Get messages in conversation {} for user {}", chatId, user.getId());
        PageResponse<List<MessageResponse>> messages = chatService.getMessages(chatId, user.getId(), page, size);
        return new ResponseData<>(HttpStatus.OK.value(), "Messages retrieved successfully", messages);
    }

    /**
     * POST /chats/{chatId}/messages - Send a message (REST fallback)
     */
    @PostMapping(value = "/{chatId}/messages", consumes = {"multipart/form-data"})
    public ResponseData<MessageResponse> sendMessage(
            @PathVariable Long chatId,
            @ModelAttribute SendMessageRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Send message in conversation {} by user {}", chatId, user.getId());
        MessageResponse message = chatService.sendMessage(chatId, user.getId(), request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Message sent successfully", message);
    }

    /**
     * PUT /chats/{chatId}/read - Mark all messages in a conversation as read
     */
    @PutMapping("/{chatId}/read")
    public ResponseData<Void> markAsRead(
            @PathVariable Long chatId,
            @AuthenticationPrincipal User user) {
        log.info("Mark messages as read in conversation {} for user {}", chatId, user.getId());
        chatService.markAsRead(chatId, user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Messages marked as read", null);
    }
}
