package com.example.nutriplanner.service;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId, Long receiverId, String content) throws ApiExceptionResponse;
    List<Message> getConversation(Long userId1, Long userId2) throws ApiExceptionResponse;
}