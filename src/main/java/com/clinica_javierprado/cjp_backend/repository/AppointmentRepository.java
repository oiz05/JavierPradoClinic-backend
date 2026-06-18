package com.clinica_javierprado.cjp_backend.repository;

import com.clinica_javierprado.cjp_backend.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorProfileId(Long doctorProfileId);
    List<Appointment> findByClinicId(Long clinicId);
    List<Appointment> findByPatientIdAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(Long patientId, LocalDateTime appointmentDate);
    List<Appointment> findByDoctorProfileIdAndClinicIdAndAppointmentDateBetween(Long doctorProfileId, Long clinicId, LocalDateTime start, LocalDateTime end);
}
