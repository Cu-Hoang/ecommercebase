package com.project.ecommercebase.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

public record UserResponse(
        UUID id,
        String firstname,
        String lastname,
        String username,
        String email,
        LocalDate dateOfBirth,
        String avatar) {}
