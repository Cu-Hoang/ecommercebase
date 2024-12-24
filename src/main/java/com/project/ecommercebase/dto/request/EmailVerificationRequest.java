package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;

public record EmailVerificationRequest(
        @Email @NotEmpty @NotBlank String email, @Range(min = 100000, max = 999999) Integer verificationCode) {}
