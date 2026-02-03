package com.cuisine.menu_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cuisine.menu_manager.dto.Credentials;
import com.cuisine.menu_manager.dto.TokenResponse;
import com.cuisine.menu_manager.service.TokenService;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> create(@RequestBody Credentials credentials) {
        String token = tokenService.login(credentials.getUsername(), credentials.getPassword());
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new TokenResponse(token));
    }
}