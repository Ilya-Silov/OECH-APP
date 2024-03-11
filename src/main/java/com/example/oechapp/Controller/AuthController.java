package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.AuthRequest;
import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Контроллер для аутентификации")
public class AuthController {
    private final AuthService authService;
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


}
