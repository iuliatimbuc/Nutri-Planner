package com.example.nutriplanner.service.impl;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.Message;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.MessageRepository;
import com.example.nutriplanner.repository.UserRepository;
import com.example.nutriplanner.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImp implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageServiceImp(MessageRepository messageRepository,
                             UserRepository userRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Message sendMessage(Long senderId, Long receiverId, String content) throws ApiExceptionResponse {
        User sender = userRepository.findById(senderId).orElse(null);
        if (sender == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No user with id " + senderId))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        User receiver = userRepository.findById(receiverId).orElse(null);
        if (receiver == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No user with id " + receiverId))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        Message saved = messageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/chat/" + receiverId, saved);

        return saved;
    }

    public List<Message> getConversation(Long userId1, Long userId2) throws ApiExceptionResponse {
        User user1 = userRepository.findById(userId1).orElse(null);
        if (user1 == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No user with id " + userId1))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        User user2 = userRepository.findById(userId2).orElse(null);
        if (user2 == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No user with id " + userId2))
                    .message("Entity not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return messageRepository
                .findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(user1, user2, user1, user2);
    }
}