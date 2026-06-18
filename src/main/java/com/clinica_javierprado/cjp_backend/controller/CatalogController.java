package com.clinica_javierprado.cjp_backend.controller;

import com.clinica_javierprado.cjp_backend.dto.AppointmentTypeResponse;
import com.clinica_javierprado.cjp_backend.dto.ClinicResponse;
import com.clinica_javierprado.cjp_backend.dto.DoctorResponse;
import com.clinica_javierprado.cjp_backend.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors() {
        return ResponseEntity.ok(catalogService.getDoctors());
    }

    @GetMapping("/clinics")
    public ResponseEntity<List<ClinicResponse>> getClinics() {
        return ResponseEntity.ok(catalogService.getClinics());
    }

    @GetMapping("/appointment-types")
    public ResponseEntity<List<AppointmentTypeResponse>> getAppointmentTypes() {
        return ResponseEntity.ok(catalogService.getAppointmentTypes());
    }
}
