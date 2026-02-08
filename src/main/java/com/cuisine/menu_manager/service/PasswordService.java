package com.cuisine.menu_manager.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final UserService userService;
    private final TokenService tokenService;

    public PasswordService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public boolean userExists(String username) {
        return userService.userExists(username);
    }

    public String generateResetToken(String username) {
        return tokenService.generateOnetimeToken(username);
    }

    public boolean resetPassword(String token, String newPassword) {
        String username = tokenService.validateOnetimeToken(token);
        if (username == null) {
            return false;
        }
        
        userService.resetPassword(username, newPassword);
        tokenService.deleteOnetimeToken(token);
        return true;
    }
}
