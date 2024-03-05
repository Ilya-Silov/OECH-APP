package com.example.oechapp.Entity.RequestDto;

import lombok.Data;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
