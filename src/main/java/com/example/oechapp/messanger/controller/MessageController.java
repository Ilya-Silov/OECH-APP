    package com.example.oechapp.messanger.controller;

    import com.example.oechapp.Entity.User;
    import com.example.oechapp.Security.UserDetailsImpl;
    import com.example.oechapp.Service.FileStorageService;
    import com.example.oechapp.Service.UserService;
    import com.example.oechapp.messanger.entity.ChatMessage;
    import com.example.oechapp.messanger.entity.ChatRoom;
    import com.example.oechapp.messanger.entity.dto.ChatMessageMapper;
    import com.example.oechapp.messanger.entity.dto.ChatMessageRequest;
    import com.example.oechapp.messanger.service.ChatMessageService;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    import java.util.ArrayList;
    import java.util.List;

    @RestController
    @RequestMapping("/api/messages")
    @RequiredArgsConstructor
    @Tag(name = "Messages", description = "Котроллер для работы с сообщениями")
    public class MessageController {

        private final ChatMessageService chatMessageService;
        private final ChatMessageMapper chatMessageMapper;
        private final UserService userService;

        @PostMapping
        @Operation(summary = "Отправить сообщение", description = "Отправляет сообщение в чат.\nsenderId - id авторзованного пользователя,\nrecipientId - id получателя\nАвторизация не требуется.")
        public ChatMessage sendMessage(@RequestBody ChatMessageRequest message) {
            return chatMessageService.save(chatMessageMapper.mapChatMessageRequestToChatMessage(message));
        }

        @Operation(summary = "История сообщений", description = "Получает историю сообщений между двумя пользователями. При получении все сообщения из стории переводятся в статус прочтано.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Успешно получена история сообщений"),
                @ApiResponse(responseCode = "404", description = "История сообщений не найдена")
        })
        @GetMapping("/{senderId}/{recipientId}")
        public ResponseEntity<List<ChatMessage>> getMessageHistory(@PathVariable Long senderId, @PathVariable Long recipientId, Authentication auth) {
    //        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
    //        User user = userService.getUserByEmail(auser.getUsername()).get();
    //        if (!user.getId().equals(senderId) && !user.getId().equals(recipientId))
    //        {
    //            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //        }

            return new ResponseEntity<>(chatMessageService.getMessageHistory(senderId, recipientId), HttpStatus.OK);
        }


        @Operation(summary = "Доступные чаты", description = "Получает список доступных чатов для пользователя")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Успешно получен список доступных чатов"),
                @ApiResponse(responseCode = "403", description = "Доступ запрещен")
        })
        @GetMapping("/chats/available")
        @PreAuthorize("isAuthenticated()")
        @SecurityRequirement(name = "JWT Token")
        public ResponseEntity<?> getChats(Authentication auth)
        {
            UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
            User user = userService.getUserByEmail(auser.getUsername()).get();

            List<ChatRoom> chatRooms = chatMessageService.getChatsByUser(user).stream()
                    .map(mUser->
                           ChatRoom.builder()
                                    .recipientId(mUser.getId())
                                    .countUnread(chatMessageService.countNewMessages(user.getId(), mUser.getId()))
                                    .build()).toList();

            return new ResponseEntity<>(chatRooms, HttpStatus.OK);
        }
    }