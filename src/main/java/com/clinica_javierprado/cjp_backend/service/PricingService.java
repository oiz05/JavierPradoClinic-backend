package com.clinica_javierprado.cjp_backend.service;

import com.clinica_javierprado.cjp_backend.domain.AppointmentPrice;
import com.clinica_javierprado.cjp_backend.domain.AppointmentType;
import com.clinica_javierprado.cjp_backend.domain.DoctorProfile;
import com.clinica_javierprado.cjp_backend.dto.AppointmentQuoteResponse;
import com.clinica_javierprado.cjp_backend.repository.AppointmentPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final AppointmentPriceRepository appointmentPriceRepository;

    public AppointmentPrice resolvePrice(DoctorProfile doctorProfile, AppointmentType appointmentType, LocalDate date) {
        return appointmentPriceRepository.findActivePrice(
                        doctorProfile.getMedicalSpecialty(),
                        appointmentType.getId(),
                        date
                )
                .orElseThrow(() -> new IllegalArgumentException("No price configured for this specialty and appointment type."));
    }

    public AppointmentQuoteResponse buildQuote(DoctorProfile doctorProfile, AppointmentType appointmentType, LocalDate date) {
        AppointmentPrice price = resolvePrice(doctorProfile, appointmentType, date);
        return AppointmentQuoteResponse.builder()
                .doctorProfileId(doctorProfile.getId())
                .medicalSpecialty(doctorProfile.getMedicalSpecialty())
                .appointmentTypeId(appointmentType.getId())
                .appointmentTypeName(appointmentType.getName())
                .price(price.getPrice())
                .currency(price.getCurrency())
                .durationMinutes(appointmentType.getDurationMinutes())
                .build();
    }
}
