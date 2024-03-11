package com.example.oechapp.messanger.controller;

import com.example.oechapp.Entity.User;
import com.example.oechapp.Security.UserDetailsImpl;
import com.example.oechapp.Service.UserService;
import com.example.oechapp.messanger.entity.ChatMessage;
import com.example.oechapp.messanger.entity.dto.ChatMessageMapper;
import com.example.oechapp.messanger.entity.dto.ChatMessageRequest;
import com.example.oechapp.messanger.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatMessageService;
    private final ChatMessageMapper chatMessageMapper;
    private final UserService userService;

    @PostMapping
    public ChatMessage sendMessage(@RequestBody ChatMessageRequest message) {
        return chatMessageService.save(chatMessageMapper.mapChatMessageRequestToChatMessage(message));
    }

    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getMessageHistory(@PathVariable Long senderId, @PathVariable Long recipientId, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();
        if (!user.getId().equals(senderId) && !user.getId().equals(recipientId))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(chatMessageService.getMessageHistory(senderId, recipientId), HttpStatus.OK);
    }

    //TODO:Доделаать
    @GetMapping("/chats/avaliable")
    public ResponseEntity<?> getChats()
    {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}