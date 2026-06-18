package com.clinica_javierprado.cjp_backend.config;

import com.clinica_javierprado.cjp_backend.domain.AppointmentPrice;
import com.clinica_javierprado.cjp_backend.domain.AppointmentType;
import com.clinica_javierprado.cjp_backend.domain.Clinic;
import com.clinica_javierprado.cjp_backend.domain.DoctorClinic;
import com.clinica_javierprado.cjp_backend.domain.DoctorProfile;
import com.clinica_javierprado.cjp_backend.domain.DoctorSchedule;
import com.clinica_javierprado.cjp_backend.repository.AppointmentPriceRepository;
import com.clinica_javierprado.cjp_backend.repository.AppointmentTypeRepository;
import com.clinica_javierprado.cjp_backend.repository.ClinicRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorClinicRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorProfileRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@Profile({"docker", "local-db"})
@Order(2)
@RequiredArgsConstructor
public class LocalCatalogSeedDataInitializer implements ApplicationRunner {

    private final ClinicRepository clinicRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final AppointmentPriceRepository appointmentPriceRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorClinicRepository doctorClinicRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Clinic sanIsidro = upsertClinic("Sede Principal San Isidro", "Av. Javier Prado Este 1066");
        Clinic surco = upsertClinic("Sede Surco", "Av. Caminos del Inca 1234");
        Clinic laMolina = upsertClinic("Sede La Molina", "Av. La Molina 789");

        AppointmentType presencial = upsertAppointmentType("Consulta presencial", "Consulta medica en sede", 30);
        AppointmentType virtual = upsertAppointmentType("Consulta virtual", "Consulta medica por videollamada", 30);
        AppointmentType control = upsertAppointmentType("Control", "Seguimiento posterior a una consulta", 20);

        seedPrices(presencial, virtual, control);
        seedDoctorLocationsAndSchedules(List.of(sanIsidro, surco, laMolina));
        log.info("Local catalog seed completed");
    }

    private Clinic upsertClinic(String name, String address) {
        return clinicRepository.findByName(name)
                .orElseGet(() -> clinicRepository.save(Clinic.builder()
                        .name(name)
                        .address(address)
                        .build()));
    }

    private AppointmentType upsertAppointmentType(String name, String description, int durationMinutes) {
        return appointmentTypeRepository.findByName(name)
                .orElseGet(() -> appointmentTypeRepository.save(AppointmentType.builder()
                        .name(name)
                        .description(description)
                        .durationMinutes(durationMinutes)
                        .active(true)
                        .build()));
    }

    private void seedPrices(AppointmentType presencial, AppointmentType virtual, AppointmentType control) {
        seedPrice("Cardiologia", presencial, "150.00");
        seedPrice("Cardiologia", virtual, "120.00");
        seedPrice("Cardiologia", control, "90.00");
        seedPrice("Dermatologia", presencial, "130.00");
        seedPrice("Dermatologia", virtual, "100.00");
        seedPrice("Dermatologia", control, "80.00");
        seedPrice("Medicina General", presencial, "100.00");
        seedPrice("Medicina General", virtual, "80.00");
        seedPrice("Medicina General", control, "60.00");
        seedPrice("Pediatria", presencial, "120.00");
        seedPrice("Pediatria", virtual, "95.00");
        seedPrice("Pediatria", control, "75.00");
    }

    private void seedPrice(String medicalSpecialty, AppointmentType appointmentType, String price) {
        if (appointmentPriceRepository.findActivePrice(medicalSpecialty, appointmentType.getId(), LocalDate.now()).isPresent()) {
            return;
        }
        appointmentPriceRepository.save(AppointmentPrice.builder()
                .medicalSpecialty(medicalSpecialty)
                .appointmentType(appointmentType)
                .price(new BigDecimal(price))
                .currency("PEN")
                .validFrom(LocalDate.now())
                .active(true)
                .build());
    }

    private void seedDoctorLocationsAndSchedules(List<Clinic> clinics) {
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();
        for (DoctorProfile doctor : doctors) {
            for (Clinic clinic : clinics) {
                if (!doctorClinicRepository.existsByDoctorProfileIdAndClinicId(doctor.getId(), clinic.getId())) {
                    doctorClinicRepository.save(DoctorClinic.builder()
                            .doctorProfile(doctor)
                            .clinic(clinic)
                            .build());
                }
                seedSchedule(doctor, clinic, 1, LocalTime.of(8, 0), LocalTime.of(12, 0));
                seedSchedule(doctor, clinic, 2, LocalTime.of(8, 0), LocalTime.of(12, 0));
                seedSchedule(doctor, clinic, 3, LocalTime.of(14, 0), LocalTime.of(18, 0));
                seedSchedule(doctor, clinic, 4, LocalTime.of(8, 0), LocalTime.of(12, 0));
                seedSchedule(doctor, clinic, 5, LocalTime.of(14, 0), LocalTime.of(18, 0));
            }
        }
    }

    private void seedSchedule(DoctorProfile doctor, Clinic clinic, int dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if (doctorScheduleRepository.existsByDoctorProfileIdAndClinicIdAndDayOfWeekAndStartTimeAndEndTime(
                doctor.getId(), clinic.getId(), dayOfWeek, startTime, endTime)) {
            return;
        }
        doctorScheduleRepository.save(DoctorSchedule.builder()
                .doctorProfile(doctor)
                .clinic(clinic)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .active(true)
                .build());
    }
}
