package com.example.oechapp.messanger.repository;

import com.example.oechapp.messanger.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
