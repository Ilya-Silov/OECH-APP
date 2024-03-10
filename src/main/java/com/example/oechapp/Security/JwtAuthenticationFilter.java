package com.example.oechapp.Security;

import com.example.oechapp.Entity.User;
import com.example.oechapp.Repository.UserRepository;
import com.example.oechapp.Service.JwtService;
import com.example.oechapp.Service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(issuer).build();
    }

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Получаем токен из заголовка
        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX) && !StringUtils.startsWith(authHeader, "Google ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(BEARER_PREFIX.length());

        String email;
        if (authHeader.startsWith("Google "))
        {
            jwt = authHeader.substring("Google ".length());
            JwtDecoder jwtDecoder = jwtDecoder(); // получаем настроенный JwtDecoder из контекста Spring

            try {
                // Проверяем токен
                Jwt decodedJwt = jwtDecoder.decode(jwt);

                // Проверяем, что токен действителен и содержит необходимые атрибуты
                if (decodedJwt.getIssuedAt() != null && decodedJwt.getExpiresAt() != null && decodedJwt.getClaim("email") != null) {
                    // Валидация прошла успешно, аутентифицируем пользователя
                    email = decodedJwt.getClaimAsString("email");

                    // Ваша логика загрузки или создания пользователя на основе email
                    UserDetails userDetails;
                    try {
                        userDetails = userDetailsService.loadUserByUsername(email);
                    }catch (UsernameNotFoundException asdawad)
                    {
                        User user =new User();
                        user.setEmail(email);
                        userRepository.save(user);
                        userDetails = userDetailsService.loadUserByUsername(email);
                    }

                    // Создаем объект аутентификации
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Устанавливаем объект аутентификации в контекст безопасности
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Продолжаем выполнение цепочки фильтров
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (JwtException e) {
                // Если токен невалиден, отправляем ответ с ошибкой 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        try {
            email = jwtService.extractEmail(jwt);
            if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService
                        .userDetailsService()
                        .loadUserByUsername(email);

                // Если токен валиден, то аутентифицируем пользователя
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (MalformedJwtException ex)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}