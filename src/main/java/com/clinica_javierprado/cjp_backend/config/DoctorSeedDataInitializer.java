package com.clinica_javierprado.cjp_backend.config;

import com.clinica_javierprado.cjp_backend.domain.DoctorProfile;
import com.clinica_javierprado.cjp_backend.domain.Role;
import com.clinica_javierprado.cjp_backend.domain.User;
import com.clinica_javierprado.cjp_backend.repository.DoctorProfileRepository;
import com.clinica_javierprado.cjp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DoctorSeedDataInitializer implements ApplicationRunner {

    private final DoctorSeedProperties doctorSeedProperties;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!doctorSeedProperties.isEnabled() || doctorSeedProperties.getItems().isEmpty()) {
            return;
        }

        int seededDoctors = 0;
        for (DoctorSeedProperties.DoctorSeed doctorSeed : doctorSeedProperties.getItems()) {
            if (doctorProfileRepository.findByCmp(doctorSeed.getCmp()).isPresent()) {
                log.info("Skipping doctor seed for cmp {} because it already exists", doctorSeed.getCmp());
                continue;
            }

            if (userRepository.findByEmail(doctorSeed.getEmail()).isPresent()) {
                log.warn("Skipping doctor seed for cmp {} because email {} already exists", doctorSeed.getCmp(), doctorSeed.getEmail());
                continue;
            }

            if (userRepository.existsByDni(doctorSeed.getDni())) {
                log.warn("Skipping doctor seed for cmp {} because dni {} already exists", doctorSeed.getCmp(), doctorSeed.getDni());
                continue;
            }

            User user = User.builder()
                    .firstName(doctorSeed.getFirstName())
                    .lastName(doctorSeed.getLastName())
                    .dni(doctorSeed.getDni())
                    .email(doctorSeed.getEmail())
                    .phoneNumber(doctorSeed.getPhoneNumber())
                    .password(passwordEncoder.encode(doctorSeed.getPassword()))
                    .role(Role.DOCTOR)
                    .build();

            user = userRepository.save(user);

            DoctorProfile doctorProfile = new DoctorProfile();
            doctorProfile.setUser(user);
            doctorProfile.setCmp(doctorSeed.getCmp());
            doctorProfile.setMedicalSpecialty(doctorSeed.getMedicalSpecialty());
            doctorProfileRepository.save(doctorProfile);

            seededDoctors++;
        }

        log.info("Seeded {} doctor accounts", seededDoctors);
    }
}
