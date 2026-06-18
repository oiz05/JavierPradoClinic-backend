package com.clinica_javierprado.cjp_backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    @NotNull
    private Long doctorProfileId;

    @NotNull
    private Long clinicId;

    @NotNull
    private Long appointmentTypeId;

    @NotNull
    @Future
    private LocalDateTime appointmentDate;
}
