package com.example.oechapp.Entity.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetRequest {
    private String email;
    private String code;
    private String newPassword;
}
