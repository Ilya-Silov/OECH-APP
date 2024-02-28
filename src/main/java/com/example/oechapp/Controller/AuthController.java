package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.AuthDTO;
import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Service.AuthService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private OAuth2AuthorizedClientService authorizedClientService;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;
//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    String redirectUri;

    @PostMapping("/login")
    public ResponseEntity<?> authorize(@RequestBody AuthDTO authDTO)
    {
        JWTAuthResponse response =  authService.signIn(authDTO.getEmail(), authDTO.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/oauth2/code/google")
    public String oauth2LoginSuccess(OAuth2User oauth2User) {
        // Здесь вы можете получить информацию о пользователе OAuth2 и выполнить необходимые действия

        // Пример получения email пользователя
        String email = oauth2User.getAttribute("email");

        return "OAuth2 login successful for user with email: " + email;
    }


    public class AccessTokenResponse {
        @JsonProperty("access_token")
        private String access_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }
    public class UserInfoResponse {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
//    public String googleSignInSuccess(OAuth2AuthenticationToken token) {
//        if (token == null) {
//            // Обработка случая, когда токен не существует
//            return "redirect:/login?error=token_missing";
//        }
//
//        String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();
//        String userName = token.getName();
//
//        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authorizedClientRegistrationId, userName);
//        if (client == null || client.getAccessToken() == null) {
//            // Обработка случая, когда нет авторизованного клиента или токена доступа
//            return "redirect:/login?error=client_missing";
//        }
//
//        OAuth2AccessToken accessToken = client.getAccessToken();
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET,
//                new HttpEntity<>(createHeaders(accessToken)), String.class);
//        String userInfo = response.getBody();
//        // Обработка информации о пользователе
//        // Например, создание нового пользователя в базе данных или обновление существующего профиля
//        return "redirect:/profile";
//    }

    private HttpHeaders createHeaders(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        return headers;
    }
}
