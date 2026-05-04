package com.example.nutriplanner.mapper;

import com.example.nutriplanner.dto.MessageDTO;
import com.example.nutriplanner.model.Message;

public class MessageMapper {

    public static MessageDTO toDto(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getName());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getName());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}