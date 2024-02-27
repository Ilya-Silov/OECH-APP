package com.example.oechapp.Service;

import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public JWTAuthResponse signIn(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                password
        ));

        var user = userDetailsService.loadUserByUsername(username);

        var jwt = jwtService.generateToken(user);
        return new JWTAuthResponse(jwt);
    }
}
