package com.clinica_javierprado.cjp_backend.exception;

import lombok.Getter;

@Getter
public class EmailNotVerifiedException extends RuntimeException {

    private final String email;

    public EmailNotVerifiedException(String email) {
        super("Debes verificar tu correo antes de iniciar sesion.");
        this.email = email;
    }
}
