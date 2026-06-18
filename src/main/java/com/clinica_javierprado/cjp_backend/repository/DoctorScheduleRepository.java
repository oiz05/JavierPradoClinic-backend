package com.clinica_javierprado.cjp_backend.repository;

import com.clinica_javierprado.cjp_backend.domain.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorProfileIdAndClinicIdAndDayOfWeekAndActiveTrue(Long doctorProfileId, Long clinicId, Integer dayOfWeek);
    boolean existsByDoctorProfileIdAndClinicIdAndDayOfWeekAndStartTimeAndEndTime(Long doctorProfileId, Long clinicId, Integer dayOfWeek, java.time.LocalTime startTime, java.time.LocalTime endTime);
}
