package com.clinica_javierprado.cjp_backend.repository;

import com.clinica_javierprado.cjp_backend.domain.AppointmentPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AppointmentPriceRepository extends JpaRepository<AppointmentPrice, Long> {

    @Query("""
            select ap from AppointmentPrice ap
            where lower(ap.medicalSpecialty) = lower(:medicalSpecialty)
              and ap.appointmentType.id = :appointmentTypeId
              and ap.active = true
              and ap.validFrom <= :date
              and (ap.validTo is null or ap.validTo >= :date)
            order by ap.validFrom desc
            """)
    Optional<AppointmentPrice> findActivePrice(
            @Param("medicalSpecialty") String medicalSpecialty,
            @Param("appointmentTypeId") Long appointmentTypeId,
            @Param("date") LocalDate date
    );
}
