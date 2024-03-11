package com.example.oechapp.messanger.controller;

import com.example.oechapp.messanger.entity.ChatMessage;
import com.example.oechapp.messanger.entity.dto.ChatMessageMapper;
import com.example.oechapp.messanger.entity.dto.ChatMessageRequest;
import com.example.oechapp.messanger.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {
    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper chatMessageMapper;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessageRequest message) {
        ChatMessage msg = chatMessageMapper.mapChatMessageRequestToChatMessage(message);
        return chatMessageService.save(msg);
    }
}