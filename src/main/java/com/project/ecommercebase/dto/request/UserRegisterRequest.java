package com.project.ecommercebase.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;

public record UserRegisterRequest(
        String email,
        String firstname,
        String lastname,
        String username,
        String password,
        @Past LocalDate dateOfBirth) {}
