package com.clinica_javierprado.cjp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer dni;
    private String email;
    private Boolean emailVerified;
    private String phoneNumber;
    private String profilePhoto;
    private String role;
    
    // Doctor specific fields
    private String medicalSpecialty;
    private String cmp;
}
