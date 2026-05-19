package com.clinica_javierprado.cjp_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail; // Typically the configured username or a verified sender email in Resend

    public void sendPasswordResetEmail(String to, String token) {
        // Here we build the password reset link
        // This could be pointing to the frontend URL
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("onboarding@resend.dev"); // Note: In a real environment with Resend, you use your verified domain. Using default sandbox for test.
        message.setTo(to);
        message.setSubject("CJP - Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(message);
    }
}
