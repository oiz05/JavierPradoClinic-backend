package com.clinica_javierprado.cjp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentQuoteResponse {
    private Long doctorProfileId;
    private String medicalSpecialty;
    private Long appointmentTypeId;
    private String appointmentTypeName;
    private BigDecimal price;
    private String currency;
    private Integer durationMinutes;
}
