package com.clinica_javierprado.cjp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorResponse {
    private String code;
    private String message;
    private String email;
}
