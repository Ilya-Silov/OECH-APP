package com.example.oechapp.Entity.RequestDto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
