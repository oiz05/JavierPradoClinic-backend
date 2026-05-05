package com.clinica_javierprado.cjp_backend.AuthenticationContext.domain.entities;

import java.util.UUID;

import com.clinica_javierprado.cjp_backend.AuthenticationContext.domain.valueObjects.Role;

public class User {

    private UUID uuid;
    private Role role;
    private int dni;
    private int phoneNumber;
    private String email;
    

}
