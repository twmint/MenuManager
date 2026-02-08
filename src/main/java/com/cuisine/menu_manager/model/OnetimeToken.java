package com.cuisine.menu_manager.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "onetime_tokens")
public class OnetimeToken {
    @Id
    private String id;
    private String token;
    private String username;
    private Instant expiresAt;

    public OnetimeToken(String token, String username, Instant expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}