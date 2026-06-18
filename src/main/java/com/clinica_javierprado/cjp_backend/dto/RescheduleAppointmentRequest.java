package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleAppointmentRequest {
    @NotNull
    @Future
    private LocalDateTime appointmentDate;
}
