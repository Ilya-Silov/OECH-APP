package com.example.oechapp.Config;

import com.example.oechapp.Entity.RequestDto.Mapper.PackageMapper;
import com.example.oechapp.Entity.RequestDto.Mapper.UserMapper;
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
    public PackageMapper packageMapper() {
        return Mappers.getMapper(PackageMapper.class);
    }
}