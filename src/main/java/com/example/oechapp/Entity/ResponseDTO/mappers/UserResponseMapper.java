package com.example.oechapp.Entity.ResponseDTO.mappers;


import com.example.oechapp.Entity.ResponseDTO.UserResponse;
import com.example.oechapp.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    UserResponse mapUserToUserResponse(User user);

}
