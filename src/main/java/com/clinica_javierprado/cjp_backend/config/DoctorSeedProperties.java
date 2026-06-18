package com.clinica_javierprado.cjp_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.seed.doctors")
public class DoctorSeedProperties {

    private boolean enabled;
    private List<DoctorSeed> items = new ArrayList<>();

    @Getter
    @Setter
    public static class DoctorSeed {
        private String firstName;
        private String lastName;
        private Integer dni;
        private String email;
        private String phoneNumber;
        private String password;
        private String cmp;
        private String medicalSpecialty;
    }
}
