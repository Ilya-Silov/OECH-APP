package com.example.oechapp.messanger.controller;

import com.example.oechapp.messanger.entity.ChatMessage;
import com.example.oechapp.messanger.entity.dto.ChatMessageMapper;
import com.example.oechapp.messanger.entity.dto.ChatMessageRequest;
import com.example.oechapp.messanger.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api//messages")
public class MessageController {
    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper chatMessageMapper;

    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessageRequest message) {

        return new ResponseEntity<>(chatMessageService.save(chatMessageMapper.mapChatMessageRequestToChatMessage(message)), HttpStatus.OK);
    }

    @GetMapping("/{}")
    public List<ChatMessage> getMessages() {
        ret
    }
}