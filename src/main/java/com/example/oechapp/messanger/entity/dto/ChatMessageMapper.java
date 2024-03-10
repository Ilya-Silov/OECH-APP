package com.example.oechapp.messanger.entity.dto;

import com.example.oechapp.Entity.Package;
import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import com.example.oechapp.messanger.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    ChatMessage mapChatMessageRequestToChatMessage(ChatMessageRequest chatMessageRequest);

}
