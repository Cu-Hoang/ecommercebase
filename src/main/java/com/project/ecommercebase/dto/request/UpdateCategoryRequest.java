package com.project.ecommercebase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.UUID;

public record UpdateCategoryRequest(@NotEmpty @NotBlank String name, @NotEmpty @NotBlank @UUID String parent_id) {}
