package com.cuisine.menu_manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final String baseUrl;

    public MailService(JavaMailSender mailSender, @Value("${app.fe-base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
    }

    public void sendPlainText(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtml(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String username, String token) {
        String resetLink = baseUrl + "/reset-password.html?token=" + token;
        String body = "Hi " + username + ", \n\n"
                + "Click the link below to reset your password: \n"
                + resetLink + "\n\n"
                + "This link expires in 15 minutes."
                + "If you did not request this, please ignore this email.";
        sendPlainText(username, "Cuisine Menu Manager: Password Reset Request", body);
    }
}
