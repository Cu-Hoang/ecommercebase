package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record EmailRequest(@Email @NotEmpty @NotBlank String email) {}
