package com.example.oechapp.messanger.controller;

import com.example.oechapp.Entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

//    private final SimpMessagingTemplate messagingTemplate;
//    private final ChatMessageService chatMessageService;
//    private final ChatRoomService chatRoomService;
//
//    @MessageMapping("/chat")
//    public void processMessage(@Payload ChatMessage chatMessage) {
//        var chatId = chatRoomService
//                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
//        chatMessage.setChatId(chatId.get());
//
//        ChatMessage saved = chatMessageService.save(chatMessage);
//
//        messagingTemplate.convertAndSendToUser(
//                chatMessage.getRecipientId(),"/queue/messages",
//                new ChatNotification(
//                        saved.getId(),
//                        saved.getSenderId(),
//                        saved.getSenderName()));
//    }
}