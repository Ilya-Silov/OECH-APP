package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.AuthRequest;
import com.example.oechapp.Entity.RequestDto.PasswordResetRequest;
import com.example.oechapp.Entity.RequestDto.VerificationCodeRequest;
import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Service.AuthService;
import com.example.oechapp.Service.RecoveryCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Контроллер для аутентификации")
public class AuthController {
    private final AuthService authService;
    private final RecoveryCodeService recoveryCodeService;
    private OAuth2AuthorizedClientService authorizedClientService;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;

    @Operation(summary = "Аутентификация", description = "Выполняет аутентификацию пользователя.")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authorize(@RequestBody AuthRequest authDTO)
    {
        JWTAuthResponse response =  authService.signIn(authDTO.getEmail(), authDTO.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Отправка кода сброса пароля", description = "Отправляет код для сброса пароля на указанный адрес электронной почты.")
    @PostMapping("/reset/code")
    public ResponseEntity<?> sendResetCode(@RequestParam String email) {
        try {
            authService.sendResetCode(email);
        }catch (NotFoundException ex)
        {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Сброс пароля", description = "Сбрасывает пароль пользователя по коду сброса.")
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {

            authService.resetPassword(passwordResetRequest.getEmail(),
                    passwordResetRequest.getCode(),
                    passwordResetRequest.getNewPassword());
        }
        catch (RuntimeException ex)
        {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Верификация кода сброса", description = "Проверяет, является ли предоставленный код действительным для указанного адреса электронной почты.")
    @ApiResponse(responseCode = "200", description = "Код верный")
    @ApiResponse(responseCode = "400", description = "Код неверный")
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationCodeRequest verificationCodeRequest) {
        if (recoveryCodeService.verifyResetCode(verificationCodeRequest.getEmail(), verificationCodeRequest.getCode()))
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
