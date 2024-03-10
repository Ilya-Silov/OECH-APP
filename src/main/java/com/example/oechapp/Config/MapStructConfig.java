package com.example.oechapp.Config;

import com.example.oechapp.Entity.RequestDto.Mapper.PackageMapper;
import com.example.oechapp.Entity.RequestDto.Mapper.UserMapper;
import com.example.oechapp.Entity.ResponseDTO.UserResponse;
import com.example.oechapp.Entity.ResponseDTO.mappers.UserResponseMapper;
import com.example.oechapp.messanger.entity.dto.ChatMessageMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public UserResponseMapper userResponseMapper()
    {
        return Mappers.getMapper(UserResponseMapper.class);
    }

    @Bean
    public PackageMapper packageMapper() {
        return Mappers.getMapper(PackageMapper.class);
    }

    @Bean
    public ChatMessageMapper chatMessageMapper()
    {
        return Mappers.getMapper(ChatMessageMapper.class);
    }
}