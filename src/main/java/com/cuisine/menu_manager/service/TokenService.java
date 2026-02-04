package com.cuisine.menu_manager.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cuisine.menu_manager.model.User;
import com.cuisine.menu_manager.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
    private final UserRepository userRepository;
    private final SecretKey key;
    private final PasswordEncoder passwordEncoder;
    
    public TokenService(UserRepository userRepository, @Value("${jwt.secret}") String jwtSecret, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // expires in 30 minutes
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1800000))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", String.class);
        } catch (JwtException e) {
            return null;
        }
    }
}