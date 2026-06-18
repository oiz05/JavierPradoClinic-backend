package com.clinica_javierprado.cjp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long doctorProfileId;
    private String doctorName;
    private String medicalSpecialty;
    private Long clinicId;
    private String clinicName;
    private String clinicAddress;
    private Long appointmentTypeId;
    private String appointmentTypeName;
    private Integer durationMinutes;
    private LocalDateTime appointmentDate;
    private String status;
    private BigDecimal price;
    private String currency;
}
