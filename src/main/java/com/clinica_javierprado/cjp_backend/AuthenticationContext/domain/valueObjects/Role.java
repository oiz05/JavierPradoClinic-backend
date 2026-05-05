package com.clinica_javierprado.cjp_backend.AuthenticationContext.domain.valueObjects;

import jakarta.persistence.EnumType;

public enum Role {
    ADMIN,
    PATIENT,
    DOCTOR;

    public static void setRole(String role) {

        if (role.isBlank()) {
            throw new IllegalArgumentException("Role can't be nule");
        }

        if(!(role.equals("ADMIN") || role.equals("PATIENT") || role.equals("DOCTOR"))) {
            throw new IllegalArgumentException("Role must be one of the given: ADMIN, PATIENT or DOCTOR");
        }

        EnumType.valueOf(role);

    }
}
