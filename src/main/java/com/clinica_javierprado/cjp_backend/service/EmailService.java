package com.clinica_javierprado.cjp_backend.service;

import com.clinica_javierprado.cjp_backend.exception.EmailDeliveryException;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${app.resend.api-key}")
    private String resendApiKey;

    @Value("${app.mail.from-email}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Value("${app.mail.verification-subject}")
    private String verificationSubject;

    @Value("${app.mail.welcome-subject}")
    private String welcomeSubject;

    @Value("${app.password-reset.frontend-base-url}")
    private String frontendBaseUrl;

    @Value("${app.password-reset.path}")
    private String passwordResetPath;

    @Value("${app.password-reset.mail-subject}")
    private String passwordResetSubject;

    private Resend resend;

    @PostConstruct
    void init() {
        if (isBlank(resendApiKey)) {
            throw new IllegalStateException("RESEND_API_KEY is required to send emails.");
        }
        if (isBlank(fromEmail) || !fromEmail.matches("^[^\\s@<>]+@[^\\s@<>]+\\.[^\\s@<>]+$")) {
            throw new IllegalStateException("MAIL_FROM must be a valid sender email address.");
        }

        resend = new Resend(resendApiKey.trim());
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendBaseUrl + passwordResetPath + "?token=" + token;
        String html = """
                <p>Hola,</p>
                <p>Recibimos una solicitud para restablecer tu contrasena en Clinica Javier Prado.</p>
                <p><a href=\"%s\">Restablecer contrasena</a></p>
                <p>Si no solicitaste este cambio, ignora este mensaje.</p>
                """.formatted(escapeHtml(resetUrl));

        sendEmail(to, passwordResetSubject, html);
    }

    public void sendVerificationCodeEmail(String to, String firstName, String code, long expirationMinutes) {
        String html = """
                <p>Hola, %s.</p>
                <p>Tu codigo de verificacion para Clinica Javier Prado es:</p>
                <p style=\"font-size: 24px; font-weight: 700; letter-spacing: 4px;\">%s</p>
                <p>Este codigo vence en %d minutos.</p>
                <p>Si no solicitaste este registro, ignora este mensaje.</p>
                """.formatted(escapeHtml(firstName), escapeHtml(code), expirationMinutes);

        sendEmail(to, verificationSubject, html);
    }

    public void sendWelcomeEmail(String to, String firstName) {
        String html = """
                <p>Hola, %s.</p>
                <p>Bienvenido a Clinica Javier Prado. Tu correo fue verificado correctamente y tu cuenta ya esta activa.</p>
                <p>Gracias por confiar en nosotros.</p>
                """.formatted(escapeHtml(firstName));

        sendEmail(to, welcomeSubject, html);
    }

    private void sendEmail(String to, String subject, String html) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(buildSender())
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new EmailDeliveryException("No se pudo enviar el correo. Verifica la configuracion de Resend.", e);
        }
    }

    private String buildSender() {
        if (isBlank(fromName)) {
            return fromEmail.trim();
        }
        return fromName.trim() + " <" + fromEmail.trim() + ">";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
