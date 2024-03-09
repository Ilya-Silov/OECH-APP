package com.example.oechapp.Entity;

import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String senderName;
    private String recipientName;
    private String content;
    private LocalDateTime timestamp;
    private MessageStatus status;
}
