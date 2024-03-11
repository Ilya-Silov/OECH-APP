package com.example.oechapp.messanger.service;


import com.example.oechapp.Entity.User;
import com.example.oechapp.Entity.UserRoles;
import com.example.oechapp.Service.UserService;
import com.example.oechapp.messanger.entity.ChatMessage;
import com.example.oechapp.messanger.entity.ChatRoom;
import com.example.oechapp.messanger.entity.MessageStatus;
import com.example.oechapp.messanger.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;



@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return chatMessageRepository.countMessagesBetweenUsersByStatus(
                senderId, recipientId, MessageStatus.RECEIVED).size();
    }
    public List<ChatMessage> getMessageHistory(User user1, User user2) {
        return getMessageHistory(user1.getId(), user2.getId());
    }
    public List<ChatMessage> getMessageHistory(long user1, long user2) {
        List<ChatMessage> messages = chatMessageRepository.findMessagesBetweenUsers(user1, user2);
        messages.forEach(msg -> msg.setStatus(MessageStatus.DELIVERED));
        chatMessageRepository.saveAll(messages);
        return messages;
    }

    public List<User> getChatsByUser(User user)
    {
        return userService.getUserByRole(UserRoles.DELIVER);
    }

//    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
//        var chatId = chatRoomService.getChatId(senderId, recipientId, false);
//
//        var messages =
//                chatId.map(cId -> chatMessageRepository.findByChatId(cId)).orElse(new ArrayList<>());
//
//        if(messages.size() > 0) {
//            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
//        }
//
//        return messages;
//    }

//    public ChatMessage findById(String id) {
//        return repository
//                .findById(id)
//                .map(chatMessage -> {
//                    chatMessage.setStatus(MessageStatus.DELIVERED);
//                    return repository.save(chatMessage);
//                })
//                .orElseThrow(() ->
//                        new NotFoundException("can't find message (" + id + ")"));
//    }

//    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
//        Query query = new Query(
//                Criteria
//                        .where("senderId").is(senderId)
//                        .and("recipientId").is(recipientId));
//        Update update = Update.update("status", status);
//        mongoOperations.updateMulti(query, update, ChatMessage.class);
//    }
}