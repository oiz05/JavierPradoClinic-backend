package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditProfileRequest {
    @NotBlank
    @Email
    private String email;

    private String phoneNumber;

    // Doctor specific fields
    private String medicalSpecialty;
    private String cmp;
}
