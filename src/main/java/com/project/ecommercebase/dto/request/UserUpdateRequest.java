package com.project.ecommercebase.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;

public record UserUpdateRequest(
        @Email @NotEmpty @NotBlank String email,
        String firstname,
        String lastname,
        String username,
        String avatar,
        @Past LocalDate dateOfBirth) {}
