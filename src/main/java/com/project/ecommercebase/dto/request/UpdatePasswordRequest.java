package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UpdatePasswordRequest(@NotEmpty @NotBlank String oldPassword, @NotEmpty @NotBlank String newPassword) {}
