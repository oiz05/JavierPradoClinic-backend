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

    @Value("${app.password-reset.frontend-base-url}")
    private String frontendBaseUrl;

    @Value("${app.password-reset.path}")
    private String passwordResetPath;

    @Value("${app.password-reset.mail-from}")
    private String fromEmail;

    @Value("${app.password-reset.mail-subject}")
    private String passwordResetSubject;

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendBaseUrl + passwordResetPath + "?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(passwordResetSubject);
        message.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(message);
    }
}
