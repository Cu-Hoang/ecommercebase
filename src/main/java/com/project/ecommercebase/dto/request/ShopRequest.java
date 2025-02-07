package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ShopRequest(@NotEmpty @NotBlank String name, @NotEmpty @NotBlank String province) {}
