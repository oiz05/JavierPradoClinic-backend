package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyEmailRequest {

    @NotBlank(message = "El email es requerido.")
    @Email(message = "Email invalido.")
    private String email;

    @NotBlank(message = "El codigo de verificacion es requerido.")
    @Pattern(regexp = "^\\d{6}$", message = "El codigo de verificacion debe tener 6 digitos.")
    private String code;
}
