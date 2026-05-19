package com.clinica_javierprado.cjp_backend.controller;

import com.clinica_javierprado.cjp_backend.dto.AuthResponse;
import com.clinica_javierprado.cjp_backend.dto.ForgotPasswordRequest;
import com.clinica_javierprado.cjp_backend.dto.LoginRequest;
import com.clinica_javierprado.cjp_backend.dto.MessageResponse;
import com.clinica_javierprado.cjp_backend.dto.RegisterRequest;
import com.clinica_javierprado.cjp_backend.dto.ResetPasswordRequest;
import com.clinica_javierprado.cjp_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("If your email is registered, you will receive a password reset link."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully."));
    }
}
