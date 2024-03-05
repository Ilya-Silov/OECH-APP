package com.example.oechapp.Entity.RequestDto.Mapper;

import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import com.example.oechapp.Entity.RequestDto.CreateUserRequest;

import com.example.oechapp.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    User mapCreateUserRequestToUser(CreateUserRequest request);


    // Добавьте другие методы маппинга, если необходимо
}