package com.clinica_javierprado.cjp_backend.dto;

import com.clinica_javierprado.cjp_backend.domain.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAppointmentStatusRequest {
    @NotNull
    private AppointmentStatus status;
}
