package com.clinica_javierprado.cjp_backend.controller;

import com.clinica_javierprado.cjp_backend.domain.User;
import com.clinica_javierprado.cjp_backend.dto.AppointmentQuoteResponse;
import com.clinica_javierprado.cjp_backend.dto.AppointmentResponse;
import com.clinica_javierprado.cjp_backend.dto.AvailabilitySlotResponse;
import com.clinica_javierprado.cjp_backend.dto.CreateAppointmentRequest;
import com.clinica_javierprado.cjp_backend.dto.RescheduleAppointmentRequest;
import com.clinica_javierprado.cjp_backend.dto.UpdateAppointmentStatusRequest;
import com.clinica_javierprado.cjp_backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.getMyAppointments(user));
    }

    @GetMapping("/quote")
    public ResponseEntity<AppointmentQuoteResponse> quote(
            @RequestParam Long doctorProfileId,
            @RequestParam Long appointmentTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        return ResponseEntity.ok(appointmentService.quote(doctorProfileId, appointmentTypeId, appointmentDate));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilitySlotResponse>> getAvailability(
            @RequestParam Long doctorProfileId,
            @RequestParam Long clinicId,
            @RequestParam Long appointmentTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailability(doctorProfileId, clinicId, appointmentTypeId, date));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(user, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody @Valid UpdateAppointmentStatusRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(user, id, request.getStatus()));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody @Valid RescheduleAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.reschedule(user, id, request));
    }
}
