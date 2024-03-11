package com.example.oechapp.messanger.repository;

import com.example.oechapp.Entity.User;
import com.example.oechapp.messanger.entity.ChatMessage;
import com.example.oechapp.messanger.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.senderId = :senderId AND m.recipientId = :recipientId) " +
            "   OR (m.senderId = :recipientId AND m.recipientId = :senderId) " +
            "ORDER BY m.id")
    public List<ChatMessage> findMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);
    @Query("SELECT m FROM ChatMessage m " +
            "WHERE ((m.senderId = :senderId AND m.recipientId = :recipientId) " +
            "   OR (m.senderId = :recipientId AND m.recipientId = :senderId)) " +
            "AND m.status = :status " +
            "ORDER BY m.id")
    public List<ChatMessage> countMessagesBetweenUsersByStatus(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId, @Param("status") MessageStatus status);

}