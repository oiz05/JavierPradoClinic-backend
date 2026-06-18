package com.clinica_javierprado.cjp_backend.service;

import com.clinica_javierprado.cjp_backend.domain.Appointment;
import com.clinica_javierprado.cjp_backend.domain.AppointmentStatus;
import com.clinica_javierprado.cjp_backend.domain.AppointmentType;
import com.clinica_javierprado.cjp_backend.domain.Clinic;
import com.clinica_javierprado.cjp_backend.domain.DoctorProfile;
import com.clinica_javierprado.cjp_backend.domain.DoctorSchedule;
import com.clinica_javierprado.cjp_backend.domain.User;
import com.clinica_javierprado.cjp_backend.dto.AvailabilitySlotResponse;
import com.clinica_javierprado.cjp_backend.repository.AppointmentRepository;
import com.clinica_javierprado.cjp_backend.repository.AppointmentTypeRepository;
import com.clinica_javierprado.cjp_backend.repository.ClinicRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorClinicRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorProfileRepository;
import com.clinica_javierprado.cjp_backend.repository.DoctorScheduleRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppointmentServiceTest {

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final DoctorProfileRepository doctorProfileRepository = mock(DoctorProfileRepository.class);
    private final ClinicRepository clinicRepository = mock(ClinicRepository.class);
    private final AppointmentTypeRepository appointmentTypeRepository = mock(AppointmentTypeRepository.class);
    private final DoctorClinicRepository doctorClinicRepository = mock(DoctorClinicRepository.class);
    private final DoctorScheduleRepository doctorScheduleRepository = mock(DoctorScheduleRepository.class);
    private final PricingService pricingService = mock(PricingService.class);

    private final AppointmentService appointmentService = new AppointmentService(
            appointmentRepository,
            doctorProfileRepository,
            clinicRepository,
            appointmentTypeRepository,
            doctorClinicRepository,
            doctorScheduleRepository,
            pricingService
    );

    @Test
    void getAvailabilityUsesDurationPlusBufferStepAndKeepsPriceOptional() {
        LocalDate date = LocalDate.now().plusDays(7);
        DoctorProfile doctor = DoctorProfile.builder()
                .id(1L)
                .medicalSpecialty("Cardiologia")
                .user(User.builder().firstName("Ana").lastName("Paz").build())
                .build();
        Clinic clinic = Clinic.builder().id(2L).name("Sede").address("Av. Test").build();
        AppointmentType appointmentType = AppointmentType.builder()
                .id(3L)
                .name("Consulta presencial")
                .description("Consulta medica")
                .durationMinutes(30)
                .active(true)
                .build();
        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctorProfile(doctor)
                .clinic(clinic)
                .dayOfWeek(date.getDayOfWeek().getValue())
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .active(true)
                .build();
        Appointment existingAppointment = Appointment.builder()
                .id(10L)
                .doctorProfile(doctor)
                .clinic(clinic)
                .appointmentType(appointmentType)
                .appointmentDate(date.atTime(9, 0))
                .status(AppointmentStatus.CONFIRMED)
                .build();

        when(doctorProfileRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(clinic));
        when(appointmentTypeRepository.findById(3L)).thenReturn(Optional.of(appointmentType));
        when(doctorClinicRepository.existsByDoctorProfileIdAndClinicId(1L, 2L)).thenReturn(true);
        when(pricingService.resolvePrice(doctor, appointmentType, date))
                .thenThrow(new IllegalArgumentException("No price configured"));
        when(doctorScheduleRepository.findByDoctorProfileIdAndClinicIdAndDayOfWeekAndActiveTrue(
                1L,
                2L,
                date.getDayOfWeek().getValue()
        )).thenReturn(List.of(schedule));
        when(appointmentRepository.findByDoctorProfileIdAndClinicIdAndAppointmentDateBetween(
                1L,
                2L,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay().minusNanos(1)
        )).thenReturn(List.of(existingAppointment));

        List<AvailabilitySlotResponse> slots = appointmentService.getAvailability(1L, 2L, 3L, date);

        assertThat(slots).extracting(slot -> slot.getStartAt().toLocalTime())
                .containsExactly(
                        LocalTime.of(8, 0),
                        LocalTime.of(8, 40),
                        LocalTime.of(9, 20),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 40),
                        LocalTime.of(11, 20)
                );
        assertThat(slots).filteredOn(AvailabilitySlotResponse::isAvailable)
                .extracting(slot -> slot.getStartAt().toLocalTime())
                .containsExactly(
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 40),
                        LocalTime.of(11, 20)
                );
        assertThat(slots).allSatisfy(slot -> {
            assertThat(slot.getPrice()).isNull();
            assertThat(slot.getCurrency()).isNull();
        });
    }
}
