package com.cuisine.menu_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cuisine.menu_manager.dto.ForgotPasswordRequest;
import com.cuisine.menu_manager.dto.ResetPasswordRequest;
import com.cuisine.menu_manager.dto.UsernameResponse;
import com.cuisine.menu_manager.service.MailService;
import com.cuisine.menu_manager.service.PasswordService;
import com.cuisine.menu_manager.service.TokenService;

@RestController
@RequestMapping("/password")
public class PasswordController {
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final MailService mailService;

    public PasswordController(PasswordService passwordService, TokenService tokenService, MailService mailService) {
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String username = request.getUsername();
        if (!passwordService.userExists(username)) {
            return ResponseEntity.ok().build();
        }
        String token = passwordService.generateResetToken(username);
        mailService.sendPasswordResetEmail(username, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean success = passwordService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam String token) {
        String username = tokenService.validateOnetimeToken(token);
        if (username == null) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(new UsernameResponse(username));
    }
}
