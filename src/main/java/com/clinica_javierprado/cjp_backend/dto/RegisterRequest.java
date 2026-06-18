package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Los nombres son requeridos.")
    private String firstName;

    @NotBlank(message = "Los apellidos son requeridos.")
    private String lastName;

    @NotNull(message = "El DNI es requerido.")
    @Min(value = 10000000, message = "El DNI debe tener 8 digitos numericos.")
    @Max(value = 99999999, message = "El DNI debe tener 8 digitos numericos.")
    private Integer dni;

    @NotBlank(message = "El email es requerido.")
    @Email(message = "Email invalido.")
    private String email;

    @NotBlank(message = "La contrasena es requerida.")
    private String password;

    @NotBlank(message = "El telefono es requerido.")
    @Pattern(regexp = "^9\\d{8}$", message = "El telefono debe empezar con 9 y tener 9 digitos.")
    private String phoneNumber;
}
