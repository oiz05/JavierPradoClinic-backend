package com.clinica_javierprado.cjp_backend.service;

import com.clinica_javierprado.cjp_backend.domain.AppointmentType;
import com.clinica_javierprado.cjp_backend.domain.Clinic;
import com.clinica_javierprado.cjp_backend.domain.DoctorProfile;
import com.clinica_javierprado.cjp_backend.dto.AppointmentTypeResponse;
import com.clinica_javierprado.cjp_backend.dto.ClinicResponse;
import com.clinica_javierprado.cjp_backend.dto.DoctorResponse;
import com.clinica_javierprado.cjp_backend.repository.AppointmentTypeRepository;
import com.clinica_javierprado.cjp_backend.repository.ClinicRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final ClinicRepository clinicRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctors() {
        return doctorProfileRepository.findAll().stream()
                .sorted(Comparator.comparing(doctor -> doctor.getUser().getLastName()))
                .map(this::toDoctorResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClinicResponse> getClinics() {
        return clinicRepository.findAll().stream()
                .sorted(Comparator.comparing(Clinic::getName))
                .map(this::toClinicResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentTypeResponse> getAppointmentTypes() {
        return appointmentTypeRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(this::toAppointmentTypeResponse)
                .toList();
    }

    private DoctorResponse toDoctorResponse(DoctorProfile doctorProfile) {
        return DoctorResponse.builder()
                .id(doctorProfile.getId())
                .cmp(doctorProfile.getCmp())
                .medicalSpecialty(doctorProfile.getMedicalSpecialty())
                .email(doctorProfile.getUser().getEmail())
                .firstName(doctorProfile.getUser().getFirstName())
                .lastName(doctorProfile.getUser().getLastName())
                .profilePhoto(doctorProfile.getUser().getProfilePhoto())
                .build();
    }

    private ClinicResponse toClinicResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .build();
    }

    private AppointmentTypeResponse toAppointmentTypeResponse(AppointmentType appointmentType) {
        return AppointmentTypeResponse.builder()
                .id(appointmentType.getId())
                .name(appointmentType.getName())
                .description(appointmentType.getDescription())
                .durationMinutes(appointmentType.getDurationMinutes())
                .build();
    }
}
