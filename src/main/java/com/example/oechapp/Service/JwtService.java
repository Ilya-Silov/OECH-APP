package com.example.oechapp.Service;

import com.example.oechapp.Security.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    @Value("${OECH.app.secret}")
    private String jwtSigningKey;

    @Value("${OECH.app.lifetime}")
    private int jwtLifetime;

    public String generateToken(UserDetails userDetails)
    {
        //UserDetailsImpl userDetails = (UserDetailsImpl)userDetails;
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserDetailsImpl customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getUsername());
            //claims.put("role", customUserDetails.getAuthorities());
        }
        return generateToken(claims, userDetails);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token)
                .getPayload();
    }
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtLifetime* 1000L*60L))
                .signWith(getSigningKey()).compact();
    }
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);

        //return Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
    }
}
