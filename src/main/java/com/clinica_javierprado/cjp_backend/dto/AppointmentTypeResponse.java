package com.clinica_javierprado.cjp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTypeResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationMinutes;
}
