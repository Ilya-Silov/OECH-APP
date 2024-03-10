package com.example.oechapp.messanger.entity.dto;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ChatMessageRequest {
    private String text;
    private String author;
    private LocalDateTime createDate;
    private Long chatId;
}
