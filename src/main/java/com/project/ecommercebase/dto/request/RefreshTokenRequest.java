package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(@NotEmpty @NotBlank String refreshToken) {}
