package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.socialnet.dto.request.SendMessageRequest;
import vn.socialnet.dto.response.ConversationResponse;
import vn.socialnet.dto.response.MessageResponse;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.ParticipantInfo;
import vn.socialnet.enums.ConversationType;
import vn.socialnet.enums.MediaType;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.*;
import vn.socialnet.repository.ConversationParticipantsRepository;
import vn.socialnet.repository.ConversationRepository;
import vn.socialnet.repository.MessageRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.ChatService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ConversationParticipantsRepository participantsRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    @Override
    public List<ConversationResponse> getUserConversations(Long userId) {
        List<Conversation> conversations = conversationRepository.findUserConversations(userId);

        return conversations.stream()
                .map(conv -> mapToConversationResponse(conv, userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConversationResponse startOrGetConversation(Long user1Id, Long user2Id) {
        // Check if conversation already exists
        var existingConv = conversationRepository.findDirectConversation(user1Id, user2Id);
        if (existingConv.isPresent()) {
            return mapToConversationResponse(existingConv.get(), user1Id, null);
        }

        // Create new conversation
        User user1 = getUserById(user1Id);
        User user2 = getUserById(user2Id);

        Conversation conversation = Conversation.builder()
                .type(ConversationType.DIRECT)
                .build();

        // Save conversation first without participants
        conversation = conversationRepository.save(conversation);

        // Add participants after conversation is persisted
        ConversationParticipants participant1 = ConversationParticipants.builder()
                .id(new ConversationParticipantsId(user1Id, conversation.getId()))
                .userJoined(user1)
                .conversation(conversation)
                .joinedAt(LocalDateTime.now())
                .build();

        ConversationParticipants participant2 = ConversationParticipants.builder()
                .id(new ConversationParticipantsId(user2Id, conversation.getId()))
                .userJoined(user2)
                .conversation(conversation)
                .joinedAt(LocalDateTime.now())
                .build();

        // Save participants separately
        participantsRepository.save(participant1);
        participantsRepository.save(participant2);

        List<ConversationParticipants> participantsList = new ArrayList<>();
        participantsList.add(participant1);
        participantsList.add(participant2);

        log.info("Created new conversation {} between users {} and {}",
                conversation.getId(), user1Id, user2Id);

        // Pass participants explicitly to avoid Hibernate issues
        return mapToConversationResponse(conversation, user1Id, participantsList);
    }

    @Override
    public PageResponse<List<MessageResponse>> getMessages(
            Long conversationId, Long userId, int page, int size) {

        // Verify user is participant
        if (!participantsRepository.isUserParticipant(conversationId, userId)) {
            throw new AccessDeniedException("You are not a participant in this conversation");
        }

        Conversation conversation = getConversationById(conversationId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageRepository.findByConversationOrderByCreatedAtAsc(
                conversation, pageable);

        List<MessageResponse> messageResponses = messagePage.getContent().stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());

        return PageResponse.<List<MessageResponse>>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(messagePage.getTotalPages())
                .items(messageResponses)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(
            Long conversationId, Long userId, SendMessageRequest request) {

        // Verify user is participant
        if (!participantsRepository.isUserParticipant(conversationId, userId)) {
            throw new AccessDeniedException("You are not a participant in this conversation");
        }

        Conversation conversation = getConversationById(conversationId);
        User user = getUserById(userId);

        Message message = Message.builder()
                .conversation(conversation)
                .user(user)
                .message(request.getMessage())
                .isRead(false)
                .mediaType(MediaType.TEXT)
                .build();

        // Handle media upload if present
        if (request.getMedia() != null && !request.getMedia().isEmpty()) {
            try {
                String mediaUrl = s3Service.uploadFile(request.getMedia());
                message.setMediaUrl(mediaUrl);
                message.setMediaType(determineMediaType(request.getMedia().getContentType()));
            } catch (IOException e) {
                log.error("Failed to upload message media", e);
                throw new RuntimeException("Failed to upload media file");
            }
        }

        message = messageRepository.save(message);

        // Update conversation's updatedAt timestamp
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        log.info("Message sent in conversation {} by user {}", conversationId, userId);

        MessageResponse response = mapToMessageResponse(message);
        messagingTemplate.convertAndSend("/topic/chat/" + conversationId, response);

        return response;
    }

    @Override
    @Transactional
    public void markAsRead(Long conversationId, Long userId) {
        // Verify user is participant
        if (!participantsRepository.isUserParticipant(conversationId, userId)) {
            throw new AccessDeniedException("You are not a participant in this conversation");
        }

        messageRepository.markMessagesAsRead(conversationId, userId);
        log.info("Marked messages as read in conversation {} for user {}", conversationId, userId);
    }

    /**
     * Helper method to map Conversation entity to ConversationResponse DTO
     */
    private ConversationResponse mapToConversationResponse(Conversation conversation, Long currentUserId) {
        return mapToConversationResponse(conversation, currentUserId, null);
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation, Long currentUserId, List<ConversationParticipants> providedParticipants) {
        // Get participants
        List<ConversationParticipants> participantsSource;
        if (providedParticipants != null) {
            participantsSource = providedParticipants;
        } else {
            participantsSource = new ArrayList<>(conversation.getParticipants());
        }

        List<ParticipantInfo> participants = participantsSource.stream()
                .map(cp -> ParticipantInfo.builder()
                        .userId(cp.getUserJoined().getId())
                        .userName(cp.getUserJoined().getName())
                        .userAvatar(cp.getUserJoined().getAvatar())
                        .build())
                .collect(Collectors.toList());

        // Get last message
        MessageResponse lastMessage = messageRepository
                .findFirstByConversationOrderByCreatedAtDesc(conversation)
                .map(this::mapToMessageResponse)
                .orElse(null);

        // Get unread count
        Long unreadCount = messageRepository.countByConversationIdAndUserIdNotAndIsReadFalse(conversation.getId(), currentUserId, false);

        return ConversationResponse.builder()
                .id(conversation.getId())
                .type(conversation.getType())
                .participants(participants)
                .lastMessage(lastMessage)
                .unreadCount(unreadCount)
                .createdAt(conversation.getCreatedAt())
                .build();
    }

    /**
     * Helper method to map Message entity to MessageResponse DTO
     */
    private MessageResponse mapToMessageResponse(Message message) {
        User user = message.getUser();

        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .userId(user.getId())
                .userName(user.getName())
                .userAvatar(user.getAvatar())
                .message(message.getMessage())
                .mediaUrl(message.getMediaUrl())
                .mediaType(message.getMediaType())
                .isRead(message.getIsRead())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Conversation getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
    }

    private MediaType determineMediaType(String contentType) {
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return vn.socialnet.enums.MediaType.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return vn.socialnet.enums.MediaType.VIDEO;
            }
        }
        return vn.socialnet.enums.MediaType.IMAGE; // default
    }
}
