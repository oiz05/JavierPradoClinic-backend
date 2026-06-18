package com.clinica_javierprado.cjp_backend.repository;

import com.clinica_javierprado.cjp_backend.domain.DoctorClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorClinicRepository extends JpaRepository<DoctorClinic, Long> {
    boolean existsByDoctorProfileIdAndClinicId(Long doctorProfileId, Long clinicId);
    Optional<DoctorClinic> findByDoctorProfileIdAndClinicId(Long doctorProfileId, Long clinicId);
}
