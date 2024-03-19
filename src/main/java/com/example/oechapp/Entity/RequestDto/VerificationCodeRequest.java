package com.example.oechapp.Entity.RequestDto;

import lombok.Data;

@Data
public class VerificationCodeRequest {
    private String email;
    private String code;
}