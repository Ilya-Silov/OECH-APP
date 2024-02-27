package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.AuthDTO;
import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private OAuth2AuthorizedClientService authorizedClientService;


    @PostMapping("/login")
    public ResponseEntity<?> authorize(@RequestBody AuthDTO authDTO)
    {
        JWTAuthResponse response =  authService.signIn(authDTO.getEmail(), authDTO.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/oauth2/code/google")
    public String googleSignInSuccess(OAuth2AuthenticationToken token) {
        // Обработка случая, когда токен не существует или не является OAuth2 токеном

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
        OAuth2AccessToken accessToken = client.getAccessToken();
        // Получение информации о пользователе из Google API с использованием токена доступа

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET,
                new HttpEntity<>(createHeaders(accessToken)), String.class);
        String userInfo = response.getBody();
        // Обработка информации о пользователе
        // Например, создание нового пользователя в базе данных или обновление существующего профиля
        return "redirect:/profile";
    }

    private HttpHeaders createHeaders(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        return headers;
    }
}
