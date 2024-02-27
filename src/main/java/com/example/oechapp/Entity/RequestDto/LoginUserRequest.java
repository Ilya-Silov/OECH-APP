package com.example.oechapp.Entity.RequestDto;

import com.example.oechapp.Entity.Address;
import lombok.Data;

import lombok.Data;

import java.util.List;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}


