package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerificationRequest {

    @NotBlank(message = "El email es requerido.")
    @Email(message = "Email invalido.")
    private String email;
}
